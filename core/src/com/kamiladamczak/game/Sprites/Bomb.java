package com.kamiladamczak.game.Sprites;

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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Screens.PlayScreen;
import com.kamiladamczak.game.Sprites.Player.Player;


public class Bomb extends Sprite {
    protected World world;
    protected PlayScreen screen;
    private Player player;
    public Body b2body;
    private int power;
    private float x;
    private float y;



    protected BodyDef bdef = new BodyDef();
    protected FixtureDef fdef = new FixtureDef();
    protected CircleShape shape = new CircleShape();
    protected Fixture fixture;
    private boolean collsion = false;


    private float stateTime;
    private Animation<TextureRegion> bombAnimation;
    private Array<TextureRegion> frames;

    private float detonationTime;

    public Bomb(PlayScreen screen,  Player player, float x, float y, int power) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.player = player;
        this.x = x*Bomberman.CELLSIZE;
        this.y = y*Bomberman.CELLSIZE;
        setPosition((int)this.x+8,(int)this.y+8);
        this.power = power;
        //System.out.println("bomb created:"+this.x+" "+this.y+" "+power);
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
        detonationTime += dt;
        setPosition(b2body.getPosition().x-getWidth()/2, b2body.getPosition().y-getHeight()/2);
        if(!collsion) {
            if(!Intersector.overlaps(player.getBoundingRectangle(),this.getBoundingRectangle())) {
                collsion = true;
            }
        } else {
            fixture = b2body.createFixture(fdef);
            Filter filter = new Filter();
            filter.categoryBits = Bomberman.SOLID_BIT;
            fixture.setFilterData(filter);
        }
        setRegion(bombAnimation.getKeyFrame(stateTime, true));

        if(detonationTime >= 5f) {
            this.boom();
            new Fire(screen, player, power);
        }
    }

    private void boom() {
        System.out.println("BOOM!");
        world.destroyBody(b2body);
        screen.destroyBomb(this);
    }

    private void defineBomb() {
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        shape.setRadius(8);
        fdef.filter.categoryBits = Bomberman.DESTORYED_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData("bomb");
    }
}
