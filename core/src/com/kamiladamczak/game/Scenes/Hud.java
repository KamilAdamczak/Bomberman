package com.kamiladamczak.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Screens.PlayScreen;

public class Hud implements Disposable {
    public Stage stage;

    private static Integer score;
    private static Integer lives;
    private static Integer bombs;
    private static Integer power;

    private static Label  scoreLabel;
    private Label livesLabel;
    private Label bombsLabel;
    private Label powerLabel;

    public Hud(SpriteBatch sb)  {
        score = 0;
        lives = 0;
        bombs = 0;
        power = 0;

        Viewport viewport = new FitViewport(Bomberman.WIDTH,Bomberman.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        BitmapFont myFont;
        myFont = new BitmapFont();
        myFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        myFont.getData().setScale(.5f);
        scoreLabel = new Label(String.format("SCORE: %d", score), new Label.LabelStyle(myFont, Color.RED));
        livesLabel = new Label(String.format("LIVES: %d", lives), new Label.LabelStyle(myFont, Color.RED));
        bombsLabel = new Label(String.format("BOMBS: %d", bombs), new Label.LabelStyle(myFont, Color.RED));
        powerLabel = new Label(String.format("POWER: %d", power), new Label.LabelStyle(myFont, Color.RED));
        table.add(livesLabel).padLeft(10).padTop(2);
        table.add(bombsLabel).padLeft(10).padTop(2);
        table.add(powerLabel).padLeft(10).padTop(2);
        table.add(scoreLabel).padLeft(10).padTop(2);

        stage.addActor(table);
    }

    public void update(float dt, PlayScreen screen)  {
        score = screen.entityManager.getPlayer().score;
        lives = screen.entityManager.getPlayer().lives;
        bombs = screen.entityManager.getPlayer().bombs-screen.entityManager.getBombs().size;
        power = screen.entityManager.getPlayer().power;

        scoreLabel.setText(String.format("SCORE: %d", score));
        livesLabel.setText(String.format("LIVES: %d", lives));
        bombsLabel.setText(String.format("BOMBS: %d", bombs));
        powerLabel.setText(String.format("POWER: %d", power));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
