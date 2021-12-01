package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.Iterator;
import java.util.List;

public class PlayerShip extends Ship{
    public PlayerShip() {
        super(SpaceInvadersGame.WIDTH/2, SpaceInvadersGame.HEIGHT- ShapeMatrix.PLAYER.length-1);
        setStaticView(ShapeMatrix.PLAYER);
    }
    //проверка на попадание вражеской пули
    public  void verifyHit(List<Bullet> bullets){
        //прерываем проверку если пуль нет или корабли игрока уничтожен
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

    @Override
    public void kill() {
        if (!isAlive) return;
        isAlive=false;
        setAnimatedView(ShapeMatrix.KILL_PLAYER_ANIMATION_FIRST,
                ShapeMatrix.KILL_PLAYER_ANIMATION_SECOND,
                ShapeMatrix.KILL_PLAYER_ANIMATION_THIRD,
                ShapeMatrix.DEAD_PLAYER);
    }
}
