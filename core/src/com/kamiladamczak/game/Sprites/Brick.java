package com.kamiladamczak.game.Sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import com.kamiladamczak.game.Bomberman;

import com.kamiladamczak.game.Scenes.Hud;
import com.kamiladamczak.game.Screens.PlayScreen;

public class Brick extends InteractiveTileObject{
    private Rectangle rectangle;
    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        rectangle = bounds;
        fixture.setUserData(this);
        setCategoryFilter(Bomberman.BRICK_BIT);
    }

    @Override
    public void destroy() {
        setCategoryFilter(Bomberman.DESTORYED_BIT);
        getCell().setTile(null);
    }


}
