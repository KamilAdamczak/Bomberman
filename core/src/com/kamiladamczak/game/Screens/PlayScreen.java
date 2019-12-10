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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Scenes.Hud;
import com.kamiladamczak.game.Sprites.Bomb;
import com.kamiladamczak.game.Sprites.Brick;
import com.kamiladamczak.game.Sprites.Explosion.Explosion;
import com.kamiladamczak.game.Sprites.Explosion.Flame;
import com.kamiladamczak.game.Sprites.Player.Player;
import com.kamiladamczak.game.Sprites.Player.PlayerController;
import com.kamiladamczak.game.Sprites.PowerUp;
import com.kamiladamczak.game.Sprites.Solid;
import com.kamiladamczak.game.Tools.B2WorldCreator;
import com.kamiladamczak.game.Tools.WorldContactListener;


public class PlayScreen implements Screen {
    TextureAtlas atlas;
    private Bomberman game;

    //Camera
    private OrthographicCamera gamecam;
    private Viewport gamePort;

    //HUD
    private Hud hud;

    //Tiled Map variables
    private TmxMapLoader maploader;
    public TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private Array<Bomb> bombs = new Array<>();

    private Array<Explosion> expolsions = new Array<>();

    private Array<Brick> bricks = new Array<>();;

    private Array<PowerUp> powerUps = new Array<>();

    //Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;

    private Player player;
    private PlayerController pcon;

    private boolean deboug = false;
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
        maploader = new TmxMapLoader();
        map = maploader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        //initially set our gamecam to be centered correctly at the start of map
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this);
        player = new Player(world, this);
        pcon = new PlayerController(player, this);
        world.setContactListener(new WorldContactListener());
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void handleInput(float dt) {

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            deboug = !deboug;

        pcon.update(dt);

    }


    public void update(float dt) {
        handleInput(dt);
       world.step(1/60f, 0,2);
        hud.update(dt);
        player.update(dt);
        for(Bomb bomb:bombs) {
            bomb.update(dt);
        }
        for(Explosion expolsion:expolsions) {
            expolsion.update(dt);
        }

        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void render(float v) {
        update(v);

        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        if(deboug)
        b2dr.render(world, gamecam.combined);
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        for(Bomb bomb:bombs) {
            bomb.draw(game.batch);
        }

        for(PowerUp powerUp: powerUps) {
            powerUp.draw(game.batch);
        }

        for(Explosion exposion:expolsions) {
            exposion.draw(game.batch);
        }

        player.draw(game.batch);

        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        //hud.stage.draw();

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

    public void newBomb(Bomb bomb) {
        bombs.add(bomb);
    }

    public Array<Bomb> getBombs() {
        return bombs;
    }

    public void destroyBomb(Bomb bomb) {
        bombs.removeValue(bomb, true);
    }

    public Array<Brick> getBricks() {
        return bricks;
    }

    public void addBrick(Brick b) {
        bricks.add(b);
    }

   public Vector2 getGridPosition(float x, float y) {
       return new Vector2(((int)x/16), ((int)y/16));
   }

    public void newExplosion(Explosion explosion) {
        expolsions.add(explosion);
    }

    public void destroyExplosion(Explosion explosion) {
        expolsions.removeValue(explosion, true);
    }

    public Array<Explosion> getExpolsion() {
        return expolsions;
    }

    public Array<PowerUp> getPowerUp() {
        return powerUps;
    }

    public void addPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp);
    }

    public void removePowerUp(PowerUp powerUp) {
        powerUps.removeValue(powerUp, true);
    }
}
