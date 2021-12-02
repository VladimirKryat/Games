package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.*;


public class Star extends GameObject {
    private final static String STAR_SIGN = "\u2605";
    public Star(double x, double y) {
        super(x, y);
    }
    public void draw(Game game){
        game.setCellValueEx((int)Math.round(x),(int)Math.round(y), Color.NONE,STAR_SIGN,Color.WHITESMOKE,100);
    }
}
