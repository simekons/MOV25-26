package com.example.practica2;

import com.example.androidengine.AndroidEngine;
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

/*
 * GameScene implementa las funcionalidades del juego principal.
 */
public class GameScene implements IScene {

    // Engine.
    private AndroidEngine iEngine;

    // Graphics.
    private IGraphics iGraphics;

    // Audio.
    private IAudio iAudio;

    // Mapa de juego.
    private MapGrid mapGrid;

    // ¿Está por mejorar? (sí/no)
    private boolean upgrades = false;

    // Array de enemigos.
    private ArrayList<Enemy> enemies;

    // Array de torres.
    private ArrayList<Tower> towers;

    // Torre activa.
    private Tower activeTower;

    // Cooldown de aparición de enemigos.
    private float cooldown = 1.0f;

    // Temporizador para las oleadas.
    private float timer = 0.0f;

    // Enemigos spawneados.
    private int enemiesSpawned = 0;

    // Enemigos por oleada.
    private int enemiesPerWave = 5;

    // Cooldown de oleada.
    private float waveCooldown = 3.0f;

    // Espera entre oleadas.
    private float waveRestTimer = 0;

    // ¿Se está spawneando una oleada? (sí/no)
    private boolean spawningWave = true;

    // Dinero actual.
    private int money;

    // Vidas actuales.
    private int lives = 10;

    // Tipo de torre.
    private TowerType type = null;

    // Torres.
    private TowerButton tower1, tower2, tower3;

    // Imágenes de dinero/vida.
    private IImage coinimg;
    private IImage heartimg;

    // Botones de mejoras.
    private UpgradeButton sword, bow, clock;
    private IImage sword_img, bow_img, clock_img;

    // Sonidos.
    private ISound turret, upgrade;

    // Fuente.
    private IFont moneyText;

    // Dificultad.
    private int difficulty;

    // Oleada.
    private int wave = 0;

    // CONSTRUCTORA
    public GameScene(int difficulty) {
        this.iEngine = AndroidEngine.get_instance();
        this.iGraphics = this.iEngine.getGraphics();
        this.iAudio = this.iEngine.getAudio();
        this.iEngine.getAds().setBannerVisible(false);
        this.difficulty = difficulty;
        this.money = 150;
        loadAssets();

        Maps map1 = Maps.level1();
        this.mapGrid = new MapGrid(map1, 600, 320, iGraphics);
        this.enemies = new ArrayList<Enemy>();

        IFont fontButton = iGraphics.createFont("fonts/fff.ttf", 10, false, false);
        this.tower1 = new TowerButton(this.iGraphics, fontButton, 445, 360, 50, 50, 100, TowerType.Rayo, 0xFFFFFFFF, 0xFF000000);
        this.tower2 = new TowerButton(this.iGraphics, fontButton, 505, 360, 50, 50, 150, TowerType.Hielo, 0xFFFFFFFF, 0xFF000000);
        this.tower3 = new TowerButton(this.iGraphics, fontButton, 565, 360, 50, 50, 200, TowerType.Fuego, 0xFFFFFFFF, 0xFF000000);

        this.sword = new UpgradeButton(this.iGraphics, fontButton, this.sword_img, 445, 360, 50, 50, 75, 0xFFFFFFFF, 0xFF000000);
        this.bow = new UpgradeButton(this.iGraphics, fontButton, this.bow_img, 505, 360, 50, 50, 75, 0xFFFFFFFF, 0xFF000000);
        this.clock = new UpgradeButton(this.iGraphics, fontButton, this.clock_img, 565, 360, 50, 50, 100, 0xFFFFFFFF, 0xFF000000);
        this.towers = new ArrayList<Tower>();
    }

    // Carga de recursos.
    public void loadAssets() {
        this.bow_img = this.iGraphics.loadImage("sprites/bow.png");
        this.sword_img = this.iGraphics.loadImage("sprites/sword.png");
        this.clock_img = this.iGraphics.loadImage("sprites/clock.png");

        this.coinimg = this.iGraphics.loadImage("sprites/coin.png");
        this.heartimg = this.iGraphics.loadImage("sprites/heart.png");

        this.turret = this.iAudio.newSound("music/turret.wav");

        this.upgrade = this.iAudio.newSound("music/upgrade.wav");

        moneyText = iGraphics.createFont("fonts/fff.ttf", 15, false, false);
    }

    // RENDERIZADO
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

        iGraphics.setColor(0xFF00FF00);
        iGraphics.drawText(moneyText, "Oleada " + (this.wave + 1), 525, 25);
    }

    // UPDATE
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
            if (enemies.isEmpty()) { // Si no quedan enemigos vivos.
                waveRestTimer += deltaTime; // Temporizador entre oleadas.

                if (waveRestTimer > waveCooldown) {
                    waveRestTimer = 0;
                    this.wave++;
                    waves(); // iniciar siguiente oleada.
                }
            } else {
                // Resetear el temporizador si todavía quedan enemigos.
                waveRestTimer = 0;
            }
        }

        // Actualizar los enemigos.
        for (Enemy e : enemies) {
            e.update(deltaTime);
        }

        // Actualizar las torres.
        for (Tower tower : towers) {
            tower.update(deltaTime, enemies);
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            if (!e.isActive()) {
                if (!e.reachedEnd())
                    money += 50;
                else
                    lives -= 1;
                it.remove();
            }
        }

        enemies.removeIf(e -> !e.isActive());

        if (lives == 0){
            iEngine.setScenes(new FinalScene(this.difficulty, false));
        }
    }

    // INPUT
    @Override
    public void handleInput(List<IInput.TouchEvent> events) {
        for (IInput.TouchEvent e : events) {
            if (e.type == IInput.TouchEvent.TouchEventType.TOUCH_UP) {
                if (handleTowerSelection(e)) return;

                if (!upgrades) {
                    if (handleTowerPlacement(e)) return;
                    handleTowerTypeSelection(e);
                } else {
                    handleUpgrades(e);
                }

                if (!upgrades) deselectAllTowers();
            }
        }
    }

    // Añadir enemigos.
    private void addEnemy(int wave) {
        int vida = 30 + (wave * 15);
        int defensa = 0 + (wave * 2);
        int speed = 20;
        EnemyResist resist = EnemyResist.Nada;

        if (this.wave >= 2) {
            // Resistencias.
            Random n = new Random();
            int valor = n.nextInt(3);
            resist = EnemyResist.values()[valor];

            // Velocidad.
            if (valor == 2){
                speed += 10;
            }
        }

        enemies.add(new Enemy(this.iGraphics, this.iAudio, speed, 5, true, this.mapGrid, vida, defensa, resist));
    }

    // Gestor de oleadas.
    private void waves() {

        switch (this.difficulty) {
            case 0:
                if (this.wave < 3) {
                    enemiesPerWave = 5 + this.wave;
                    waveCooldown = 5.0f;
                } else {
                    this.iEngine.setScenes(new FinalScene(this.difficulty, true));
                }
                break;
            case 1:
                if (this.wave < 7) {
                    enemiesPerWave = 7 + this.wave;
                    waveCooldown = 4.0f;
                } else {
                    this.iEngine.setScenes(new FinalScene(this.difficulty, true));
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

    }

    // Gestor para seleccionar una torre.
    private boolean handleTowerSelection(IInput.TouchEvent e) {
        for (Tower tower : towers) {
            if (tower.isTouched(e.x, e.y)) {
                if(activeTower != null)
                    activeTower.setSelected(false);
                activeTower = tower;
                tower.setSelected(true);

                List<Integer> active = tower.getActiveUpgrades();
                sword.setActive(!active.contains(0));
                bow.setActive(!active.contains(1));
                clock.setActive(!active.contains(2));

                upgrades = true;
                return true;
            }
        }
        return false;
    }

    // Gestor para posicionar la torre (tras clicar botón).
    private boolean handleTowerPlacement(IInput.TouchEvent e) {
        if (type == null) return false;


        Tower tower = mapGrid.placeTowerAt(e.x, e.y, type, iGraphics, iAudio);
        if (tower != null) {
            switch (type){
                case Rayo:
                    money -= tower1.getCost();
                    break;
                case Hielo:
                    money -= tower2.getCost();
                    break;
                case Fuego:
                    money -= tower3.getCost();
                    break;
            }
            iAudio.playSound(turret, false);
            towers.add(tower);
        }
        mapGrid.showAvailableCells(false);
        type = null;

        tower1.setSelected(false);
        tower2.setSelected(false);
        tower3.setSelected(false);

        return true;
    }

    // Gestor para clicar un botón de torre.
    private void handleTowerTypeSelection(IInput.TouchEvent e) {
        if (tower1.isTouched(e.x, e.y)) {
            selectTowerType(tower1);
        } else if (tower2.isTouched(e.x, e.y)) {
            selectTowerType(tower2);
        } else if (tower3.isTouched(e.x, e.y)) {
            selectTowerType(tower3);
        }
    }

    // Activar botón de torre (si hay dinero).
    private void selectTowerType(TowerButton towerBtn) {
        if(money >= towerBtn.getCost()){
            towerBtn.setSelected(true);
            mapGrid.showAvailableCells(true);
            type = towerBtn.getTipo();
        }
    }

    // Gestor de mejoras.
    private void handleUpgrades(IInput.TouchEvent e) {
        if (sword.isTouched(e.x, e.y)) {
            if(canAffordUpgrade(sword)){
                this.iAudio.playSound(this.upgrade, false);
                applyUpgrade(0, sword);
            }
        } else if (bow.isTouched(e.x, e.y)) {
            if(canAffordUpgrade(bow)){
                this.iAudio.playSound(this.upgrade, false);
                applyUpgrade(1, bow);
            }
        } else if (clock.isTouched(e.x, e.y)) {
            if(canAffordUpgrade(clock)){
                this.iAudio.playSound(this.upgrade, false);
                applyUpgrade(2, clock);
            }
        } else {
            upgrades = false;
        }
    }

    // ¿Puede comprar una mejora? (sí/no)
    private boolean canAffordUpgrade(UpgradeButton upgradeBtn) {
        return money >= upgradeBtn.getCost();
    }

    // Aplicar mejora.
    private void applyUpgrade(int upgradeId, UpgradeButton upgradeBtn) {
        activeTower.activateUpgrade(upgradeId);
        money -= upgradeBtn.getCost();
        upgrades = false;
    }

    // Deselector de torres.
    private void deselectAllTowers() {
        for (Tower tower : towers) {
            tower.setSelected(false);
        }
    }
}