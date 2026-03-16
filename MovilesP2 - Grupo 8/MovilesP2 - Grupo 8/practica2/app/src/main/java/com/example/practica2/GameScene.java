package com.example.practica2;

import android.util.Pair;

import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidFile;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidImage;
import com.example.engine.IAudio;
import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IImage;
import com.example.engine.IInput;
import com.example.engine.IScene;
import com.example.engine.ISound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * GameScene implementa la escena de juego.
 */
public class GameScene implements IScene {

    // Engine.
    private AndroidEngine iEngine;

    // Graphics.
    private AndroidGraphics graphics;

    // Audio.
    private AndroidAudio audio;

    private AndroidFile androidFile;

    // Mapa de juego.
    private MapGrid mapGrid;

    private static int level;


    // ¿Está por mejorar? (sí/no)
    private boolean upgrades = false;

    // ¿Es la primera vez que se pasa este nivel?
    private boolean isFirstTime = false;
    // Array de enemigos.
    private ArrayList<Enemy> enemies;

    private AndroidImage goblin;
    private AndroidImage orc;

    // Array de torres.
    private ArrayList<Tower> towers;
    private IImage imgRayo = null;
    private IImage imgFuego = null;
    private IImage imgHielo = null;

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
    private int cost = 0;

    // Torres.
    private List<TowerButton> towerButtons;

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

    private GameLoader gameLoader;

    // Dificultad.
    private int difficulty;

    // Oleada.
    private int wave = 0;

    private LevelData levelData;

    /**
     * CONSTRUCTORA.
     * @param levelData
     * @param gameLoader
     */
    public GameScene(LevelData levelData, GameLoader gameLoader){

        completeLevel();
        AndroidEngine.get_instance().setScenes(new FinalScene(gameLoader, this.difficulty, true, this.isFirstTime));


        init(gameLoader);

        // Se instancia el levelData
        this.levelData = levelData;
        if (this.levelData != null){
            this.level = this.levelData.getLevel();
        }
        Maps map = new Maps(this.levelData);
        this.mapGrid = new MapGrid(map, 600, 320, graphics);
    }

    /**
     * CONSTRUCTORA (modo libre).
     * @param gameLoader
     * @param difficulty
     */
    public GameScene(GameLoader gameLoader, int difficulty) {

        init(gameLoader);

        this.difficulty = difficulty;

        Maps map = Maps.level1();
        this.mapGrid = new MapGrid(map, 600, 320, graphics);


    }

    /**
     * Método que inicializa las variables.
     * @param gameLoader
     */
    private void init(GameLoader gameLoader){
        this.iEngine = AndroidEngine.get_instance();
        this.graphics = this.iEngine.getGraphics();
        this.audio = this.iEngine.getAudio();
        this.androidFile = this.iEngine.getFile();
        this.gameLoader = gameLoader;

        this.iEngine.getAds().setBannerVisible(false);
        this.money = 500;

        loadAssets();

        this.enemies = new ArrayList<>();

        IFont fontButton = graphics.createFont("fonts/fff.ttf", 10, false, false);

        PlayerShopState state = gameLoader.getPlayerShopState();

        towerButtons = new ArrayList<>();

        Map<String, ShopItemData> skins = new HashMap<>();

        for (ShopItemData item : gameLoader.getShopManager().getSkinItems()) {
            skins.put(item.getId(), item);
        }

        if (state.isPurchased("skinRayo") && skins.get("skinRayo") != null) {
            imgRayo = graphics.loadImage(skins.get("skinRayo").getImagePath());
        }

        if (state.isPurchased("skinFuego") && skins.get("skinFuego") != null) {
            imgFuego = graphics.loadImage(skins.get("skinFuego").getImagePath());
        }

        if (state.isPurchased("skinHielo") && skins.get("skinHielo") != null) {
            imgHielo = graphics.loadImage(skins.get("skinHielo").getImagePath());
        }

        towerButtons.add(new TowerButton(graphics, fontButton, 445, 360, 50, 50, 100, TowerType.Rayo, 0xFFFFFFFF, 0xFF000000, imgRayo));
        towerButtons.add(new TowerButton(graphics, fontButton, 505, 360, 50, 50, 150, TowerType.Hielo, 0xFFFFFFFF, 0xFF000000, imgHielo));
        towerButtons.add(new TowerButton(graphics, fontButton, 565, 360, 50, 50, 200, TowerType.Fuego, 0xFFFFFFFF, 0xFF000000, imgFuego));

        int x = 385;

        if(state.isPurchased("towerStar")){
            towerButtons.add(new TowerButton(graphics, fontButton, x, 360, 50, 50, 250, TowerType.Star, 0xFFFFFFFF, 0xFF000000, null));
            x -= 60;
        }
        if(state.isPurchased("towerStun")){
            towerButtons.add(new TowerButton(graphics, fontButton, x, 360, 50, 50, 200, TowerType.Stun, 0xFFFFFFFF, 0xFF000000, null));
            x -= 60;
        }
        if(state.isPurchased("towerPoison")){
            towerButtons.add(new TowerButton(graphics, fontButton, x, 360, 50, 50, 120, TowerType.Poison, 0xFFFFFFFF, 0xFF000000, null));
        }

        this.sword = new UpgradeButton(this.graphics, fontButton, this.sword_img, 445, 360, 50, 50, 75, 0xFFFFFFFF, 0xFF000000);
        this.bow = new UpgradeButton(this.graphics, fontButton, this.bow_img, 505, 360, 50, 50, 75, 0xFFFFFFFF, 0xFF000000);
        this.clock = new UpgradeButton(this.graphics, fontButton, this.clock_img, 565, 360, 50, 50, 100, 0xFFFFFFFF, 0xFF000000);
        this.towers = new ArrayList<Tower>();
    }

    /**
     * Método que carga los recursos necesarios.
     */
    public void loadAssets() {
        this.goblin = (AndroidImage) this.graphics.loadImage("sprites/goblin.png");
        this.orc = (AndroidImage) this.graphics.loadImage("sprites/orc.png");
        this.bow_img = this.graphics.loadImage("sprites/bow.png");
        this.sword_img = this.graphics.loadImage("sprites/sword.png");
        this.clock_img = this.graphics.loadImage("sprites/clock.png");

        this.coinimg = this.graphics.loadImage("sprites/coin.png");
        this.heartimg = this.graphics.loadImage("sprites/heart.png");

        this.turret = this.audio.newSound("music/turret.wav");

        this.upgrade = this.audio.newSound("music/upgrade.wav");

        moneyText = graphics.createFont("fonts/fff.ttf", 15, false, false);
    }

    /**
     * Método de RENDERIZADO.
     */
    @Override
    public void render() {
        // Banda de abajo

        mapGrid.render();

        graphics.setColor(0xFFC0C0C0);
        graphics.fillRectangle(0, 320, 600, 80);

        graphics.setColor(0xFF000000);
        graphics.drawImage(coinimg, 15, 380, 30, 30);
        graphics.drawText(moneyText, String.valueOf(money), 50, 390);
        graphics.drawImage(heartimg, 18, 340, 30, 30);
        graphics.drawText(moneyText, String.valueOf(lives), 50, 350);

        if (upgrades) {
            this.sword.render();
            this.bow.render();
            this.clock.render();
        } else {
            for(TowerButton tb : this.towerButtons){
                tb.render();
            }
        }

        for (Enemy e : this.enemies) {
            e.render();
        }

        for (Tower t : this.towers) {
            t.render();
        }

        graphics.setColor(0xFF000000);
        graphics.drawText(moneyText, "Oleada " + (this.wave + 1), 525, 25);
    }

    /**
     * Método de UPDATE.
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {

        timer += deltaTime;

        if (this.levelData != null){
            enemiesPerWave = this.levelData.getWaveAmounts().get(this.wave);
        }

        // Enemigos de la primera oleada.
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
                if (!e.reachedEnd()){
                    if (this.levelData != null)
                        money+=this.levelData.getReward();
                    else
                        money+= 50;
                }
                else
                    lives -= 1;
                it.remove();
            }
        }

        enemies.removeIf(e -> !e.isActive());

        if (lives == 0){
            iEngine.setScenes(new FinalScene(gameLoader, this.difficulty, false, this.isFirstTime));
        }
    }

    /**
     * Método que GESTIONA el INPUT.
     * @param events
     */
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

    /**
     * Método que añade los enemigos.
     * @param wave
     */
    private void addEnemy(int wave) {
        int vida = 30 + (wave * 15);
        int defensa = 0 + (wave * 2);
        int speed = 50;
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

        AndroidImage enemyImage = null;
        switch(this.wave){
            case 0: case 2:
                enemyImage = this.goblin;
                break;
            case 1:
                enemyImage = this.orc;
                break;
            default:
                break;
        }
        this.enemies.add(new Enemy(this.graphics, this.audio, enemyImage, speed, 5, true, this.mapGrid, vida, defensa, resist));
    }

    /**
     * Método de completar nivel.
     */
    private void completeLevel() {
        if (levelData == null)
            return;

        // if (lives < 1) return;

        ArrayList<Pair<String, Integer>> worlds = this.gameLoader.get_levels();

        int world = levelData.getWorld() - 1;
        int lvlIndex = levelData.getLevel() - 1;


        ArrayList<AdventureScene.LevelState> globalLevelStates = gameLoader.loadLevelStates();

        int globalIndex = 0;

        for (int i = 0; i < world; i++){
            globalIndex += worlds.get(i).second;
        }

        globalIndex += lvlIndex;

        if (globalLevelStates.get(globalIndex) == AdventureScene.LevelState.UNLOCKED_INCOMPLETE) {
            this.isFirstTime = true;
        }

        globalLevelStates.set(globalIndex, AdventureScene.LevelState.UNLOCKED_COMPLETED);

        if (globalIndex + 1 < globalLevelStates.size()) {
            int nextGlobalIndex = globalIndex + 1;
            if (globalLevelStates.get(nextGlobalIndex) == AdventureScene.LevelState.LOCKED) {
                globalLevelStates.set(nextGlobalIndex, AdventureScene.LevelState.UNLOCKED_INCOMPLETE);
            }
        }

        gameLoader.saveLevelsState(globalLevelStates);
    }

    /**
     * Método que gestiona las oleadas.
     */
    private void waves() {


        switch (this.difficulty) {
            case 0:
                if (this.wave < 3) {
                    enemiesPerWave = 5 + this.wave;
                    waveCooldown = 5.0f;
                } else {
                    completeLevel();

                    this.iEngine.setScenes(new FinalScene(gameLoader, this.difficulty, true, this.isFirstTime));
                }
                break;
            case 1:
                if (this.wave < 7) {
                    enemiesPerWave = 7 + this.wave;
                    waveCooldown = 4.0f;
                } else {
                    this.iEngine.setScenes(new FinalScene(gameLoader, this.difficulty, true, this.isFirstTime));
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

    /**
     * Gestor de torres.
     * @param e
     * @return
     */
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

    /**
     * Gestor para posicionar la torre (tras clicar en ella).
     * @param e
     * @return
     */
    private boolean handleTowerPlacement(IInput.TouchEvent e) {
        if (type == null) return false;

        IImage img = null;

        switch (type)
        {
            case Rayo: img = imgRayo; break;
            case Fuego: img = imgFuego; break;
            case Hielo: img = imgHielo; break;
        }

        Tower tower = mapGrid.placeTowerAt(e.x, e.y, type, graphics, audio, img);
        if (tower != null) {
            money -= cost;
            cost = 0;
            audio.playSound(turret, false);
            towers.add(tower);
        }
        mapGrid.showAvailableCells(false);
        type = null;

        for(TowerButton tb : this.towerButtons){
            tb.setSelected(false);
        }

        return true;
    }

    /**
     * Gestor para clicar un botón de torre.
     * @param e
     */
    private void handleTowerTypeSelection(IInput.TouchEvent e) {
        for(TowerButton tb : this.towerButtons){
            if(tb.isTouched(e.x, e.y)) {
                selectTowerType(tb);
                return;
            }
        }
    }

    /**
     * Método para activar el botón de torre (si hay dinero).
     * @param towerBtn
     */
    private void selectTowerType(TowerButton towerBtn) {
        if(money >= towerBtn.getCost()){
            towerBtn.setSelected(true);
            mapGrid.showAvailableCells(true);
            type = towerBtn.getTipo();
            cost = towerBtn.getCost();
        }
    }

    /**
     * Gestor de mejoras.
     * @param e
     */
    private void handleUpgrades(IInput.TouchEvent e) {
        if (sword.isTouched(e.x, e.y)) {
            if(canAffordUpgrade(sword)){
                this.audio.playSound(this.upgrade, false);
                applyUpgrade(0, sword);
            }
        } else if (bow.isTouched(e.x, e.y)) {
            if(canAffordUpgrade(bow)){
                this.audio.playSound(this.upgrade, false);
                applyUpgrade(1, bow);
            }
        } else if (clock.isTouched(e.x, e.y)) {
            if(canAffordUpgrade(clock)){
                this.audio.playSound(this.upgrade, false);
                applyUpgrade(2, clock);
            }
        } else {
            upgrades = false;
        }
    }

    /**
     * Método que devuelve si puede comprar una mejora o no.
     * @param upgradeBtn
     * @return
     */
    private boolean canAffordUpgrade(UpgradeButton upgradeBtn) {
        return money >= upgradeBtn.getCost();
    }

    /**
     * Método que aplica una mejora.
     * @param upgradeId
     * @param upgradeBtn
     */
    private void applyUpgrade(int upgradeId, UpgradeButton upgradeBtn) {
        activeTower.activateUpgrade(upgradeId);
        money -= upgradeBtn.getCost();
        upgrades = false;
    }

    /**
     * Deselector de torres.
     */
    private void deselectAllTowers() {
        for (Tower tower : towers) {
            tower.setSelected(false);
        }
    }
}