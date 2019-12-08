package com.kamiladamczak.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Screens.PlayScreen;

import static com.badlogic.gdx.math.MathUtils.round;

public class Bomb extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    private int power;
    private float x;
    private float y;

    private boolean collsion = false;


    private float stateTime;
    private Animation<TextureRegion> bombAnimation;
    private Array<TextureRegion> frames;

    public Bomb(PlayScreen screen, float x, float y, int power) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(round(x/16)*16-8,round(y/16)*16-8);
        this.power = power;
        System.out.println("bomb created:"+round(x)+" "+round(y)+" "+power);
        defineBomb();

        frames = new Array<TextureRegion>();
        for(int i=0; i<3; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("bomb"), i*16, 0, 16,16));
        }
        bombAnimation = new Animation(0.5f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16, 16);

    }

    public void update(float dt) {

        stateTime += dt;
        setPosition(b2body.getPosition().x-getWidth()/2, b2body.getPosition().y-getHeight()/2);
        if(!collsion) {

        }
        setRegion(bombAnimation.getKeyFrame(stateTime, true));
    }

    private void defineBomb() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8);

        fdef.filter.categoryBits = Bomberman.SOLID_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData("bomb");
    }
}
