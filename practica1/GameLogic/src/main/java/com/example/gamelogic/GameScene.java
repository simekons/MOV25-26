package com.example.gamelogic;

import com.example.engine.IEngine;
import com.example.engine.IGraphics;
import com.example.engine.IImage;
import com.example.engine.IInput;
import com.example.engine.IScene;

import java.util.ArrayList;
import java.util.List;

public class GameScene implements IScene {

    private IEngine iEngine;
    private IGraphics iGraphics;

    //private Cell cell;

    private MapGrid mapGrid;
    private boolean grid = false;

    private boolean upgrades = false;

    private ArrayList<Enemy> enemies;

    private ArrayList<Tower> towers;
    private float cooldown = 1.0f;

    private float timer = 0.0f;

    private int type = -1;
    private CostButton tower1;
    private CostButton tower2;
    private CostButton tower3;
    private IImage tower1img;
    private IImage tower2img;
    private IImage tower3img;

    private CostButton sword;
    private IImage sword_img;
    private CostButton bow;
    private IImage bow_img;
    private CostButton clock;
    private IImage clock_img;

    public GameScene(IEngine iEngine){
        this.iEngine = iEngine;
        this.iGraphics = this.iEngine.getGraphics();

        loadAssets();

        Maps map1 = Maps.level1();
        this.mapGrid = new MapGrid(map1, 600, 320, iGraphics);
        //createCells();
        this.enemies = new ArrayList<Enemy>();

        var fontButton = iGraphics.createFont("fonts/fff.ttf", 10, false, false);
        this.tower1 = new CostButton(this.iGraphics, fontButton, this.tower1img, 445, 360, 50, 50, 100, 0xFFFFFFFF, 0xFF000000);
        this.tower2 = new CostButton(this.iGraphics, fontButton, this.tower1img, 505, 360, 50, 50, 150, 0xFFFFFFFF, 0xFF000000);
        this.tower3 = new CostButton(this.iGraphics, fontButton, this.tower1img, 565, 360, 50, 50, 200, 0xFFFFFFFF, 0xFF000000);
        //this.tower2= new Button(this.iGraphics, this.tower2img, 660, 550, 50, 50);
        //this.tower3 = new Button(this.iGraphics, this.tower3img, 720, 550, 50, 50);

        this.sword = new CostButton(this.iGraphics, fontButton, this.sword_img, 445, 360, 50, 50, 75, 0xFFFFFFFF, 0xFF000000);
        this.bow = new CostButton(this.iGraphics, fontButton, this.bow_img, 505, 360, 50, 50, 75, 0xFFFFFFFF, 0xFF000000);
        this.clock = new CostButton(this.iGraphics, fontButton, this.clock_img, 565, 360, 50, 50, 100, 0xFFFFFFFF, 0xFF000000);
        this.towers = new ArrayList<Tower>();
    }

    public void loadAssets(){
        this.tower1img = this.iGraphics.loadImage("sprites/tower1.png");
        this.tower2img = this.iGraphics.loadImage("sprites/tower1.png");
        this.tower3img = this.iGraphics.loadImage("sprites/tower1.png");

        this.bow_img = this.iGraphics.loadImage("sprites/bow.png");
        this.sword_img = this.iGraphics.loadImage("sprites/sword.png");
        this.clock_img = this.iGraphics.loadImage("sprites/clock.png");
    }

    private void addEnemy(){
        enemies.add(new Enemy(this.iGraphics, 40, 5, true, this.mapGrid));
    }

    @Override
    public void render() {
        // Banda de abajo
        iGraphics.setColor(0xFF808080);
        iGraphics.fillRectangle(0, 320,600,80);

        mapGrid.render();

        if (upgrades) {
            this.sword.render();
            this.bow.render();
            this.clock.render();
        }
        else{
            this.tower1.render();
            this.tower2.render();
            this.tower3.render();
        }

        for (Enemy e : this.enemies){
            e.render();
        }
    }

    @Override
    public void update(float deltaTime) {

        timer += deltaTime;
        if (timer > cooldown){
            addEnemy();
            cooldown += 1.0f;
        }

        for (Enemy e : this.enemies){
            e.movement(deltaTime);
        }
    }

    @Override
    public void handleInput(List<IInput.TouchEvent> events) {
        for (IInput.TouchEvent e : events){
            switch(e.type){
                case TOUCH_UP:
                    if (!upgrades){
                        if (tower1.isTouched(e.x, e.y)){
                            mapGrid.showAvailableCells(true);
                            type = 0;
                        }
                        if (tower2.isTouched(e.x, e.y)){
                            mapGrid.showAvailableCells(true);
                            type = 1;
                        }
                        if (tower3.isTouched(e.x, e.y)){
                            mapGrid.showAvailableCells(true);
                            type = 2;
                        }
                    }
                    else {
                        if (sword.isTouched(e.x, e.y)){
                            //rr
                            upgrades = false;
                        }
                        if (bow.isTouched(e.x, e.y)){
                            //rr
                        }
                        if (clock.isTouched(e.x, e.y)){
                            //r
                        }
                    }
            }
        }
    }

    /*
    private void showAvailableCells(boolean available){
        this.grid = true;
        for (int i = 0; i < this.mapGrid.getRows(); i++){
            for (int j = 0; j < this.mapGrid.getColumns(); j++){
                if (!this.mapGrid.getCell(i, j).getPath() && !this.mapGrid.getCell(i, j).getTower()){
                    this.mapGrid.getCell(i, j).setAvailable(available);
                }
            }
        }
    }*/
}
