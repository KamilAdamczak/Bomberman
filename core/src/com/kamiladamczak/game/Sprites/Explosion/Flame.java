package com.kamiladamczak.game.Sprites.Explosion;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Screens.PlayScreen;
import com.kamiladamczak.game.Sprites.Bomb;
import com.kamiladamczak.game.Sprites.Player.Player;

public class Flame extends Sprite {
    private enum Direction {MIDDLE, M_DOWN, M_LEFT, M_UP, M_RIGHT, DOWN, LEFT, UP, RIGHT};
    private World world;
    private PlayScreen screen;
    private Player player;
    private Explosion explosion;
    private Body b2body;
    private Direction dir;

    private BodyDef bdef = new BodyDef();
    private FixtureDef fdef = new FixtureDef();
    private CircleShape shape = new CircleShape();

    private float stateTime;
    private Animation<TextureRegion> animation;

    public Flame(PlayScreen screen, Player player, Explosion explosion, int power, Direction dir) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.player = player;
        this.explosion = explosion;
        this.dir = dir;

        getFrames(dir);
        define();
    }

    private void define() {
        //define middle part
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        shape.setRadius(8);
        fdef.filter.categoryBits = Bomberman.DAMAGE_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData("flames");

    }

    public void update(float dt) {
        stateTime += dt;
        setPosition(b2body.getPosition().x-getWidth()/2, b2body.getPosition().y-getHeight()/2);
        setRegion(animation.getKeyFrame(stateTime,false));
        if(animation.isAnimationFinished(stateTime)) {
            screen.destroyExplosion(this);
            world.destroyBody(this.b2body);
        }
    }

    private void getFrames(Direction dir) {
        Array<TextureRegion> frames = new Array<>();
        switch (dir) {
            case MIDDLE:
                for(int i=0; i<4; i++)
                    frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion_middle"), i*16, 0, 16,16));
                animation = new Animation(0.1f, frames);
                animation.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
                stateTime = 0;
                setBounds(getX(), getY(), 16, 16);
                break;
            case M_DOWN:
                break;
            case M_LEFT:
                break;
            case M_UP:
                break;
            case M_RIGHT:
                break;
            case DOWN:
                break;
            case LEFT:
                break;
            case UP:
                break;
            case RIGHT:
                break;
        }
    }
}
