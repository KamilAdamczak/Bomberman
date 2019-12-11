package com.kamiladamczak.game.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Sprites.Bomb;
import com.kamiladamczak.game.Sprites.Enemies.Enemy;
import com.kamiladamczak.game.Sprites.Explosion.Flame;
import com.kamiladamczak.game.Sprites.Player.Player;
import com.kamiladamczak.game.Sprites.Solid;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case Bomberman.ENEMY_BIT | Bomberman.PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == Bomberman.PLAYER_BIT) {
                        if (!((Player) fixA.getUserData()).invincible)
                            ((Player) fixA.getUserData()).kill();
                } else if (fixB.getFilterData().categoryBits == Bomberman.PLAYER_BIT) {
                        if (!((Player) fixB.getUserData()).invincible)
                            ((Player) fixB.getUserData()).kill();
                }
                break;
            case Bomberman.ENEMY_BIT | Bomberman.BOMB_BIT:
            case Bomberman.ENEMY_BIT | Bomberman.SOLID_BIT:
            case Bomberman.ENEMY_BIT | Bomberman.BRICK_BIT:
                if (fixA.getFilterData().categoryBits == Bomberman.ENEMY_BIT) {
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, true);
                } else if(fixB.getFilterData().categoryBits == Bomberman.ENEMY_BIT) {
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, true);
                }
                break;
        }

    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}