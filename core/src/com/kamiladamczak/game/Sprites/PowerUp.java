package com.kamiladamczak.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kamiladamczak.game.Screens.PlayScreen;


public class PowerUp extends Sprite{
    private PlayScreen screen;

    public enum TYPE {BOMB, POWER}
    private TYPE type;

    private TextureRegion sprite;

    public PowerUp(PlayScreen screen, float x, float y, TYPE type) {
        this.screen = screen;
        this.type = type;

        setPosition(x+8,y+8);

        //set sprite depending of it type
        switch (type) {
            case BOMB:
                sprite = new TextureRegion(screen.getAtlas().findRegion("power_ups"),0,0,16,16);
                break;
            case POWER:
                sprite = new TextureRegion(screen.getAtlas().findRegion("power_ups"),16,0,16,16);
                break;
        }
        setRegion(sprite);
        setBounds(x,y,16,16);
    }

    //return this powerup type
    public TYPE getType() {
        return type;
    }

    //remove this powerUp instance from entity manager powerup array
    public void destroy() {
        screen.entityManager.removePowerUp(this);
    }

}
