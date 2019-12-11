package com.kamiladamczak.game.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.kamiladamczak.game.Screens.PlayScreen;
import com.kamiladamczak.game.Sprites.Player.Player;

public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;

    //Enemy definition gets screen, x.y for position and dir for movement type
    public Enemy(PlayScreen screen, float x, float y, String dir) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        switch (dir) {
            //setting up horizontal movement for enemy
            case "H":
                velocity = new Vector2(30, 0);
                break;
            //setting up vertical movement for enemy
            case "V":
                velocity = new Vector2(0, 30);
                break;
        }
    }

    protected abstract void defineEnemy();

    public abstract void onHitByFire(Player player);

    //make enemy go into reversed direction after hitting obstacle
    public void reverseVelocity(boolean x, boolean y) {
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }
}
