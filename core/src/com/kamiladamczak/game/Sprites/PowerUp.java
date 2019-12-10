package com.kamiladamczak.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kamiladamczak.game.Bomberman;
import com.kamiladamczak.game.Screens.PlayScreen;
import com.kamiladamczak.game.Sprites.Player.Player;

public class PowerUp extends Sprite{
    private World world;
    private PlayScreen screen;
    private Body b2body;


    public enum TYPE {BOMB, POWER};
    private TYPE type;

    private BodyDef bdef = new BodyDef();
    private FixtureDef fdef = new FixtureDef();
    private PolygonShape shape = new PolygonShape();
    private boolean collsion = false;


    private TextureRegion sprite;


    public PowerUp(PlayScreen screen, float x, float y, TYPE type) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.type = type;
        setPosition(x+8,y+8);
        System.out.println("create powerup: "+type+" "+x+" "+y);

        sprite = new TextureRegion(screen.getAtlas().findRegion("power_ups"),0,0,16,16);
        setRegion(sprite);
        setBounds(x,y,16,16);
    }


    public TYPE getType() {
        return type;
    }

    public void destory() {
        screen.removePowerUp(this);
    }

}
