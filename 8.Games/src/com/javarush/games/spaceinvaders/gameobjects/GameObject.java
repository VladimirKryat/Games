package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.*;

//общий класс для всех объектов на игровом поле
public class GameObject {
    public double x;
    public double y;
    //значение элемента = порядковый номер Color.values()
    public int[][] matrix;
    public int width;
    public int height;
    public GameObject(double x, double y){
        this.x=x;
        this.y=y;
    }
    public void draw(Game game){
        for (int row=0; row<height; row++)
            for (int column=0;column<width; column++){
                game.setCellValueEx((int)Math.round(x)+column, (int)Math.round(y)+row, Color.values()[matrix[row][column]],"");
            }

    }

    //устанавливает форму, ширину и высоту объекта с помощью матрицы
    public void setMatrix(int[][] matrix){
        this.matrix=matrix;
        this.width=matrix[0].length;
        this.height=matrix.length;
    }

    //получаем фактическое(только заполненные элементы матрицы) тело переданного объекта
    // и вызываем проверку с пересечением тела текущего объекта
    public boolean isCollision(GameObject gameObject) {
        //проход по матрице переданного объекта
        for (int gameObjectX = 0; gameObjectX < gameObject.width; gameObjectX++) {
            for (int gameObjectY = 0; gameObjectY < gameObject.height; gameObjectY++) {
                //для элемента который > 0 (т.е содержит тело объекта) проверяем на коллизии с текущим объектом
                if (gameObject.matrix[gameObjectY][gameObjectX] > 0) {
                    if (isCollision(gameObjectX + gameObject.x, gameObjectY + gameObject.y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //проверка пересечения полученных координат с фактическим телом текущего объекта
    private boolean isCollision(double x, double y) {
        //проходим по матрице(телу) текущего объекта
        for (int matrixX = 0; matrixX < width; matrixX++) {
            for (int matrixY = 0; matrixY < height; matrixY++) {
                //проверяем на совпадение с полученными координатами, координаты частей текущего объекта
                if (matrix[matrixY][matrixX] > 0
                        && matrixX + (int) this.x == (int) x
                        && matrixY + (int) this.y == (int) y) {
                    return true;
                }
            }
        }
        return false;
    }
}
