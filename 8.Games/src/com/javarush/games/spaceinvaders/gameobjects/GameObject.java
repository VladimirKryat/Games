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
}
