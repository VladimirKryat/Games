package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.gameobjects.Bullet;
import com.javarush.games.spaceinvaders.gameobjects.EnemyFleet;
import com.javarush.games.spaceinvaders.gameobjects.PlayerShip;
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
    private PlayerShip playerShip;
    //сложность игры == вероятность выстрела вражеского корабля
    public static final int COMPLEXITY = 5;
    private List<Bullet> enemyBullets;
    private boolean isGameStopped;
    private int animationsCount;
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
        playerShip = new PlayerShip();
        isGameStopped =false;
        animationsCount=0;
        drawScene();
        setTurnTimer(40);
    }


    private void drawScene(){
        drawField();
        enemyFleet.draw(this);
        enemyBullets.forEach(bullet -> bullet.draw(this));
        playerShip.draw(this);
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
        playerShip.move();
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
        //игру завершаем не сразу, чтобы отобразились анимации
        if (!playerShip.isAlive) stopGameWithDelay();
        playerShip.verifyHit(enemyBullets);
        removeDeadBullets();
    }

    //игра не останавливается пока счетчик шагов <10
    private void stopGameWithDelay() {
        animationsCount++;
        if (animationsCount>=10) stopGame(playerShip.isAlive);
    }

    private void stopGame(boolean isWin){
        isGameStopped =true;
        stopTurnTimer();
        if (isWin) showMessageDialog(Color.AQUA, "YOU WIN", Color.GREEN, 50);
        else showMessageDialog(Color.AQUA,"YOU LOSE", Color.RED, 50);
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key){
            case SPACE: if (isGameStopped) {
                isGameStopped=false;
                createGame();
            }
                break;
            case LEFT:playerShip.setDirection(Direction.LEFT);
                break;
            case RIGHT:playerShip.setDirection(Direction.RIGHT);
                break;
        }
    }

    //при отпускании клавиши влево или вправо при совпадении с направлением корабля, устанавливаем Direction.UP для остановки корабля
    @Override
    public void onKeyReleased(Key key) {
        if ((key==Key.LEFT&&playerShip.getDirection()==Direction.LEFT)||
                (key==Key.RIGHT&&playerShip.getDirection()==Direction.RIGHT))
            playerShip.setDirection(Direction.UP);
    }
}
