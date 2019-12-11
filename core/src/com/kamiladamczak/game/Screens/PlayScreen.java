package com.kamiladamczak.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Scenes.Hud;
import com.kamiladamczak.game.Tools.B2WorldCreator;
import com.kamiladamczak.game.Tools.EntityManager;
import com.kamiladamczak.game.Tools.WorldContactListener;

public class PlayScreen implements Screen {
    //
    private TextureAtlas atlas;
    private Bomberman game;

    //Camera
    private OrthographicCamera gamecam;
    private Viewport gamePort;

    //HUD
    private Hud hud;

    //Tiled Map variables
    public TiledMap map;
    private OrthogonalTiledMapRenderer renderer;


    //Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;

    public EntityManager entityManager;

    private boolean debug = false;
    public PlayScreen(Bomberman game) {
        this.game = game;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas("Sprite.pack");
        //Create cam used to follow player through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virutal aspect ratio despite screen size
        gamePort = new FitViewport(Bomberman.WIDTH, Bomberman.HEIGHT, gamecam);

        //create ouor game HUD for scores/timers/level info
        hud = new Hud(game.batch);

        //Load our map and setup our map renderer
        TmxMapLoader maploader = new TmxMapLoader();
        map = maploader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        //initially set our gamecam to be centered correctly at the start of map
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();
        entityManager = new EntityManager(world, this);
        new B2WorldCreator(this);

        world.setContactListener(new WorldContactListener());
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void handleInput(float dt) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            debug = !debug;
    }

    public void update(float dt) {
        handleInput(dt);
        entityManager.update(dt);

        world.step(1/60f, 0,2);
        hud.update(dt, this);

        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void render(float v) {
        update(v);
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();

        if(debug)
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        entityManager.draw(game.batch);

        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
    }


    public Vector2 getGridPosition(float x, float y) {
        return new Vector2(((int)x/16), ((int)y/16));
    }
}
