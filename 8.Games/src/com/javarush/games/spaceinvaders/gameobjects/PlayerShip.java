package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.Iterator;
import java.util.List;

public class PlayerShip extends Ship{
    private Direction direction=Direction.UP;
    public PlayerShip() {
        super(SpaceInvadersGame.WIDTH/2, SpaceInvadersGame.HEIGHT- ShapeMatrix.PLAYER.length-1);
        setStaticView(ShapeMatrix.PLAYER);
    }
    //проверка на попадание вражеской пули
    public  void verifyHit(List<Bullet> bullets){
        //прерываем проверку если пуль нет или корабль игрока уничтожен
        if (bullets==null||bullets.isEmpty()||!isAlive) return;
        Iterator<Bullet> bulletIterator = bullets.iterator();
        //проход по вражеским пулям с проверкой на коллизию с телом корабля игрока
        while (bulletIterator.hasNext()){
            Bullet bullet = bulletIterator.next();
            if (bullet.isAlive&&isCollision(bullet)) {
                kill();
                bullet.kill();
            }
        }
    }

    //устанавливаем новые фреймы(матрицы) для анимации гибели игрока
    @Override
    public void kill() {
        if (!isAlive) return;
        isAlive=false;
        setAnimatedView(false,
                ShapeMatrix.KILL_PLAYER_ANIMATION_FIRST,
                ShapeMatrix.KILL_PLAYER_ANIMATION_SECOND,
                ShapeMatrix.KILL_PLAYER_ANIMATION_THIRD,
                ShapeMatrix.DEAD_PLAYER);
    }

    //направление Down не изменяет направление движения, т.к. не предусмотрено
    public void setDirection(Direction newDirection) {
        if (newDirection!=Direction.DOWN) {
            this.direction = newDirection;
        }
    }
    public Direction getDirection() {
        return direction;
    }

//    перемещаем корабль согласно направлению движения с проверкой на выход с поля
    public void move(){
        if (!isAlive) return;
        if (direction==Direction.LEFT){
            x--;
        }
        if (direction==Direction.RIGHT){
            x++;
        }
        if (x<0) x=0;
        if (x+width>SpaceInvadersGame.WIDTH){
            x=SpaceInvadersGame.WIDTH-width;
        }
    }

    @Override
    public Bullet fire() {
        if (!isAlive) return null;
        //x для новой пули - должен быть поцентру корабля -> x+2 || x+width/2
        //новая пуля должна быть выше корабля -> y-heightBullet
        return new Bullet(x+2,y-ShapeMatrix.BULLET.length,Direction.UP);
    }
    public void win() {
        setStaticView(ShapeMatrix.WIN_PLAYER);
    }
}
