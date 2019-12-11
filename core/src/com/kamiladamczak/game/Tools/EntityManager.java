package com.kamiladamczak.game.Tools;

import com.badlogic.gdx.utils.Array;
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

    private Array<Explosion> expolsions;

    private Array<Brick> bricks;

    private Array<PowerUp> powerUps;

    private Array<Slime> slimes;
    
    public EntityManager() {
        bombs = new Array<>();
        expolsions = new Array<>();
        bricks = new Array<>();
        powerUps = new Array<>();
        slimes = new Array<>();
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

    public Array<Explosion> getExpolsions() {
        return expolsions;
    }
    public void newExplosion(Explosion explosion) {
        expolsions.add(explosion);
    }
    public void destroyExplosion(Explosion explosion) {
        expolsions.removeValue(explosion, true);
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
