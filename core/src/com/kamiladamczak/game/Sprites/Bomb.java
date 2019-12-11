package com.kamiladamczak.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Screens.PlayScreen;
import com.kamiladamczak.game.Sprites.Explosion.Explosion;
import com.kamiladamczak.game.Sprites.Player.Player;


public class Bomb extends Sprite {
    private World world;
    private PlayScreen screen;
    private Player player;
    private Body b2body;
    private int power;
    private float x;
    private float y;

    private BodyDef bdef = new BodyDef();
    private FixtureDef fdef = new FixtureDef();
    private PolygonShape shape = new PolygonShape();

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

        defineBomb();

        frames = new Array<>();
        for(int i=0; i<3; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("bomb"), i*16, 0, 16,16));
        }
        bombAnimation = new Animation(0.5f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16, 16);
    }

    private void defineBomb() {
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(getX(),getY());

        b2body = world.createBody(bdef);

        shape.setAsBox(5, 5);
        fdef.shape = shape;
        fdef.filter.categoryBits = Bomberman.DESTROYED_BIT;
        b2body.createFixture(fdef);
        b2body.setUserData(this);
    }

    public void update(float dt) {
        stateTime += dt;
        detonationTime += dt;
        setPosition(b2body.getPosition().x-getWidth()/2, b2body.getPosition().y-getHeight()/2);

        //activating collision after player get off form bomb
        if(!collsion) {
            if(!Intersector.overlaps(player.getBoundingRectangle(),this.getBoundingRectangle())) {
                collsion = true;
            }
        } else {
            if(b2body.getFixtureList().first().getFilterData().categoryBits != Bomberman.BOMB_BIT) {
                b2body.getFixtureList().removeValue(b2body.getFixtureList().first(), true);
                Fixture fixture;
                fixture = b2body.createFixture(fdef);
                Filter filter = new Filter();
                filter.categoryBits = Bomberman.BOMB_BIT;
                fdef.filter.maskBits = Bomberman.ENEMY_BIT;
                fixture.setFilterData(filter);
            }
        }

        setRegion(bombAnimation.getKeyFrame(stateTime, true));

        //If bomb is more than 2s on screen make it BOOM!
        if(detonationTime >= 2f) {
            detonate();
        }
    }

    //if bomb make BOOM, destroy it b2body from world, and remove it from array
    private void boom() {
        world.destroyBody(b2body);
        screen.entityManager.destroyBomb(this);
    }

    //if bomb detonate create explosion
    public void detonate() {
        screen.entityManager.newExplosion(new Explosion(screen, player, this, power));
        this.boom();
    }

    //function that get's called when flames collide with bomb
    public void otherDetonate() {
        detonationTime+= 1.5f;
    }
}
