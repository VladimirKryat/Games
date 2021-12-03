package com.javarush.games.moonlander;
import com.javarush.engine.cell.*;
public class MoonLanderGame extends Game{
    public static final int WIDTH=64;
    public static final int HEIGHT=64;
    private int score;
    private boolean isGameStopped;
    private boolean isUpPressed;        //флаг нажата ли клавиша
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private Rocket rocket;              // ракета для игры
    private GameObject landscape;      // ландшафт игры
    private GameObject platform;        //место куда нужно посадить корабль
    @Override
    public void initialize(){
        setScreenSize(WIDTH,HEIGHT);    //задаём количество полей
        showGrid(false);    //убираем сетку
        createGame();
    }

    private void createGame(){
        isLeftPressed=false;
        isRightPressed=false;
        isUpPressed=false;
        isGameStopped=false;
        score=1000;
        createGameObjects();        //создаём объекты для игры
        setTurnTimer(50);           //запускаем таймер
        drawScene();
    }   //создание игры, обнуление параметров

    private void drawScene(){
        for(int i=0;i<HEIGHT;i++)
            for (int j=0;j<WIDTH;j++)
                setCellColor(i,j,Color.AQUA);
        landscape.draw(this);
        rocket.draw(this);
    }   //отрисовка поля

    private void createGameObjects(){
        landscape =new GameObject(0,25,ShapeMatrix.LANDSCAPE);
        rocket=new Rocket(WIDTH/2,0);
        platform=new GameObject(23,HEIGHT-1,ShapeMatrix.PLATFORM);
    }       //объявление объектов игры

    private void check(){
        if (rocket.isCollision(platform) && rocket.isStopped()){
            win();
            return;
        }
        if (rocket.isCollision(landscape)) {
            gameOver();
            return;
        }
    }   // проверка пересечения ландщафта и ракеты

    private void win(){
        stopTurnTimer();
        rocket.land();
        isGameStopped=true;
        showMessageDialog(Color.YELLOWGREEN,"YOU WIN!!!",Color.RED,50);
    }

    private void gameOver(){
        stopTurnTimer();
        isGameStopped=true;
        rocket.crash();
        score=0;
        rocket.draw(this);
        showMessageDialog(Color.RED, "GAME OVER", Color.BLUEVIOLET, 50);
    }

    @Override
    public void onTurn(int steps){
        rocket.move(isUpPressed,isLeftPressed,isRightPressed);
        check();
        if (score>0) {score-=1;}
        setScore(score);   //проверка изменения положения ракеты
        drawScene();
    }       //(шаги) действие по таймеру

    @Override
    public void setCellColor(int x,int y,Color color){
        if (x<0||x>=WIDTH||y<0||y>=HEIGHT) return;
        super.setCellColor(x,y,color);
    }   //переопределяем метод чтобы не отрисовывал клетки за пределами поля
    @Override
    public void  onKeyPress(Key key){
        switch (key) {
            case UP:
                isUpPressed = true;
                break;
            case LEFT:
                isLeftPressed = true;
                isRightPressed = false;
                break;
            case RIGHT:
                isRightPressed = true;
                isLeftPressed = false;
                break;
            case SPACE:
                if (isGameStopped) createGame();
                break;
            case ESCAPE:
                stop();
                break;
            default:

                break;
        }
    }   //срабатывает при нажатии клавиши

    @Override
    public void onKeyReleased (Key key){
        switch (key){
            case UP:
                isUpPressed = false;
                break;
            case LEFT:
                isLeftPressed = false;
                break;
            case RIGHT:
                isRightPressed = false;
                break;
            default:
                break;
        }
    }   //срабатывает при  отпускании клавиши
    @Override
    public void stop(){
        if (isGameStopped) Runtime.getRuntime().exit(0);
        isGameStopped=true;
        showMessageDialog(Color.YELLOWGREEN,"Press Esc/Space for Exit/Restart",Color.RED,25);
    }
}
