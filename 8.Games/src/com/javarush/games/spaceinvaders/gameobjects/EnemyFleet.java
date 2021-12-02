package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.*;

public class EnemyFleet {
    //кол-во рядов кораблей
    private static final int ROWS_COUNT = 3;
    //кол-во кораблей в ряду
    private static final int COLUMNS_COUNT = 10;
    //расстояние между левыми верхними углами соседних кораблей
    private static final int STEP = ShapeMatrix.ENEMY.length+1;
    //список кораблей
    private List<EnemyShip> ships;
    private Direction direction = Direction.RIGHT;


    public EnemyFleet(){
        createShips();
    }
    //метод для создания списка кораблей
    private void createShips(){
        ships= new ArrayList<>(ROWS_COUNT*COLUMNS_COUNT);
        for (int y=0; y<ROWS_COUNT;y++)
            for (int x=0; x<COLUMNS_COUNT; x++){
                //12 - начальный отступ от поля первой линии флота
                // в данной реализации корабли будут создаваться верно пока матрица корабля квадратная,
                // либо высота больше ширины, иначе они будут наезжать друг на друге
                //также не учитывается сколько кораблей может вместиться в ряд
                EnemyShip enemyShip = new EnemyShip(x*STEP,y*STEP+12);
                ships.add(enemyShip);
            }
        ships.add(new Boss(
                //босс генерируется в середине вражесского флота
                STEP*COLUMNS_COUNT/2-ShapeMatrix.BOSS_ANIMATION_FIRST.length/2-1,
                5
        ));
    }
    public void draw(Game game){
        ships.forEach(enemyShip -> enemyShip.draw(game));
    }

    //координата самого "левого" корабля
    private double getLeftBorder(){
        return ships.stream().min(Comparator.comparingDouble(ship -> ship.x)).get().x;
    }
    //x+width самого правого корабля
    private double getRightBorder(){
        EnemyShip rightShip = ships.stream().max(Comparator.comparingDouble(ship -> ship.x + ship.width)).get();
        return rightShip.x+(double)rightShip.width;
    }
    //идея в том чтобы скорость движения увеличивалась при уменьшении количества врагов
    private double getSpeed(){
        double result = ships.size()==0? 1:ships.size();
        return Double.min(2.0,3.0/ships.size());
    }

    public void move(){
//        если кораблей нет ничего не делаем
        if (ships.size()==0) return;
//        если достигли края арены, меняем направление и спускаемся вниз
        double speed = getSpeed();
        boolean flageDown = false;
        if (direction==Direction.LEFT && getLeftBorder()<0){
            direction=Direction.RIGHT;
            flageDown=true;
        }
        else {
            if (direction == Direction.RIGHT && getRightBorder() > SpaceInvadersGame.WIDTH) {
                direction = Direction.LEFT;
                flageDown = true;
            }
        }
        if (flageDown) ships.forEach(ship->ship.move(Direction.DOWN,speed));
        else ships.forEach(ship->ship.move(direction,speed));
    }

    //выстрел с вероятность COMPLEXITY %
    public Bullet fire(Game game){
        if (ships.size()==0) return null;
        //получем число рандомом [0..100/COMPLEXITY], которое == 0  с вероятностью 100/COMPLEXITY
        int probability = game.getRandomNumber(100/SpaceInvadersGame.COMPLEXITY);
        //если 0 то не стреляем
        if (probability>0) return null;
        //выбираем рандомный корабль и стреляем
        int numberRandomShip = game.getRandomNumber(ships.size());
        return ships.get(numberRandomShip).fire();
    }
    public void deleteHiddenShips(){
        ships.removeIf(enemyShip -> !enemyShip.isVisible());
    }
    //проверка попадания пуль и kill их в случае истины

    public int verifyHit(List<Bullet>bullets){
        if (bullets==null||bullets.isEmpty()) return 0;
        int result = 0;
        for(EnemyShip ship:ships){
            if (!ship.isAlive) continue;
            for (Bullet bullet : bullets) {
                if (bullet.isAlive && ship.isCollision(bullet)) {
                    bullet.kill();
                    ship.kill();
                    result+=ship.score;
                }
            }
        }
        return result;
    }
    
    public double getBottomBorder(){
        if (ships.isEmpty()) return 0.0;
        EnemyShip lowestShip = ships.stream().max(Comparator.comparingDouble(ship -> ship.y + ship.height)).orElse(null);
        return lowestShip.y+lowestShip.height;
    }

    public int getShipsCount(){
        return ships.size();
    }
}
