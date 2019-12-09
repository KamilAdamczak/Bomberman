package com.kamiladamczak.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Scenes.Hud;
import com.kamiladamczak.game.Screens.PlayScreen;

public class Brick{
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    public Brick(PlayScreen screen, Rectangle bounds) {
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() /2), (bounds.getY() + bounds.getHeight() / 2));

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() /2, bounds.getHeight()/2);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
        fixture.setUserData(this);
        setCategoryFilter(Bomberman.BRICK_BIT);
    }
    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int) (body.getPosition().x /16), (int)(body.getPosition().y/16));
    }

    public void Destoryed() {
        Gdx.app.log("Brick", "Collision");
        setCategoryFilter(Bomberman.DESTORYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
    }
}
