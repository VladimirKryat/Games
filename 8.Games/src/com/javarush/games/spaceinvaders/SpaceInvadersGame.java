package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.gameobjects.Bullet;
import com.javarush.games.spaceinvaders.gameobjects.EnemyFleet;
import com.javarush.games.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpaceInvadersGame extends Game {
    //размеры поля
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private List<Star> stars;
    //вражеский флот
    private EnemyFleet enemyFleet;
    //сложность игры == вероятность выстрела вражеского корабля
    public static final int COMPLEXITY = 5;
    private List<Bullet> enemyBullets;
    @Override
    public void initialize() {
        setScreenSize(WIDTH,HEIGHT);
        showGrid(true);
        createGame();
    }
//        отрисовка поля
    private void drawField(){
//        отрисовка фона
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
        enemyFleet=new EnemyFleet();
        enemyBullets = new ArrayList<>();
        drawScene();
        setTurnTimer(40);
    }


    private void drawScene(){
        drawField();
        enemyFleet.draw(this);
        enemyBullets.forEach(bullet -> bullet.draw(this));
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
    private void moveSpaceObjects(){
        enemyFleet.move();
        enemyBullets.forEach(Bullet::move);
    }

    @Override
    public void onTurn(int step) {
        moveSpaceObjects();
        Bullet newBullet = enemyFleet.fire(this);
        if (newBullet!=null) enemyBullets.add(newBullet);
        check();
        drawScene();
    }
    //проверка пуль и удаление если они !isAlive или вышли за пределы поля
    private void removeDeadBullets(){
        Iterator<Bullet> iterator = enemyBullets.iterator();
        while (iterator.hasNext()){
            Bullet bullet = iterator.next();
            if (!bullet.isAlive||bullet.y>=HEIGHT-1) {
                iterator.remove();
            }
        }
    }
    //метод для проверки объектов на поле
    private void check(){
        removeDeadBullets();
    }
}
