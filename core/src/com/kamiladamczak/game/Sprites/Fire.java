package com.kamiladamczak.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.kamiladamczak.game.Screens.PlayScreen;
import com.kamiladamczak.game.Sprites.Player.Player;

public class Fire extends Sprite {
    private PlayScreen screen;
    private Player player;
    private int power;

    public Fire(PlayScreen screen, Player player, int power) {
        this.screen =screen;
        this.player = player;
        this.power = power;

        System.out.println("new fire from:"+player+" of power: "+power);
    }
}
