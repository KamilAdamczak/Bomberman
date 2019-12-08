package com.kamiladamczak.game.Sprites.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class PlayerController {
    private Body b2body;
    private float  stopfactor;
    private Player.Direction dir;
    public PlayerController(Player player) {
        b2body = player.b2body;
        stopfactor = player.STOPFACTOR;
        dir = player.dir;
    }

    public void update(float dt) {

        if(Gdx.input.isKeyPressed(Input.Keys.UP) && b2body.getLinearVelocity().y <= Player.MAXSPEED) {
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            dir = Player.Direction.UP;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && b2body.getLinearVelocity().y >= -Player.MAXSPEED) {
            b2body.applyLinearImpulse(new Vector2(0, -4f), b2body.getWorldCenter(), true);
            dir = Player.Direction.DOWN;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && b2body.getLinearVelocity().x <= Player.MAXSPEED) {
            b2body.applyLinearImpulse(new Vector2(4f,0), b2body.getWorldCenter(), true);
            dir = Player.Direction.RIGHT;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && b2body.getLinearVelocity().x > -Player.MAXSPEED)  {
            b2body.applyLinearImpulse(new Vector2(-4f,0), b2body.getWorldCenter(), true);
            dir = Player.Direction.LEFT;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            //Put a bomb
        }
        //slowing down afret key release
        if(!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if(Math.abs(b2body.getLinearVelocity().y) < 10) {
                b2body.setLinearVelocity(b2body.getLinearVelocity().x, 0);
            } else
                b2body.setLinearVelocity(b2body.getLinearVelocity().x, b2body.getLinearVelocity().y*stopfactor);
        }
        if(!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if(Math.abs(b2body.getLinearVelocity().x) < 10) {
                b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);
            } else
                b2body.setLinearVelocity(b2body.getLinearVelocity().x*stopfactor, b2body.getLinearVelocity().y);

        }
    }
}
