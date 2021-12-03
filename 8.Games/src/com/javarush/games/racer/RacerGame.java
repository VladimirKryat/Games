package com.javarush.games.racer;

import com.javarush.engine.cell.*;
import com.javarush.games.racer.road.RoadManager;

public class RacerGame extends Game {
    public static final int WIDTH=64;
    public static final int HEIGHT=64;
    public static final int CENTER_X=WIDTH/2;
    public static final int ROADSIDE_WIDTH=14;
    private int score;
    private static final int RACE_GOAL_CARS_COUNT = 40; //количество авто до появления финишной черты
    private boolean isGameStopped;
    private RoadManager roadManager;
    private RoadMarking roadMarking;
    private FinishLine finishLine;
    private PlayerCar player;
    private ProgressBar progressBar;
    @Override
    public void initialize() {
        setScreenSize(WIDTH,HEIGHT);
        showGrid(false);
        createGame();
    }
    private void createGame(){
        roadManager = new RoadManager();
        roadMarking=new RoadMarking();
        player=new PlayerCar();
        player.speed=1;
        finishLine = new FinishLine();
        progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
        isGameStopped=false;
        score=3500;
        drawScene();
        setTurnTimer(40);
    }
    private void drawScene(){
        drawField();
        roadManager.draw(this);
        roadMarking.draw(this);
        finishLine.draw(this);
        progressBar.draw(this);
        player.draw(this);
    }
    private void drawField(){
        for(int i=0;i<WIDTH;i++){
            for (int j=0;j<HEIGHT;j++){
                if (i<ROADSIDE_WIDTH||i>=WIDTH-ROADSIDE_WIDTH)
                    setCellColor(i,j,Color.GREEN);  //отрисовка обочины
                if ((i!=CENTER_X)&&(i>=ROADSIDE_WIDTH&&i<WIDTH-ROADSIDE_WIDTH))
                    setCellColor(i,j,Color.GRAY);   //отрисовка асфальта
                if (i==CENTER_X)
                    setCellColor(i,j,Color.WHITE);
            }
        }
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x<0||x>=WIDTH||y<0||y>=HEIGHT) return;
        super.setCellColor(x, y, color);
    }
    private void moveAll(){
        roadManager.move(player.speed);
        roadMarking.move(player.speed);
        finishLine.move(player.speed);
        progressBar.move(roadManager.getPassedCarsCount());
        player.move();
    }

    @Override
    public void onTurn(int step) {
        score-=5;
        setScore(score);
        if (roadManager.getPassedCarsCount()>=RACE_GOAL_CARS_COUNT) {
            finishLine.show();  //активируем финишную черту если набраны очки
        }
        if (roadManager.checkCrush(player)) {
            gameOver();
            drawScene();
            return;
        }
        else {
            if (finishLine.isCrossed(player)) {
                win();
                drawScene();
                return;
            }
            moveAll();
            roadManager.generateNewRoadObjects(this);
            drawScene();
        }
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key){
            case RIGHT : player.setDirection(Direction.RIGHT);
            break;
            case LEFT: player.setDirection(Direction.LEFT);
            break;
            case UP: player.speed=2;
            break;
            case SPACE:
                if (isGameStopped) createGame();
                break;
            case ESCAPE:
                stop();
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if ((key.equals(Key.LEFT)&&player.getDirection().equals(Direction.LEFT))
                || (key.equals(Key.RIGHT)&&player.getDirection().equals(Direction.RIGHT)))
                player.setDirection(Direction.NONE);
        if (key.equals(Key.UP)) player.speed=1;
    }
    private void gameOver(){
        isGameStopped=true;
        stopTurnTimer();
        showMessageDialog(Color.BLUE,"GAME OVER",Color.RED,50);
        player.stop();
    }
    private void win(){
        isGameStopped=true;
        showMessageDialog(Color.BLUE,"YOU WIN",Color.GREEN,50);
        stopTurnTimer();
    }

    @Override
    public void stop() {
        if (isGameStopped) Runtime.getRuntime().exit(0);
        isGameStopped=true;
        showMessageDialog(Color.BLUE,"Press Esc/Space for Exit/Restart",Color.RED,25);
    }
}
