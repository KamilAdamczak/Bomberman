package com.kamiladamczak.game.Sprites.Explosion;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Screens.PlayScreen;
import com.kamiladamczak.game.Sprites.Bomb;
import com.kamiladamczak.game.Sprites.Player.Player;
import com.kamiladamczak.game.Sprites.Solid;

public class Explosion {
    private Bomb bomb;

    private Array<Flame> flames;


    public Explosion(PlayScreen screen, Player player, Bomb bomb, int power) {
        float x = bomb.getX();
        float y = bomb.getY();
        flames = new Array<>();

        flames.add(new Flame(screen, player, this, x, y, Flame.Direction.MIDDLE));

        //up
        for(int i = 1; i<=power; i++) {
            boolean wall = false;
            TiledMap map = screen.getMap();
            for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x, y+16*i, 16,16))) {
                    wall = true;
                }
            }
            if (wall)
                    break;

            if(i == power)
                flames.add(new Flame(screen, player, this, x, y+16*i, Flame.Direction.UP));
            else
                flames.add(new Flame(screen, player, this, x, y+16*i, Flame.Direction.M_UP));
        }
        //right
        for(int i = 1; i<=power; i++) {
            boolean wall = false;
            TiledMap map = screen.getMap();
            for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x+16*i, y, 16,16))) {
                    wall = true;
                }
            }
            if (wall)
                break;

            if(i == power)
                flames.add(new Flame(screen, player, this, x+16*i, y, Flame.Direction.RIGHT));
            else
                flames.add(new Flame(screen, player, this, x+16*i, y, Flame.Direction.M_RIGHT));
        }
        //down
        for(int i = 1; i<=power; i++) {
            boolean wall = false;
            TiledMap map = screen.getMap();
            for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x, y-16*i, 16,16))) {
                    wall = true;
                }
            }
            if (wall)
                break;

            if(i == power)
                flames.add(new Flame(screen, player, this, x, y-16*i, Flame.Direction.DOWN));
            else
                flames.add(new Flame(screen, player, this, x, y-16*i, Flame.Direction.M_DOWN));
        }
        //left
        for(int i = 1; i<=power; i++) {
            boolean wall = false;
            TiledMap map = screen.getMap();
            for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x-16*i, y, 16,16))) {
                    wall = true;
                }
            }
            if (wall)
                break;

            if(i == power)
                flames.add(new Flame(screen, player, this, x-16*i, y, Flame.Direction.LEFT));
            else
                flames.add(new Flame(screen, player, this, x-16*i, y, Flame.Direction.M_LEFT));
        }

        System.out.println("new fire from:"+player+" of power: "+power);
    }

    public void update(float dt) {
        for(Flame flame: flames){
            flame.update(dt);
        }
    }

    public void draw(SpriteBatch batch) {
        for(Flame flame: flames) {
            flame.draw(batch);
        }
    }

    public void destroyFlame(Flame flame) {
        flames.removeValue(flame, true);
    }

}
