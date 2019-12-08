package com.kamiladamczak.game.Sprites.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.kamiladamczak.game.Screens.PlayScreen;
import com.kamiladamczak.game.Sprites.Bomb;

import static com.badlogic.gdx.math.MathUtils.round;

public class PlayerController {
    private Player player;
    private PlayScreen screen;
    public PlayerController(Player player, PlayScreen screen) {
        this.player = player;
        this.screen = screen;
    }

    public void update(float dt) {

        if(Gdx.input.isKeyPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().y <= Player.MAXSPEED) {
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            player.dir = Player.Direction.UP;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.b2body.getLinearVelocity().y >= -Player.MAXSPEED) {
            player.b2body.applyLinearImpulse(new Vector2(0, -4f), player.b2body.getWorldCenter(), true);
            player.dir = Player.Direction.DOWN;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= Player.MAXSPEED) {
            player.b2body.applyLinearImpulse(new Vector2(4f,0), player.b2body.getWorldCenter(), true);
            player.dir = Player.Direction.RIGHT;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x > -Player.MAXSPEED)  {
            player.b2body.applyLinearImpulse(new Vector2(-4f,0), player.b2body.getWorldCenter(), true);
            player.dir = Player.Direction.LEFT;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            //Put a bomb
            boolean canCreate = false;
            if(screen.getBombs().isEmpty()) {
                canCreate = true;
            } else {
                for (Bomb bomb : screen.getBombs()) {
                    if (bomb.getX() != round(player.b2body.getPosition().x/16)*16-16 || bomb.getY() != round(player.b2body.getPosition().y/16)*16-16) {
                        canCreate = true;
                    } else {
                        canCreate = false;
                    }
                }
            }

            if(canCreate) {
                screen.newBomb(new Bomb(screen, round(player.b2body.getPosition().x), round(player.b2body.getPosition().y), 1));
            }

        }
        //slowing down afret key release
        if(!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if(Math.abs(player.b2body.getLinearVelocity().y) < 10) {
                player.b2body.setLinearVelocity(player.b2body.getLinearVelocity().x, 0);
            } else
                player.b2body.setLinearVelocity(player.b2body.getLinearVelocity().x, player.b2body.getLinearVelocity().y*player.STOPFACTOR);
        }
        if(!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if(Math.abs(player.b2body.getLinearVelocity().x) < 10) {
                player.b2body.setLinearVelocity(0, player.b2body.getLinearVelocity().y);
            } else
                player.b2body.setLinearVelocity(player.b2body.getLinearVelocity().x*player.STOPFACTOR, player.b2body.getLinearVelocity().y);

        }
    }
}
