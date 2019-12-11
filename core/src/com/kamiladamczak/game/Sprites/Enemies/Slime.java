package com.kamiladamczak.game.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Screens.PlayScreen;
import com.kamiladamczak.game.Sprites.Explosion.Explosion;
import com.kamiladamczak.game.Sprites.Explosion.Flame;
import com.kamiladamczak.game.Sprites.Player.Player;

public class Slime extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> animation;
    private boolean setToDestroy;
    private boolean destroyed;

    public Slime(PlayScreen screen, float x, float y, String dir) {
        super(screen, x, y, dir);
        Array<TextureRegion> frames = new Array<>();
        for(int i=0; i<2; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("enemy_right"), i*16,0,16,16));
        animation = new Animation(.1f, frames);

        stateTime =0;
        setBounds(getX(), getY(), 16,16);
        setToDestroy = false;
        destroyed = false;
    }

    public void update(float dt) {
        stateTime += dt;
        if(setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("enemy_death"),0,0,16,16));
            stateTime = 0;
        } else if(!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y-getHeight()/2);
            setRegion(animation.getKeyFrame(stateTime, true));
        }

        for(Explosion e: screen.entityManager.getExplosions()) {
            for(Flame f: e.getFlames()) {
                if(Intersector.overlaps(getBoundingRectangle(), new Rectangle(f.getX()+4, f.getY()+4, 8,8))) {
                    if(!setToDestroy)
                        onHitByFire(f.getPlayer());
                }
            }
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5);
        fdef.filter.categoryBits = Bomberman.ENEMY_BIT;
        fdef.filter.maskBits = Bomberman.SOLID_BIT | Bomberman.BRICK_BIT | Bomberman.PLAYER_BIT | Bomberman.BOMB_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch) {
        if(!destroyed || stateTime <1) {
            super.draw(batch);
        } else {
            screen.entityManager.removeSlime(this);
        }
    }

    @Override
    public void onHitByFire(Player player) {
        player.score += 200;
        setToDestroy = true;
    }
}
