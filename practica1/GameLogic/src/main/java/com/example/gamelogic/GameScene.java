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

    private MapGrid cells;

    private ArrayList<Enemy> enemies;

    private ArrayList<Tower> towers;
    private float cooldown = 1.0f;

    private float timer = 0.0f;

    private Button tower1;
    private IImage tower1img;
    private Button tower2;
    private IImage tower2img;
    private Button tower3;
    private IImage tower3img;

    public GameScene(IEngine iEngine){
        this.iEngine = iEngine;
        this.iGraphics = this.iEngine.getGraphics();

        loadAssets();

        this.exitButton = new Button(this.iGraphics, this.exitImage, 25, 25, 50, 50);

        this.cells = new MapGrid(8, 15, 50, 50);
        createCells();
        this.enemies = new ArrayList<Enemy>();

        this.tower1 = new Button(this.iGraphics, this.tower1img, 600, 550, 50, 50);
        this.tower2= new Button(this.iGraphics, this.tower2img, 660, 550, 50, 50);
        this.tower3 = new Button(this.iGraphics, this.tower3img, 720, 550, 50, 50);

        this.towers = new ArrayList<Tower>();
    }

    private void createCells(){
        int w = 900 / this.cells.getColumns();
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 15; col++){
                boolean path = row == 4 ? true : false;
                Cell cell = new Cell(this.iGraphics, w * col, w * row,w, 0xff000000, row, col, path);
                this.cells.addCell(cell, row, col);

                if (path && this.cells.getStartingPoint() == null){
                    this.cells.setStartingPoint(row, col);
                }
            }
        }
    }

    public void loadAssets(){
        this.tower1img = this.iGraphics.loadImage("sprites/tower1.png");
        this.tower2img = this.iGraphics.loadImage("sprites/tower2.png");
        this.tower3img = this.iGraphics.loadImage("sprites/tower3.png");
    }

    private void addEnemy(){
        enemies.add(new Enemy(this.iGraphics, 40, 15, true, this.cells));
    }

    @Override
    public void render() {
        //this.cell.render();
        for (int row = 0; row < cells.getRows(); row++){
            for (int col = 0; col < cells.getColumns(); col++){
                this.cells.getCell(row, col).render();
            }
        }

        int h = (900/this.cells.getColumns()) * 80;
        this.iGraphics.setColor(0xffBBBBBB);
        iGraphics.fillRectangle(0, 500, 900, 100);

        for (Enemy e : this.enemies){
            e.render();
        }

        for (Tower t : this.towers){
            t.render();
        }

        this.tower1.render();
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
                    if (tower1.imageIsTouched(e.x, e.y)){
                        showAvailableCells(true);
                    }
                    if (e.x > 0 && e.x < 900 && e.y > 0 && e.y < 500){
                        int w = 900 / this.cells.getColumns();
                        System.out.println("e.x: " + e.x + " e.y: " + e.y + " w: " + e.x/w + " h: " + e.y/w);

                        int row = (int)(e.y / w);
                        int col = (int)(e.x / w);

                        float centerX = col * w + w / 2f;
                        float centerY = row * w + w / 2f;

                        this.towers.add(new Tower(this.iGraphics, centerX, centerY, (e.y)/w, (e.x)/w, 20, 50, 0, this.cells));

                        showAvailableCells(false);

                        this.cells.getCell((e.y)/w, (e.x)/w).setTower(true);
                    }
            }
        }
    }

    private void showAvailableCells(boolean available){
        for (int i = 0; i < this.cells.getRows(); i++){
            for (int j = 0; j < this.cells.getColumns(); j++){
                if (!this.cells.getCell(i, j).getPath() && !this.cells.getCell(i, j).getTower()){
                    this.cells.getCell(i, j).setAvailable(available);
                }
            }
        }
    }
}
