package com.kamiladamczak.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kamiladamczak.game.Screens.PlayScreen;


public class PowerUp extends Sprite{
    //private World world;
    private PlayScreen screen;

    public enum TYPE {BOMB, POWER};
    private TYPE type;


    private TextureRegion sprite;

    public PowerUp(PlayScreen screen, float x, float y, TYPE type) {
        this.screen = screen;
        this.type = type;
        setPosition(x+8,y+8);
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


    public TYPE getType() {
        return type;
    }

    public void destroy() {
        screen.entityManager.removePowerUp(this);
    }

}
