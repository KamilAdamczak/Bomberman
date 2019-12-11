package com.kamiladamczak.game.Sprites.Player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Screens.PlayScreen;
import com.kamiladamczak.game.Sprites.Explosion.Explosion;
import com.kamiladamczak.game.Sprites.Explosion.Flame;
import com.kamiladamczak.game.Sprites.PowerUp;


public class Player extends Sprite {
    public final static float STOPFACTOR = .00f;
    public final static float MAXSPEED = 80f;



    public World world;
    private PlayScreen screen;

    public  Body b2body;

    public enum State {DOWN, LEFT, UP, RIGHT, STOP, DEATH}
    private State currentState;
    private State previousState;

    public enum Direction {DOWN, LEFT, UP, RIGHT}
    public Direction dir;

    private TextureRegion horiStand;
    private TextureRegion downStand;
    private TextureRegion upStand;
    private Animation<TextureRegion> horiRun;
    private Animation<TextureRegion> downRun;
    private Animation<TextureRegion> upRun;
    private Animation<TextureRegion> death;
    private float stateTimer;
    private boolean blink;
    private boolean visible;
    private float vTimer;
    private float blinkTimer;

    public boolean invincible = false;

    public int power = 1;
    public int bombs = 1;
    public int lives = 3;
    public int score = 0;

    public Player(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("player_down"));
        this.screen = screen;
        //setting up simple state machine
        currentState = State.DOWN;
        previousState = currentState;

        blink = false;
        visible = true;

        stateTimer = vTimer =  blinkTimer = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i=0; i<3; i++)
            frames.add(new TextureRegion(getTexture(), 401+i*16,33,14,16));
        horiRun = new Animation(.1f, frames);
        frames.clear();
        for(int i=0; i<3; i++)
            frames.add(new TextureRegion(getTexture(), 354+i*16,15,14,16));
        downRun = new Animation(.1f, frames);
        frames.clear();
        for(int i=0; i<3; i++)
            frames.add(new TextureRegion(getTexture(), 308+i*16,15,14,16));
        upRun = new Animation(.1f, frames);
        frames.clear();

        for(int i=0; i<7;i++)
            frames.add(new TextureRegion(getTexture(), 109+i*16, 33, 16,16));
        death = new Animation(.2f,frames);
        frames.clear();

        downStand = new TextureRegion(screen.getAtlas().findRegion("player_down"), 15,0,14,16);
        upStand = new TextureRegion(screen.getAtlas().findRegion("player_up"), 15,0,14,16);
        horiStand = new TextureRegion(screen.getAtlas().findRegion("player_side"), 15,0,14,16);

        this.world = world;
        definePlayer();
        setBounds(0,0, 16,16);
        setRegion(upStand);
        dir = Direction.DOWN;
    }

    public void update(float dt) {
        vTimer += dt;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight()/2);
        setRegion(getFrame(dt));

        //check for collision with power up
        for(PowerUp powerUp:screen.entityManager.getPowerUps()) {
            if(Intersector.overlaps(getBoundingRectangle(), new Rectangle(powerUp.getX()+4, powerUp.getY()+4, 8,8))) {
                addPower(powerUp.getType()); //add power base on collided powerUp type
                powerUp.destroy(); //destroy collided powerUp
            }
        }

        //check for collision with flames
        for(Explosion e: screen.entityManager.getExplosions()) {
            for(Flame f: e.getFlames()) {
                if(Intersector.overlaps(getBoundingRectangle(), new Rectangle(f.getX()+4, f.getY()+4, 8,8))) {
                    if(!invincible && currentState != State.DEATH)
                        kill(); //if player collide with flame call kill() function
                }
            }
        }

        //blink after respawn
        if(blink) {
            blinkTimer += dt;
            invincible = true;
            if(vTimer >= .1) {
                visible = !visible;
                vTimer = 0;
            }
            if(blinkTimer>3) {
                invincible = false;
                blink = false;
                visible = true;
                blinkTimer = 0;
            }
        }

        if(visible)
            setAlpha(1);
         else
            setAlpha(0);

        //if death animation has ended, and player state is dead, respawn player
        if(currentState.equals(State.DEATH) && death.isAnimationFinished(stateTimer))
            respawn();
    }

    //powerup add power u know what it does
    private void addPower(PowerUp.TYPE type) {
        switch (type) {
            case BOMB:
                bombs++;
                break;
            case POWER:
                power++;
                break;
        }
    }

    //if player respawn, set position as upper left corner, activate blinking,
    // change maskbit back as it was so player can collide with other objects
    private void respawn() {
        currentState = State.STOP;
        b2body.setTransform(24,216,0);
        blink = true;
        Filter f = new Filter();
        f.maskBits = Bomberman.SOLID_BIT | Bomberman.BRICK_BIT | Bomberman.ENEMY_BIT | Bomberman.BOMB_BIT;
        f.categoryBits = Bomberman.PLAYER_BIT;
        b2body.getFixtureList().first().setFilterData(f);
    }

    //return frame base on current state
    private TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case DEATH:
                region = death.getKeyFrame(stateTimer, false);
                break;
            case DOWN:
                region = downRun.getKeyFrame(stateTimer, true);

                break;
            case LEFT:
                region = horiRun.getKeyFrame(stateTimer, true);
                if(!region.isFlipX())
                    region.flip(true, false);
                break;
            case RIGHT:
                region = horiRun.getKeyFrame(stateTimer, true);
                if(region.isFlipX())
                    region.flip(true, false);
                break;
            case UP:
                region = upRun.getKeyFrame(stateTimer, true);
                break;
            case STOP:
                    if(dir == Direction.LEFT) {
                        region = horiStand;
                        if(!region.isFlipX())
                            region.flip(true, false);
                    } else if(dir == Direction.RIGHT) {
                        region = horiStand;
                        if(region.isFlipX())
                            region.flip(true, false);
                    } else if(dir == Direction.UP) {
                        region = upStand;
                    } else if(dir == Direction.DOWN) {
                        region = downStand;
                    } else {
                        region = upStand;
                    }
                break;
            default:
                region = horiStand;
                break;
        }

        //if currentState change set stateTimer back to 0
        stateTimer = currentState == previousState ? stateTimer + dt : 0;

        previousState = currentState;
        return region;
    }

    private void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(24,216);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7.5f);
        fdef.filter.categoryBits = Bomberman.PLAYER_BIT;
        fdef.filter.maskBits = Bomberman.SOLID_BIT | Bomberman.BRICK_BIT | Bomberman.ENEMY_BIT | Bomberman.BOMB_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    //kill player, change it state to death, set linealVelocity to 0, so it can't move, change maskBits so it won't collide with other object;
    public void kill() {
        if(currentState != State.DEATH) {
            b2body.setLinearVelocity(new Vector2(0, 0));
            lives--;
            currentState = State.DEATH;
            Filter f = new Filter();
            f.maskBits =  0;
            b2body.getFixtureList().first().setFilterData(f);
        }
    }

    //get player state base on velocity
    public State getState() {
        if(currentState.equals(State.DEATH))
            return State.DEATH;

        if(b2body.getLinearVelocity().y < 0)
            return State.UP;
            //if negative in Y-Axis mario is falling
        else if(b2body.getLinearVelocity().y > 0)
            return State.DOWN;
            //if mario is positive or negative in the X axis he is running
        else if(b2body.getLinearVelocity().x < 0)
            return State.LEFT;
        else if(b2body.getLinearVelocity().x > 0)
            return State.RIGHT;
            //if none of these return then he must be standing
        else
            return State.STOP;

    }

}
