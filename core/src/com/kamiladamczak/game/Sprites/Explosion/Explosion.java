package com.kamiladamczak.game.Sprites.Explosion;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.kamiladamczak.game.Screens.PlayScreen;
import com.kamiladamczak.game.Sprites.Bomb;
import com.kamiladamczak.game.Sprites.Player.Player;

public class Explosion {
    private Bomb bomb;

    private Array<Flame> flames;

    private PlayScreen screen;
    private boolean canDestory;


    public Explosion(PlayScreen screen, Player player, Bomb bomb, int power) {
        float x = bomb.getX();
        float y = bomb.getY();
        this.screen = screen;
        flames = new Array<>();
        canDestory = false;

        flames.add(new Flame(screen, player, this, x, y, Flame.Direction.MIDDLE));


        //up
        int brick = 0;
        for(int i = 1; i<=power; i++) {
            boolean wall = false;

            TiledMap map = screen.getMap();
            for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x, y+16*i, 16,16))) {
                    wall = true;
                }
            }
            for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x, y+16*i, 8,8))) {
                    brick++;
                }
            }

            if(wall)
                break;

            if(i == power || brick>=1)
                flames.add(new Flame(screen, player, this, x, y+16*i, Flame.Direction.UP));
            else
                flames.add(new Flame(screen, player, this, x, y+16*i, Flame.Direction.M_UP));
            if(brick>=1) {
                break;
            }
        }
        //right
        brick = 0;
        for(int i = 1; i<=power; i++) {
            boolean wall = false;
            TiledMap map = screen.getMap();
            for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x+16*i, y, 16,16))) {
                    wall = true;
                }
            }

            for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x+16*i, y, 16,16))) {
                    brick++;
                }
            }
            if(wall)
                break;
            if(i == power || brick>=1)
                flames.add(new Flame(screen, player, this, x+16*i, y, Flame.Direction.RIGHT));
            else
                flames.add(new Flame(screen, player, this, x+16*i, y, Flame.Direction.M_RIGHT));
            if(brick>=1)
                break;
        }
        //down
        brick = 0;
        for(int i = 1; i<=power; i++) {
            boolean wall = false;
            TiledMap map = screen.getMap();
            for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x, y-16*i, 16,16))) {
                    wall = true;
                }
            }

            for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x, y-16*i, 16,16))) {
                    brick++;
                }
            }
            if(wall)
                break;

            if(i == power || brick>=1)
                flames.add(new Flame(screen, player, this, x, y-16*i, Flame.Direction.DOWN));
            else
                flames.add(new Flame(screen, player, this, x, y-16*i, Flame.Direction.M_DOWN));
            if(brick>=1)
                break;
        }
        //left
        brick = 0;
        for(int i = 1; i<=power; i++) {
            boolean wall = false;
            TiledMap map = screen.getMap();
            for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x-16*i, y, 16,16))) {
                    wall = true;
                }
            }

            for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x-16*i, y, 16,16))) {
                    brick++;
                }
            }
            if(wall)
                break;

            if(i == power || brick>=1)
                flames.add(new Flame(screen, player, this, x-16*i, y, Flame.Direction.LEFT));
            else
                flames.add(new Flame(screen, player, this, x-16*i, y, Flame.Direction.M_LEFT));
            if(brick>=1)
                break;
        }

    }

    public void update(float dt) {
        for(Flame flame: flames){
            flame.update(dt);
        }

        if(canDestory && flames.isEmpty()) {
            screen.destroyExplosion(this);
        }
    }

    public void draw(SpriteBatch batch) {
        for(Flame flame: flames) {
            flame.draw(batch);
        }
    }

    public void destroyFlame(Flame flame) {
        flames.removeValue(flame, true);
        canDestory = true;
    }

    public Array<Flame> getFlames() {
        return flames;
    }
}
