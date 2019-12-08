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
