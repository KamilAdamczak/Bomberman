package com.kamiladamczak.game.Sprites.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Screens.PlayScreen;
import com.kamiladamczak.game.Sprites.Bomb;

import static com.badlogic.gdx.math.MathUtils.round;

public class Player extends Sprite {
    public final static float STOPFACTOR = .00f;
    public final static float MAXSPEED = 80f;

    public enum State {DOWN, LEFT, UP, RIGHT, STOP};
    public State currentState;
    public State previousState;
    public State stopState = State.STOP;

    public enum Direction {DOWN, LEFT, UP, RIGHT};
    public Direction dir;

    public World world;
    public Body b2body;

    private TextureRegion horiStand;
    private TextureRegion downStand;
    private TextureRegion upStand;
    private Animation<TextureRegion> horiRun;
    private Animation<TextureRegion> downRun;
    private Animation<TextureRegion> upRun;
    private float stateTimer;

    public int power = 1;

    public Player(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("player_down"));
        currentState = State.DOWN;
        previousState = State.DOWN;
        stateTimer = 0;
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

        this.world = world;
        definePlayer();

        downStand = new TextureRegion(screen.getAtlas().findRegion("player_down"), 15,0,14,16);
        upStand = new TextureRegion(screen.getAtlas().findRegion("player_up"), 15,0,14,16);
        horiStand = new TextureRegion(screen.getAtlas().findRegion("player_side"), 15,0,14,16);
        setBounds(0,0, 16,16);
        setRegion(upStand);
        dir = Direction.DOWN;
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight()/2);
        setRegion(getFrame(dt));

    }
    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
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

        stateTimer = currentState == previousState ? stateTimer + dt : 0;

        previousState = currentState;
        return region;
    }

    public State getState() {
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

    private void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(24,216);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8);
        fdef.filter.categoryBits = Bomberman.PLAYER_BIT;
        fdef.filter.maskBits = Bomberman.SOLID_BIT | Bomberman.POWERUP_BIT |Bomberman.BRICK_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData("player");

    }
}
