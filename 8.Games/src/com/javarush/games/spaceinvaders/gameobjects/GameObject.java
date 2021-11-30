package com.javarush.games.spaceinvaders.gameobjects;
//общий класс для всех объектов на игровом поле
public class GameObject {
    public double x;
    public double y;
    public GameObject(double x, double y){
        this.x=x;
        this.y=y;
    }
}
