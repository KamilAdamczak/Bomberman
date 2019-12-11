package com.kamiladamczak.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kamiladamczak.game.Screens.PlayScreen;

public class Bomberman extends Game {


	public static final int CELLSIZE = 16;
	public static final int ROWS = 19;
	public static final int COLS = 15;
	public static final float WIDTH = ROWS*CELLSIZE;
	public static final float HEIGHT = COLS*CELLSIZE;

	public static final short SOLID_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short DAMAGE_BIT = 4;
	public static final short DESTROYED_BIT = 16;
	public static final short BOMB_BIT = 32;
	public static final short BRICK_BIT = 64;
	public static final short ENEMY_BIT = 128;

    public SpriteBatch batch;
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
