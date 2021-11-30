package com.javarush.games.moonlander;
import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Rocket extends GameObject {
    private double speedY = 0;
    private double speedX = 0;
    private double boost = 0.05;
    private double slowdown = boost / 10;
    private RocketFire downFire;
    private RocketFire leftFire;
    private RocketFire rightFire;

    public Rocket(double x, double y) {
        super(x, y, ShapeMatrix.ROCKET);
        downFire=new RocketFire(new ArrayList(Arrays.asList(ShapeMatrix.FIRE_DOWN_1,ShapeMatrix.FIRE_DOWN_2,ShapeMatrix.FIRE_DOWN_3)));
        leftFire=new RocketFire(new ArrayList(Arrays.asList(ShapeMatrix.FIRE_SIDE_1,ShapeMatrix.FIRE_SIDE_2)));
        rightFire=new RocketFire(new ArrayList(Arrays.asList(ShapeMatrix.FIRE_SIDE_1,ShapeMatrix.FIRE_SIDE_2)));
    }
    public void move(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed) {
        if (isUpPressed) {
            speedY -= boost;
        } else {
            speedY += boost;
        }
        y += speedY;

        if (isLeftPressed) {
            speedX -= boost;
            x += speedX;
        } else if (isRightPressed) {
            speedX += boost;
            x += speedX;
        } else if (speedX > slowdown) {
            speedX -= slowdown;
        } else if (speedX < -slowdown) {
            speedX += slowdown;
        } else {
            speedX = 0;
        }
        x += speedX;
        checkBorders();
        switchFire(isUpPressed,isLeftPressed,isRightPressed);
    }   //перемещение ракеты
    private void checkBorders() {
        if (x < 0) {
            x = 0;
            speedX = 0;
        } else if (x + width > MoonLanderGame.WIDTH) {
            x = MoonLanderGame.WIDTH - width;
            speedX = 0;
        }
        if (y <= 0) {
            y = 0;
            speedY = 0;
        }
    }   //проверка выхода за границы поля
    public boolean isStopped() {
        return speedY < 10 * boost;
    }   //проверка скорости преземления
    public boolean isCollision(GameObject object) {
        int transparent = Color.NONE.ordinal();     // передаём код - 0 - прозрачного цвета(NONE)
        for (int matrixX = 0; matrixX < width; matrixX++) {
            for (int matrixY = 0; matrixY < height; matrixY++) {
                // далее накладываем матрицы друг на друга относительно их координат на игровом поле
                int objectX = matrixX + (int) x - (int) object.x;
                int objectY = matrixY + (int) y - (int) object.y;

                if (objectX < 0 || objectX >= object.width || objectY < 0 || objectY >= object.height) {
                    continue;   //если часть ракеты выходит за пределы матрицы объекта, то не проверяем её
                }

                if (matrix[matrixY][matrixX] != transparent && object.matrix[objectY][objectX] != transparent) {
                    return true;    // если обе клетки!=0 то имеем столкновение
                }
            }
        }
        return false;
    }   //проверка на пересечение с объектом
    private void switchFire(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed){
        if (isUpPressed){
            downFire.x=x+width/2;
            downFire.y=y+height;
            downFire.show();
        }
        else {downFire.hide();}

        if (isLeftPressed){
            leftFire.x=x+width;
            leftFire.y=y+height;
            leftFire.show();
        }
        else {leftFire.hide();}

        if (isRightPressed){
            rightFire.x=x-ShapeMatrix.FIRE_SIDE_1[0].length;//rightFire.width;
            rightFire.y=y+height;
            rightFire.show();
        }
        else {rightFire.hide();}
    }   //отрисовка огня

    public void land(){
        y-=1;
    }   //ставим ракету на платформу, поднимая на 1
    public void crash(){
        matrix=ShapeMatrix.ROCKET_CRASH;
    }
    @Override
    public void draw(Game game){
        super.draw(game);
        downFire.draw(game);
        leftFire.draw(game);
        rightFire.draw(game);
    }
}
