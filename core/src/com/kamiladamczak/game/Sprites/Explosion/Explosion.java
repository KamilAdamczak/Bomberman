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

    //Array of flames that will create our explosion cross
    private Array<Flame> flames;

    private PlayScreen screen;

    private boolean canDestroy;

    public Explosion(PlayScreen screen, Player player, Bomb bomb, int power) {
        float x = bomb.getX();
        float y = bomb.getY();

        this.screen = screen;

        flames = new Array<>();

        canDestroy = false;

        //Add middle part od explosion after bomb detonate
        flames.add(new Flame(screen, player, this, x, y, Flame.Direction.MIDDLE));


        //create upper parts od explosion after bomb detonate
        int brick = 0; //set up brick cout
        for(int i = 1; i<=power; i++) {
            boolean wall = false;

            //check if flame will be colliding with solid, if so don't create it
            TiledMap map = screen.getMap();
            for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x, y+16*i, 16,16))) {
                    wall = true;
                }
            }

            if(wall)
                break;

            //check if flame will be colliding with more than one brick, if so don't create it
            for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x, y+16*i, 8,8))) {
                    brick++;
                }
            }

            //create last part of flames
            if(i == power || brick>=1)
                flames.add(new Flame(screen, player, this, x, y+16*i, Flame.Direction.UP));
            else
                //create middle part of flames
                flames.add(new Flame(screen, player, this, x, y+16*i, Flame.Direction.M_UP));

            //break if there is more than one brick in path
            if(brick>=1) {
                break;
            }
        }
        //Add right parts od explosion after bomb detonate
        brick = 0; //reset brick count
        for(int i = 1; i<=power; i++) {
            boolean wall = false;

            //check if flame will be colliding with solid, if so don't create it
            TiledMap map = screen.getMap();
            for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x+16*i, y, 16,16))) {
                    wall = true;
                }
            }

            if(wall)
                break;

            //check if flame will be colliding with more than one brick, if so don't create it
            for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x+16*i, y, 16,16))) {
                    brick++;
                }
            }

            //create last part of flames
            if(i == power || brick>=1)
                flames.add(new Flame(screen, player, this, x+16*i, y, Flame.Direction.RIGHT));
            else
                //create middle part of flames
                flames.add(new Flame(screen, player, this, x+16*i, y, Flame.Direction.M_RIGHT));

            //break if there is more than one brick in path
            if(brick>=1)
                break;
        }
        //Add bottom parts od explosion after bomb detonate
        brick = 0; //reset brick count
        for(int i = 1; i<=power; i++) {
            boolean wall = false;

            //check if flame will be colliding with solid, if so don't create it
            TiledMap map = screen.getMap();
            for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x, y-16*i, 16,16))) {
                    wall = true;
                }
            }

            if(wall)
                break;

            //check if flame will be colliding with more than one brick, if so don't create it
            for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x, y-16*i, 16,16))) {
                    brick++;
                }
            }

            //create last part of flames
            if(i == power || brick>=1)
                flames.add(new Flame(screen, player, this, x, y-16*i, Flame.Direction.DOWN));
            else
                //create middle part of flames
                flames.add(new Flame(screen, player, this, x, y-16*i, Flame.Direction.M_DOWN));

            //break if there is more than one brick in path
            if(brick>=1)
                break;
        }
        //Add left parts od explosion after bomb detonate
        brick = 0; //reset brick count
        for(int i = 1; i<=power; i++) {
            boolean wall = false;

            //check if flame will be colliding with solid, if so don't create it
            TiledMap map = screen.getMap();
            for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x-16*i, y, 16,16))) {
                    wall = true;
                }
            }

            if(wall)
                break;

            //check if flame will be colliding with more than one brick, if so don't create it
            for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Intersector.overlaps(rect, new Rectangle(x-16*i, y, 16,16))) {
                    brick++;
                }
            }

            //create last part of flames
            if(i == power || brick>=1)
                flames.add(new Flame(screen, player, this, x-16*i, y, Flame.Direction.LEFT));
            else
                //create middle part of flames
                flames.add(new Flame(screen, player, this, x-16*i, y, Flame.Direction.M_LEFT));

            //break if there is more than one brick in path
            if(brick>=1)
                break;
        }

    }

    public void update(float dt) {
        //updating flames from this explosion
        for(Flame flame: flames){
            flame.update(dt);
        }

        //remove this explosion from Array
        if(canDestroy && flames.isEmpty()) {
            screen.entityManager.destroyExplosion(this);
        }
    }

    public void draw(SpriteBatch batch) {
        //animate flames from this explosion
        for(Flame flame: flames) {
            flame.draw(batch);
        }
    }

    //remove flames from this explosion
    public void destroyFlame(Flame flame) {
        flames.removeValue(flame, true);
        canDestroy = true;
    }

    //return Array of flames for for location needs
    public Array<Flame> getFlames() {
        return flames;
    }
}
