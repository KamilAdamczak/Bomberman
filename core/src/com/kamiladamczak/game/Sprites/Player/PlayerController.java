package com.kamiladamczak.game.Sprites.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.kamiladamczak.game.Screens.PlayScreen;
import com.kamiladamczak.game.Sprites.Bomb;

public class PlayerController {
    private PlayScreen screen;

    private Player player;

    public PlayerController(PlayScreen screen, Player player) {
        this.screen = screen;
        this.player = player;
    }

    public void update(float dt) {
        /*TODO:
            change form checking for specific key, to key assigned to player (for example: player.keyUP)
        */

        //if player isn't in death state controll it
        if(player.getState() != Player.State.DEATH) {
            //reset Linear velocity to 0
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                player.b2body.setLinearVelocity(new Vector2(player.b2body.getLinearVelocity().x, 0f));
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                player.b2body.setLinearVelocity(new Vector2(player.b2body.getLinearVelocity().x, 0f));
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                player.b2body.setLinearVelocity(new Vector2(0f, player.b2body.getLinearVelocity().y));
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                player.b2body.setLinearVelocity(new Vector2(0f, player.b2body.getLinearVelocity().y));
            }

            //add Linear velocity in pressed direction
            if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().y <= Player.MAXSPEED) {
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
                player.dir = Player.Direction.UP;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.b2body.getLinearVelocity().y >= -Player.MAXSPEED) {
                player.b2body.applyLinearImpulse(new Vector2(0, -4f), player.b2body.getWorldCenter(), true);
                player.dir = Player.Direction.DOWN;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= Player.MAXSPEED) {
                player.b2body.applyLinearImpulse(new Vector2(4f, 0), player.b2body.getWorldCenter(), true);
                player.dir = Player.Direction.RIGHT;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x > -Player.MAXSPEED) {
                player.b2body.applyLinearImpulse(new Vector2(-4f, 0), player.b2body.getWorldCenter(), true);
                player.dir = Player.Direction.LEFT;
            }

            //check if player have bombs if so create on after pressing spacebar
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && screen.entityManager.getBombs().size < player.bombs) {

                //getting fixed player position
                Vector2 playerPosition = new Vector2(((int)player.b2body.getPosition().x)/16, (int)(player.b2body.getPosition().y/16));

                //check if cell where player want to put bomb are no other bombs
                boolean canCreate = false;
                if (screen.entityManager.getBombs().isEmpty()) { //if array of bombs is empty you alway can create a bomb
                    canCreate = true;
                } else {
                    for (Bomb bomb : screen.entityManager.getBombs()) {
                        if ((int) bomb.getX() == playerPosition.x * 16 && (int) bomb.getY() == playerPosition.y * 16) {
                            canCreate = false;
                            break;
                        } else {
                            canCreate = true;
                        }
                    }
                }
                //if there are any obstacles, create new instance of Bomb class in entityManager bombs array
                if (canCreate) {
                    screen.entityManager.newBomb(new Bomb(screen, player, playerPosition.x, playerPosition.y, player.power));
                }

            }

            //slowing down after key release
            if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                if (Math.abs(player.b2body.getLinearVelocity().y) < 10) {
                    player.b2body.setLinearVelocity(player.b2body.getLinearVelocity().x, 0);
                } else
                    player.b2body.setLinearVelocity(player.b2body.getLinearVelocity().x, player.b2body.getLinearVelocity().y * player.STOPFACTOR);
            }
            if (!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                if (Math.abs(player.b2body.getLinearVelocity().x) < 10) {
                    player.b2body.setLinearVelocity(0, player.b2body.getLinearVelocity().y);
                } else
                    player.b2body.setLinearVelocity(player.b2body.getLinearVelocity().x * player.STOPFACTOR, player.b2body.getLinearVelocity().y);

            }
        }

    }
}
