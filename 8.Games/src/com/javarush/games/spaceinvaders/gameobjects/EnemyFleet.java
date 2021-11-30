package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.ShapeMatrix;

import java.util.ArrayList;
import java.util.List;

public class EnemyFleet {
    //кол-во рядов кораблей
    private static final int ROWS_COUNT = 3;
    //кол-во кораблей в ряду
    private static final int COLUMNS_COUNT = 10;
    //расстояние между левыми верхними углами соседних кораблей
    private static final int STEP = ShapeMatrix.ENEMY.length+1;
    //список кораблей
    private List<EnemyShip> ships;

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
    }
    public void draw(Game game){
        ships.forEach(enemyShip -> enemyShip.draw(game));
    }

}
