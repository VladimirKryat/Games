package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ship extends GameObject{

    //список матриц для кадров анимации
    private List<int[][]> frames;
    //текущий кадр анимации
    private int frameIndex;
    public boolean isAlive=true;
    public Ship(double x, double y) {
        super(x, y);
    }
    public void setStaticView(int [][] viewFrame){
        frames=new ArrayList<>();
        frames.add(viewFrame);
        frameIndex=0;
        setMatrix(viewFrame);
    }
    public Bullet fire(){
        return null;
    };
    public void kill(){
        isAlive=false;
    }
    public void setAnimatedView(int[][]... viewFrames){
        setMatrix(viewFrames[0]);
        frameIndex=0;
        frames=Arrays.asList(viewFrames);
    }
//    устанавливаем следущий кадр, если не установлен последний кадр
    public void nextFrame(){
        frameIndex++;
        if (frameIndex >= frames.size()) {
            return;
        }
        matrix = frames.get(frameIndex);

        /*if (frameIndex<=frames.size()-1){
            frameIndex++;
            setMatrix(frames.get(frameIndex));
        }*/
        /*else {
            frameIndex=0;
            setMatrix(frames.get(frameIndex));
        }*/
    }

    @Override
    public void draw(Game game) {
        nextFrame();
        super.draw(game);
    }

    //false - если корабль уничтожен и отработала вся анимация
    public boolean isVisible(){
        return isAlive||frameIndex<frames.size()-1;
    }
}
