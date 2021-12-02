package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;

public class Boss extends EnemyShip{
    private int frameCount=0;
    public Boss(double x, double y) {
        super(x, y);
        setAnimatedView(true,
                ShapeMatrix.BOSS_ANIMATION_FIRST,
                ShapeMatrix.BOSS_ANIMATION_SECOND);
    }

    @Override
    public void kill() {
        if (!isAlive) return;
        isAlive=false;
        setAnimatedView(false,
                ShapeMatrix.KILL_BOSS_ANIMATION_FIRST,
                ShapeMatrix.KILL_BOSS_ANIMATION_SECOND,
                ShapeMatrix.KILL_BOSS_ANIMATION_THIRD);
    }

    //смена отображения раз в 10 ходов
    //но если босс неживой, то смена должна быть каждый ход
    @Override
    public void nextFrame() {
        frameCount++;
        if (frameCount % 10 == 0 || !isAlive) {
            super.nextFrame();
        }
    }

    //бос стреляет с разных пушек в зависимости от матрицы
    @Override
    public Bullet fire() {
        if (!isAlive) return null;
        if (matrix==ShapeMatrix.BOSS_ANIMATION_FIRST){
            return new Bullet(x+6, y=y+height, Direction.DOWN);
        }
        else{
            return new Bullet(x,y+height,Direction.DOWN);
        }
    }


}
