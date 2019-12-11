package com.kamiladamczak.game.Sprites.Explosion;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Screens.PlayScreen;
import com.kamiladamczak.game.Sprites.Brick;
import com.kamiladamczak.game.Sprites.Player.Player;

public class Flame extends Sprite {


    private World world;
    private PlayScreen screen;

    private Player player;

    private Explosion explosion;

    private Body b2body;

    //Direction enum that will define which sprite will be draw for this flame instance
    public enum Direction {MIDDLE, M_DOWN, M_LEFT, M_UP, M_RIGHT, DOWN, LEFT, UP, RIGHT};
    private Direction dir;

    private BodyDef bdef = new BodyDef();
    private FixtureDef fdef = new FixtureDef();
    private CircleShape shape = new CircleShape();

    private float stateTime;
    private Animation<TextureRegion> animation;

    public Flame(PlayScreen screen, Player player, Explosion explosion, float x, float y, Direction dir) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.player = player; //getting player instance so the game will know who generate this flame if it kill enemy
        this.explosion = explosion;
        this.dir = dir;
        setPosition(x+8,y+8);
        getFrames(this.dir);
        checkForCollision();
        define();
    }

    //set sprite for given direction
    private void getFrames(Direction dir) {
        Array<TextureRegion> frames = new Array<>();
        switch (dir) {
            case MIDDLE:
                for(int i=0; i<4; i++)
                    frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion_middle"), i*16, 0, 16,16));
                break;
            case M_DOWN:
                for(int i=0; i<4; i++)
                    frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion_hori_middle"), i*16, 0, 16,16));
                for(TextureRegion f: frames)
                    f.flip(false, true); //reverse frames
                break;
            case M_LEFT:
                for(int i=0; i<4; i++)
                    frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion_verti_middle"), i*16, 0, 16,16));
                for(TextureRegion f: frames)
                    f.flip(true, false); //reverse frames
                break;
            case M_UP:
                for(int i=0; i<4; i++)
                    frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion_hori_middle"), i*16, 0, 16,16));
                break;
            case M_RIGHT:
                for(int i=0; i<4; i++)
                    frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion_verti_middle"), i*16, 0, 16,16));
                break;
            case DOWN:
                for(int i=0; i<4; i++)
                    frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion_hori_end"), i*16, 0, 16,16));
                for(TextureRegion f: frames)
                    f.flip(false, true); //reverse frames
                break;
            case LEFT:
                for(int i=0; i<4; i++)
                    frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion_verti_end"), i*16, 0, 16,16));
                for(TextureRegion f: frames)
                    f.flip(true, false); //reverse frames
                break;
            case UP:
                for(int i=0; i<4; i++)
                    frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion_hori_end"), i*16, 0, 16,16));
                break;
            case RIGHT:
                for(int i=0; i<4; i++)
                    frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion_verti_end"), i*16, 0, 16,16));
                break;
        }
        animation = new Animation(0.1f, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
        stateTime = 0;
        setBounds(getX(), getY(), 16, 16);
    }

    //generate b2body
    private void define() {
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);
        shape.setRadius(8);
        fdef.filter.categoryBits = Bomberman.DAMAGE_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this); //Set user data of enemy for itself instance. With this you can call functions in ContactListener
    }

    private void checkForCollision() {
        //check for collision with brick
        TiledMap map = screen.getMap();
        for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle(); //brick rectangle
            Rectangle pRect = new Rectangle(this.getX()-8, this.getY()-8, 16, 16); //generate flame rectangle
            if (Intersector.overlaps(rect, pRect)) {
                for(Brick brick:screen.entityManager.getBricks()) {
                    if(brick.getPosition().x == rect.getX() && brick.getPosition().y == rect.getY()) {
                        if(!brick.isDestroyed)
                            brick.destroy(); //destroy brick
                    }
                }
            }
        }

        //check for collision with bombs
        for(int i=0; i <screen.entityManager.getBombs().size; i++) {
            if(Intersector.overlaps(screen.entityManager.getBombs().get(i).getBoundingRectangle(), new Rectangle(getX(), getY(), 8,8))){
                screen.entityManager.getBombs().get(i).otherDetonate(); //detonate bomb if it collide with flames
            }
        }
    }

    public void update(float dt) {
        stateTime += dt;
        setPosition(b2body.getPosition().x-getWidth()/2, b2body.getPosition().y-getHeight()/2);
        setRegion(animation.getKeyFrame(stateTime,false));
        //if animation ends remove flames from array in explosion object that generate it, and remove b2d from world
        if(animation.isAnimationFinished(stateTime)) {
            explosion.destroyFlame(this);
            world.destroyBody(this.b2body);
        }
    }

    //return player that generate this crazy flames
    public Player getPlayer() {
        return player;
    }
}
