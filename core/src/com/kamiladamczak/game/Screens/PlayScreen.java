package com.kamiladamczak.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.tools.WorldContactListener;

public class PlayScreen implements Screen {
    private final Bomberman game;
    private final float CELLSIZE = 16;
    private final float WIDTH = 19*CELLSIZE;
    private final float HEIGHT = 15*CELLSIZE;

    //Camera
    private OrthographicCamera gamecam;
    private Viewport gamePort;

    //Tiled Map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;

    //HUD




    public PlayScreen(Bomberman game) {
        this.game = game;

        //Create cam used to follow player through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virutal aspect ratio despite screen size
        gamePort = new FitViewport(WIDTH,HEIGHT, gamecam);

        //create ouor game HUD for scores/timers/level info


        //Load our map and setup our map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("level.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        //initially set our gamecam to be centered correctly at the start of map
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();


        world.setContactListener(new WorldContactListener());


    }

    @Override
    public void show() {

    }


    public void update(float dt) {

        world.step(1/60f, 6,2);

        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void render(float v) {
        update(v);

        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gamecam.combined);
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        game.batch.end();

    }

    @Override
    public void resize(int i, int i1) {
        gamePort.update(i, i1);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
