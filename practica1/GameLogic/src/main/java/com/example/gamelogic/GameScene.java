package com.example.gamelogic;

import com.example.engine.IAudio;
import com.example.engine.IEngine;
import com.example.engine.IGraphics;
import com.example.engine.IImage;
import com.example.engine.IInput;
import com.example.engine.IScene;
import com.example.engine.ISound;

import java.util.ArrayList;
import java.util.List;

public class GameScene implements IScene {

    private IEngine iEngine;
    private IGraphics iGraphics;

    private IAudio iAudio;

    //private Cell cell;

    private MapGrid mapGrid;
    private boolean grid = false;

    private boolean upgrades = false;

    private ArrayList<Enemy> enemies;

    private ArrayList<Tower> towers;
    private float cooldown = 1.0f;

    private float timer = 0.0f;

    private TowerType type = null;
    private TowerButton tower1;
    private TowerButton tower2;
    private TowerButton tower3;
    private IImage tower1img;
    private IImage tower2img;
    private IImage tower3img;

    private UpgradeButton sword;
    private IImage sword_img;
    private UpgradeButton bow;
    private IImage bow_img;
    private UpgradeButton clock;
    private IImage clock_img;

    private ISound click;


    public GameScene(IEngine iEngine){
        this.iEngine = iEngine;
        this.iGraphics = this.iEngine.getGraphics();
        this.iAudio = this.iEngine.getAudio();

        loadAssets();

        Maps map1 = Maps.level1();
        this.mapGrid = new MapGrid(map1, 600, 320, iGraphics);
        //createCells();
        this.enemies = new ArrayList<Enemy>();

        var fontButton = iGraphics.createFont("fonts/fff.ttf", 10, false, false);
        this.tower1 = new TowerButton(this.iGraphics, fontButton, 445, 360, 50, 50, 100, TowerType.Rayo, 0xFFFFFFFF, 0xFF000000);
        this.tower2 = new TowerButton(this.iGraphics, fontButton, 505, 360, 50, 50, 150, TowerType.Hielo, 0xFFFFFFFF, 0xFF000000);
        this.tower3 = new TowerButton(this.iGraphics, fontButton, 565, 360, 50, 50, 200, TowerType.Fuego, 0xFFFFFFFF, 0xFF000000);

        this.sword = new UpgradeButton(this.iGraphics, fontButton, this.sword_img, 445, 360, 50, 50, 75, 0xFFFFFFFF, 0xFF000000);
        this.bow = new UpgradeButton(this.iGraphics, fontButton, this.bow_img, 505, 360, 50, 50, 75, 0xFFFFFFFF, 0xFF000000);
        this.clock = new UpgradeButton(this.iGraphics, fontButton, this.clock_img, 565, 360, 50, 50, 100, 0xFFFFFFFF, 0xFF000000);
        this.towers = new ArrayList<Tower>();
    }

    public void loadAssets(){
        this.tower1img = this.iGraphics.loadImage("sprites/tower1.png");
        this.tower2img = this.iGraphics.loadImage("sprites/tower1.png");
        this.tower3img = this.iGraphics.loadImage("sprites/tower1.png");

        this.bow_img = this.iGraphics.loadImage("sprites/bow.png");
        this.sword_img = this.iGraphics.loadImage("sprites/sword.png");
        this.clock_img = this.iGraphics.loadImage("sprites/clock.png");

        click = this.iAudio.newSound("music/click.wav");
    }

    private void addEnemy(){
        enemies.add(new Enemy(this.iGraphics, this.iAudio, 40, 5, true, this.mapGrid));
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

        for(Tower t : this.towers){
            t.render();
        }
    }

    @Override
    public void update(float deltaTime) {

        timer += deltaTime;
        if (timer > cooldown){
            addEnemy();
            cooldown += 1.0f;
        }

        for (Tower tower : towers) {
            tower.update(deltaTime, enemies);
        }

        enemies.removeIf(e -> !e.isActive());

        for (Enemy e : enemies) {
            e.update(deltaTime);
        }
    }

    @Override
    public void handleInput(List<IInput.TouchEvent> events) {
        for (IInput.TouchEvent e : events){
            switch(e.type){
                case TOUCH_UP:
                    upgrades = false;
                    for (Tower tower : towers) {
                        tower.setSelected(false);
                        if (tower.isTouched(e.x, e.y)) {
                            tower.setSelected(true);
                            upgrades = true;
                        }
                    }

                    if (!upgrades){
                        if (type != null) {
                            Tower tower = mapGrid.placeTowerAt(e.x, e.y, type, iGraphics, this.iAudio);

                            if (tower != null) {
                                towers.add(tower); // lista global de torres en tu juego
                            }
                            this.iAudio.playSound(this.click, false);
                            mapGrid.showAvailableCells(false);
                            type = null;
                        }
                        else{
                            if (tower1.isTouched(e.x, e.y)){
                                mapGrid.showAvailableCells(true);
                                type = tower1.getTipo();
                            }
                            else if (tower2.isTouched(e.x, e.y)){
                                mapGrid.showAvailableCells(true);
                                type = tower2.getTipo();
                            }
                            else if (tower3.isTouched(e.x, e.y)){
                                mapGrid.showAvailableCells(true);
                                type = tower3.getTipo();
                            }
                        }
                    }
                    else {
                        if (sword.isTouched(e.x, e.y)){
                            //rr
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
}
