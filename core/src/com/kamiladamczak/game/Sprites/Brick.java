package com.kamiladamczak.game.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Screens.PlayScreen;
import java.util.Random;

public class Brick extends InteractiveTileObject{
    private PlayScreen screen;

    private Rectangle rectangle;
    public Boolean isDestroyed = false;

    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);

        this.screen = screen;

        rectangle = bounds;

        fixture.setUserData(this); //Set user data of enemy for itself instance. With this you can call functions in ContactListener
        setCategoryFilter(Bomberman.BRICK_BIT);
    }

    @Override

    public void destroy() {
        //set this object as destroyed
        isDestroyed = true;
        world.destroyBody(this.body); //destroy body from world
        setCategoryFilter(Bomberman.DESTROYED_BIT); //set categorybit to DESTROYER_BIT so other entities don't collide with it no more
        getCell().setTile(null); //set cell where this tile is, as empty

        //if brick gets destroyed, it gets random chance to generate a power up on it place
        Random random = new Random();
        int r = random.nextInt(100);
        if(r%6==0) {
            screen.entityManager.addPowerUp(new PowerUp(screen, getPosition().x, getPosition().y, PowerUp.TYPE.BOMB)); //generate bomb power up
        } else if(r%8==0) {
            screen.entityManager.addPowerUp(new PowerUp(screen, getPosition().x, getPosition().y, PowerUp.TYPE.POWER)); //generate fire power power up
        }

        //remove this brick form map
        for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if(rect == rectangle) {
                    map.getLayers().get(3).getObjects().remove(object);
            }
        }

        //remove this brick from entityManager bricks array
        screen.entityManager.removeBrick(this);

    }


}
