package com.kamiladamczak.game.Tools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kamiladamczak.game.Screens.PlayScreen;
import com.kamiladamczak.game.Sprites.Bomb;
import com.kamiladamczak.game.Sprites.Brick;
import com.kamiladamczak.game.Sprites.Enemies.Slime;
import com.kamiladamczak.game.Sprites.Explosion.Explosion;
import com.kamiladamczak.game.Sprites.Player.Player;
import com.kamiladamczak.game.Sprites.Player.PlayerController;
import com.kamiladamczak.game.Sprites.PowerUp;

public class EntityManager {
    private Player player;
    private PlayerController pcon;

    private Array<Bomb> bombs;

    private Array<Explosion> explosions;

    private Array<Brick> bricks;

    private Array<PowerUp> powerUps;

    private Array<Slime> slimes;

    public EntityManager(World world, PlayScreen screen) {
        player = new Player(world, screen);
        pcon = new PlayerController(player, screen);
        bombs = new Array<>();
        explosions = new Array<>();
        bricks = new Array<>();
        powerUps = new Array<>();
        slimes = new Array<>();
    }

    public void handleInput(float dt) {
        pcon.update(dt);
    }

    public void update(float dt) {
        handleInput(dt);
        player.update(dt);

        for(Bomb bomb: bombs) {
            bomb.update(dt);
        }
        for(Explosion explosion: explosions) {
            explosion.update(dt);
        }
        for(Slime slime: slimes) {
            slime.update(dt);
        }
    }

    public void draw(SpriteBatch bt) {
        for(Bomb bomb:bombs) {
            bomb.draw(bt);
        }

        for(PowerUp powerUp: powerUps) {
            powerUp.draw(bt);
        }

        for(Explosion explosion: explosions) {
            explosion.draw(bt);
        }

        for(Slime slime: slimes) {
            slime.draw(bt);
        }

        player.draw(bt);
    }


    public Player getPlayer() {
        return player;
    }

    public Array<Bomb> getBombs() {
        return bombs;
    }
    public void newBomb(Bomb bomb) {
        bombs.add(bomb);
    }
    public void destroyBomb(Bomb bomb) {
        bombs.removeValue(bomb, true);
    }

    public Array<Brick> getBricks() {
        return bricks;
    }
    public void addBrick(Brick brick) {
        bricks.add(brick);
    }
    public void removeBrick(Brick brick) {
        bricks.removeValue(brick, true);
    };

    public Array<Explosion> getExplosions() {
        return explosions;
    }
    public void newExplosion(Explosion explosion) {
        explosions.add(explosion);
    }
    public void destroyExplosion(Explosion explosion) {
        explosions.removeValue(explosion, true);
    }

    public Array<PowerUp> getPowerUps() {
        return powerUps;
    }
    public void addPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp);
    }
    public void removePowerUp(PowerUp powerUp) {
        powerUps.removeValue(powerUp, true);
    }

    public Array<Slime> getSlimes() {
        return slimes;
    };
    public void addSlime(Slime slime) {
        slimes.add(slime);
    }
    public void removeSlime(Slime slime) {
        slimes.removeValue(slime, true);
    };
}
