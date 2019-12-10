package com.kamiladamczak.game.Sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import com.kamiladamczak.game.Bomberman;

import com.kamiladamczak.game.Scenes.Hud;
import com.kamiladamczak.game.Screens.PlayScreen;

import java.util.Random;

public class Brick extends InteractiveTileObject{
    private Rectangle rectangle;
    public Boolean isDestroyed = false;

    private PlayScreen screen;
    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        this.screen = screen;
        rectangle = bounds;
        fixture.setUserData(this);
        setCategoryFilter(Bomberman.BRICK_BIT);

    }

    @Override
    public void destroy() {
        isDestroyed = true;
        world.destroyBody(this.body);
        setCategoryFilter(Bomberman.DESTORYED_BIT);
        getCell().setTile(null);
        Random random = new Random();
        int r = random.nextInt(100);
        if(r%5==0) {
            screen.addPowerUp(new PowerUp(screen, getPosition().x, getPosition().y, PowerUp.TYPE.BOMB));
        } else if(r%10==0) {
            screen.addPowerUp(new PowerUp(screen, getPosition().x, getPosition().y, PowerUp.TYPE.POWER));
        }

    }


}
