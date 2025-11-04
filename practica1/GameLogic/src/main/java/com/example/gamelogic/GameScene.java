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

    private Button exitButton;
    private IImage exitImage;

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

    private Button sword;
    private IImage sword_img;
    private Button bow;
    private IImage bow_img;
    private Button clock;
    private IImage clock_img;

    public GameScene(IEngine iEngine){
        this.iEngine = iEngine;
        this.iGraphics = this.iEngine.getGraphics();

        loadAssets();

        this.exitButton = new Button(this.iGraphics, this.exitImage, 25, 25, 50, 50);

        this.mapGrid = new MapGrid(8, 15, 600, 320, iGraphics);
        //createCells();
        this.enemies = new ArrayList<Enemy>();

        var fontButton = iGraphics.createFont("fonts/fff.ttf", 5, false, false);
        this.tower1 = new CostButton(this.iGraphics, fontButton, this.tower1img, 445, 360, 50, 50, "100", 0xFFFFFFFF, 0xFF000000);
        this.tower2 = new CostButton(this.iGraphics, fontButton, this.tower1img, 505, 360, 50, 50, "100", 0xFFFFFFFF, 0xFF000000);
        this.tower3 = new CostButton(this.iGraphics, fontButton, this.tower1img, 565, 360, 50, 50, "100", 0xFFFFFFFF, 0xFF000000);
        //this.tower2= new Button(this.iGraphics, this.tower2img, 660, 550, 50, 50);
        //this.tower3 = new Button(this.iGraphics, this.tower3img, 720, 550, 50, 50);

        this.sword = new Button(this.iGraphics, this.sword_img, 600, 550, 75, 75);
        this.bow = new Button(this.iGraphics, this.bow_img, 700, 550, 75, 75);
        this.clock = new Button(this.iGraphics, this.clock_img, 800, 550, 75, 75);
        this.towers = new ArrayList<Tower>();
    }

    /*
    private void createCells(){
        int w = 900 / this.mapGrid.getColumns();
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 15; col++){
                boolean path = row == 4 ? true : false;
                Cell cell = new Cell(this.iGraphics, w * col, w * row,w, 0xff000000, row, col, path);
                this.mapGrid.addCell(cell, row, col);

                if (path && this.mapGrid.getStartingPoint() == null){
                    this.mapGrid.setStartingPoint(row, col);
                }
            }
        }
    }*/

    public void loadAssets(){
        this.tower1img = this.iGraphics.loadImage("sprites/tower1.png");
        this.tower2img = this.iGraphics.loadImage("sprites/tower1.png");
        this.tower3img = this.iGraphics.loadImage("sprites/tower1.png");

        this.bow_img = this.iGraphics.loadImage("sprites/bow.png");
        this.sword_img = this.iGraphics.loadImage("sprites/sword.png");
        this.clock_img = this.iGraphics.loadImage("sprites/clock.png");
    }

    private void addEnemy(){
        enemies.add(new Enemy(this.iGraphics, 40, 15, true, this.mapGrid));
    }

    @Override
    public void render() {
        // Banda de abajo
        iGraphics.setColor(0xFF808080);
        iGraphics.fillRectangle(0, 320,600,80);

        mapGrid.render();
        tower1.render();
        tower2.render();
        tower3.render();

        /*
        //this.cell.render();
        for (int row = 0; row < mapGrid.getRows(); row++){
            for (int col = 0; col < mapGrid.getColumns(); col++){
                this.mapGrid.getCell(row, col).render();
            }
        }

        int h = (900/this.mapGrid.getColumns()) * 80;
        this.iGraphics.setColor(0xffBBBBBB);
        iGraphics.fillRectangle(0, 500, 900, 100);

        for (Enemy e : this.enemies){
            e.render();
        }

        for (Tower t : this.towers){
            t.render();
        }

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

         */
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
                            showAvailableCells(true);
                            type = 0;
                        }
                        if (tower2.isTouched(e.x, e.y)){
                            showAvailableCells(true);
                            type = 1;
                        }
                        if (tower3.isTouched(e.x, e.y)){
                            showAvailableCells(true);
                            type = 2;
                        }
                    }
                    else {
                        if (sword.imageIsTouched(e.x, e.y)){
                            //rr
                            upgrades = false;
                        }
                        if (bow.imageIsTouched(e.x, e.y)){
                            //rr
                        }
                        if (clock.imageIsTouched(e.x, e.y)){
                            //r
                        }
                    }

                    if (e.x > 0 && e.x < 900 && e.y > 0 && e.y < 500){
                        int w = 900 / this.mapGrid.getColumns();
                        System.out.println("e.x: " + e.x + " e.y: " + e.y + " w: " + e.x/w + " h: " + e.y/w);

                        int row = (int)(e.y / w);
                        int col = (int)(e.x / w);

                        if (this.mapGrid.getCell(row, col).getTower()){
                            upgrades = true;
                        }
                        else if (grid && !this.mapGrid.getCell(row, col).getPath()) {
                            float centerX = col * w + w / 2f;
                            float centerY = row * w + w / 2f;

                            this.towers.add(new Tower(this.iGraphics, centerX, centerY, row, col, 30, 50, this.type, this.mapGrid, this.mapGrid.getCell(row, col)));

                            showAvailableCells(false);

                            this.mapGrid.getCell(row, col).setTower(true);
                            this.grid = false;
                            type = -1;
                        }

                    }

            }
        }
    }

    private void showAvailableCells(boolean available){
        this.grid = true;
        for (int i = 0; i < this.mapGrid.getRows(); i++){
            for (int j = 0; j < this.mapGrid.getColumns(); j++){
                if (!this.mapGrid.getCell(i, j).getPath() && !this.mapGrid.getCell(i, j).getTower()){
                    this.mapGrid.getCell(i, j).setAvailable(available);
                }
            }
        }
    }
}
