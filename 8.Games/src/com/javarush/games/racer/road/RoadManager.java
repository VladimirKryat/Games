package com.javarush.games.racer.road;

import com.javarush.games.racer.PlayerCar;
import com.javarush.games.racer.RacerGame;
import com.javarush.engine.cell.Game;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoadManager {
    public static final int LEFT_BORDER =RacerGame.ROADSIDE_WIDTH;
    public static final int RIGHT_BORDER = RacerGame.WIDTH-RacerGame.ROADSIDE_WIDTH;
    private static final int FIRST_LANE_POSITION = 16;       //крайняя левая позиция
    private static final int FOURTH_LANE_POSITION = 44;      //крайняя правая позиция
    private static final int PLAYER_CAR_DISTANCE = 12;
    private int passedCarsCount;
    private List<RoadObject> items = new ArrayList<>();
    private RoadObject createRoadObject(RoadObjectType type, int x, int y){ //создание объекта согласно переданному типу
        if (type.equals(RoadObjectType.THORN)) 
            return new Thorn(x, y);
        else if (type.equals(RoadObjectType.DRUNK_CAR))
            return new MovingCar(x,y);
        else return new Car(type,x,y);
    }
    private void addRoadObject(RoadObjectType type, Game game){     //задаём координаты будущего объекта
        int x = game.getRandomNumber(FIRST_LANE_POSITION,FOURTH_LANE_POSITION);
        int y = -1*RoadObject.getHeight(type);
        RoadObject newObject = createRoadObject(type, x, y);
        if ((newObject !=null)&&(isRoadSpaceFree(newObject))) items.add(newObject);   //добавляем созданный объект в список

    }
    public void draw(Game game){
        for (RoadObject item:items){
            item.draw(game);
        }
    }
    public void move(int boost){
        for (RoadObject item:items){
            item.move(boost+item.speed,items);
        }
        deletePassedItems();
    }
    private boolean isThornExists(){
        return items.stream().anyMatch(item->item.type==RoadObjectType.THORN);      //проверяем наличие шипов в списке объектов
        /*for (RoadObject item:items){
            if (item.type==RoadObjectType.THORN) return true;
        }
        return false;*/
    }
    private boolean isMovingCarExists(){
        return items.stream().anyMatch(item->item.type==RoadObjectType.DRUNK_CAR);      //проверяем наличие "пьяных машин" в списке объектов
    }
    private boolean isRoadSpaceFree (RoadObject object){        //true если между переданным объектом
                                                                // и всеми остальными объектами из items расстояние >= PLAYER_CAR_DISTANCE
        for (RoadObject item:items){
            if (item.isCollisionWithDistance(object,PLAYER_CAR_DISTANCE)) return false;
        }
        return true;
    }
    public void generateNewRoadObjects(Game game){  //генерация объектов
        generateThorn(game);
        generateRegularCar(game);
        generateMovingCar(game);
    }
    private void generateThorn(Game game){   //генерация шипов
        int random = game.getRandomNumber(100);
        if (random<10&&(!isThornExists())) addRoadObject(RoadObjectType.THORN, game);
    }
    private void generateRegularCar(Game game){
        int carTypeNumber = game.getRandomNumber(4);
        if (game.getRandomNumber(100)<30) addRoadObject(RoadObjectType.values()[carTypeNumber],game);
    }
    private  void generateMovingCar(Game game){
        if (game.getRandomNumber(100)<10&&!isMovingCarExists()) addRoadObject(RoadObjectType.DRUNK_CAR,game);
    }
    private void deletePassedItems(){       //проверка и удаление с дороги объектов, которые уже проехал игрок. Подсчёт очков
                                    //items = items.stream().filter(item->item.y<RacerGame.HEIGHT).collect(Collectors.toList());
                                    //items.removeAll(items.stream().filter(item->item.y>=RacerGame.HEIGHT).collect(Collectors.toList()));
        List<RoadObject> itemsCopy = new ArrayList(items);
        for (RoadObject item:itemsCopy){
            if (item.y>= RacerGame.HEIGHT){
                if (!item.type.equals(RoadObjectType.THORN))
                    passedCarsCount++;
                items.remove(item);
            }
        }
    }

    public boolean checkCrush(PlayerCar playerCar){
        for (RoadObject item:items){
            if (item.isCollision(playerCar)) return true;
        }
        return false;
    }


    public int getPassedCarsCount() {
        return passedCarsCount;
    }


}

