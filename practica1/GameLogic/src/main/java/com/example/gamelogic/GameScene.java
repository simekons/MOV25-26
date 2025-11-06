package com.example.gamelogic;

import com.example.engine.IAudio;
import com.example.engine.IEngine;
import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IImage;
import com.example.engine.IInput;
import com.example.engine.IScene;
import com.example.engine.ISound;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Random;

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
    private Tower activeTower;
    private float cooldown = 1.0f;

    private float timer = 0.0f;
    private int enemiesSpawned = 0;
    private int enemiesPerWave = 5;
    private float waveCooldown = 3.0f;
    private float waveRestTimer = 0;
    private boolean spawningWave = true;

    private int money = 0;
    private int lives = 10;

    private TowerType type = null;
    private TowerButton tower1;
    private TowerButton tower2;
    private TowerButton tower3;
    private IImage tower1img;
    private IImage tower2img;
    private IImage tower3img;
    private IImage coinimg;
    private IImage heartimg;

    private UpgradeButton sword;
    private IImage sword_img;
    private UpgradeButton bow;
    private IImage bow_img;
    private UpgradeButton clock;
    private IImage clock_img;

    private ISound click;
    private IFont moneyText;

    private int difficulty;

    private int wave = 0;


    public GameScene(IEngine iEngine, int difficulty) {
        this.iEngine = iEngine;
        this.iGraphics = this.iEngine.getGraphics();
        this.iAudio = this.iEngine.getAudio();
        this.difficulty = difficulty;
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

    public void loadAssets() {
        this.tower1img = this.iGraphics.loadImage("sprites/tower1.png");
        this.tower2img = this.iGraphics.loadImage("sprites/tower1.png");
        this.tower3img = this.iGraphics.loadImage("sprites/tower1.png");

        this.bow_img = this.iGraphics.loadImage("sprites/bow.png");
        this.sword_img = this.iGraphics.loadImage("sprites/sword.png");
        this.clock_img = this.iGraphics.loadImage("sprites/clock.png");

        this.coinimg = this.iGraphics.loadImage("sprites/coin.png");
        this.heartimg = this.iGraphics.loadImage("sprites/heart.png");

        click = this.iAudio.newSound("music/click.wav");

        moneyText = iGraphics.createFont("fonts/fff.ttf", 15, false, false);
    }

    private void addEnemy(int wave) {
        int vida = 30 + (wave * 15);
        int defensa = 0 + (wave * 2);

        EnemyResist resist = EnemyResist.Nada;

        if (this.wave >= 2) {
            Random n = new Random();
            int valor = n.nextInt(3);
            resist = EnemyResist.values()[valor];
        }

        enemies.add(new Enemy(this.iGraphics, this.iAudio, 10, 5, true, this.mapGrid, vida, defensa, resist));
    }

    private void waves() {

        switch (this.difficulty) {
            case 0:
                if (this.wave < 3) {
                    enemiesPerWave = 5 + this.wave;
                    waveCooldown = 5.0f;
                } else {
                    // Victory
                }
                break;
            case 1:
                if (this.wave < 7) {
                    enemiesPerWave = 7 + this.wave;
                    waveCooldown = 4.0f;
                } else {
                    // Victory
                }
                break;
            case 2:
                enemiesPerWave = 10 + this.wave;
                waveCooldown = 3.0f;
                break;
        }

        enemiesSpawned = 0;
        spawningWave = true;
        cooldown = 0;
        timer = 0;

        this.wave++;
    }

    @Override
    public void render() {
        // Banda de abajo
        iGraphics.setColor(0xFF808080);
        iGraphics.fillRectangle(0, 320, 600, 80);

        mapGrid.render();

        iGraphics.setColor(0xFF000000);
        iGraphics.drawImage(coinimg, 15, 380, 30, 30);
        iGraphics.drawText(moneyText, String.valueOf(money), 50, 390);
        iGraphics.drawImage(heartimg, 18, 340, 30, 30);
        iGraphics.drawText(moneyText, String.valueOf(lives), 50, 350);

        if (upgrades) {
            this.sword.render();
            this.bow.render();
            this.clock.render();
        } else {
            this.tower1.render();
            this.tower2.render();
            this.tower3.render();
        }

        for (Enemy e : this.enemies) {
            e.render();
        }

        for (Tower t : this.towers) {
            t.render();
        }
    }

    @Override
    public void update(float deltaTime) {

        timer += deltaTime;

        if (spawningWave) {
            if (timer > cooldown && enemiesSpawned < enemiesPerWave) {
                addEnemy(this.wave);
                enemiesSpawned++;
                cooldown += 1.0f;
            }

            if (enemiesSpawned >= enemiesPerWave) {
                spawningWave = false;
                timer = 0;
            }
        } else {
            if (enemies.isEmpty()) { // no quedan enemigos vivos
                waveRestTimer += deltaTime; // pequeño tiempo de descanso entre waves

                if (waveRestTimer > waveCooldown) {
                    waveRestTimer = 0;
                    waves(); // iniciar siguiente wave
                }
            } else {
                // reset del descanso si todavía quedan enemigos
                waveRestTimer = 0;
            }
        }

        for (Enemy e : enemies) {
            e.update(deltaTime);
        }

        for (Tower tower : towers) {
            tower.update(deltaTime, enemies);
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            if (!e.isActive()) {
                if(!e.reachedEnd())
                    money += 10;
                else
                    lives -= 1;
                it.remove();
            }
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
                    for (Tower tower : towers)
                        tower.setSelected(false);
                    for(Tower tower : towers){
                        if (tower.isTouched(e.x, e.y)) {
                            activeTower = tower;
                            tower.setSelected(true);
                            List<Integer> active = tower.getActiveUpgrades();

                            sword.setActive(!active.contains(0));
                            bow.setActive(!active.contains(1));
                            clock.setActive(!active.contains(2));

                            upgrades = true;
                            return;
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

                            tower1.setSelected(false);
                            tower2.setSelected(false);
                            tower3.setSelected(false);
                        }
                        else{
                            if (tower1.isTouched(e.x, e.y)){
                                tower1.setSelected(true);
                                mapGrid.showAvailableCells(true);
                                type = tower1.getTipo();
                            }
                            else if (tower2.isTouched(e.x, e.y)){
                                tower2.setSelected(true);
                                mapGrid.showAvailableCells(true);
                                type = tower2.getTipo();
                            }
                            else if (tower3.isTouched(e.x, e.y)){
                                tower3.setSelected(true);
                                mapGrid.showAvailableCells(true);
                                type = tower3.getTipo();
                            }
                        }
                    }
                    else {
                        if (sword.isTouched(e.x, e.y)){
                            activeTower.activateUpgrade(0);
                        }
                        if (bow.isTouched(e.x, e.y)){
                            activeTower.activateUpgrade(1);
                        }
                        if (clock.isTouched(e.x, e.y)){
                            activeTower.activateUpgrade(2);
                        }
                        upgrades = false;
                    }
            }
        }
    }
}
