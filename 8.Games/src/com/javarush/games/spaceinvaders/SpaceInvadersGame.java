package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.List;

public class SpaceInvadersGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private List<Star> stars;
    @Override
    public void initialize() {
        setScreenSize(WIDTH,HEIGHT);
        createGame();
    }

    private void drawField(){
//        отрисовка поля(фона)
        for (int row=0; row<WIDTH; row++){
            for (int column=0;column<HEIGHT; column++){
                setCellValueEx(column,row,Color.BLACK,"");
            }
        }
//        отрисовка звёзд
        stars.forEach(x->x.draw(this));
    }

    private void createGame(){
        createStars();
        drawScene();
    }


    private void drawScene(){
        drawField();
    }

    private void createStars(){
        stars=new ArrayList<>();
        for (int count=0; count<8;count++){
            //генерируем позицию звезд 00.00::63.00
            double x =Math.round(Math.random()*6300)/100.00;
            double y =Math.round(Math.random()*6300)/100.00;
            Star star = new Star(x,y);
            stars.add(star);
        }
    }

}
