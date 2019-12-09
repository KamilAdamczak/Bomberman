package com.kamiladamczak.game.Sprites.Explosion;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Screens.PlayScreen;
import com.kamiladamczak.game.Sprites.Bomb;
import com.kamiladamczak.game.Sprites.Player.Player;

public class Explosion extends Sprite {
    private World world;
    private PlayScreen screen;
    private Player player;
    private Bomb bomb;
    private Body b2body;
    private int power;

    private Array<Body> b2bodys;

    private BodyDef bdef = new BodyDef();
    private FixtureDef fdef = new FixtureDef();
    private CircleShape shape = new CircleShape();

    private float stateTime;
    private Animation<TextureRegion> expolsionAnimation;
    private Array<TextureRegion> frames;

    private Array<Flame> flames;


    public Explosion(PlayScreen screen, Player player, Bomb bomb, int power) {
        flames = new Array<>();
        this.world = screen.getWorld();
        this.screen = screen;
        this.player = player;
        this.bomb = bomb;
        this.power = power;
        setPosition(bomb.getX()+8, bomb.getY()+8);
        b2bodys = new Array<>();
        define();

        frames = new Array<>();
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion_middle"), i*16, 0, 16,16));
        }
        expolsionAnimation = new Animation(0.1f, frames);
        expolsionAnimation.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
        stateTime = 0;
        setBounds(getX(), getY(), 16, 16);

        System.out.println("new fire from:"+this.player+" of power: "+power);
    }

    public void update(float dt) {
        stateTime += dt;
        setPosition(b2body.getPosition().x-getWidth()/2, b2body.getPosition().y-getHeight()/2);
        setRegion(expolsionAnimation.getKeyFrame(stateTime,false));
        if(expolsionAnimation.isAnimationFinished(stateTime)) {
            screen.destroyExplosion(this);
            world.destroyBody(this.b2body);
        }
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

        //define right part
        for(int i=0; i<power; i++) {

        }
    }
}
