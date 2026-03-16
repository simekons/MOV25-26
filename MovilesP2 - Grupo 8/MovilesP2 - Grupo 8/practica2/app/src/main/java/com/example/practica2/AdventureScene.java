package com.example.practica2;

import android.util.Pair;

import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidFile;
import com.example.androidengine.AndroidFont;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidImage;
import com.example.androidengine.AndroidInput;
import com.example.androidengine.AndroidSound;
import com.example.engine.IInput;
import com.example.engine.IScene;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * AdventureScene es la clase que implementa la escena de Aventura, con los niveles de cada mundo.
 */
public class AdventureScene implements IScene {


    // Motor
    private AndroidEngine engine;

    // Gráficos
    private AndroidGraphics graphics;

    // Audio
    private AndroidAudio audio;

    // Botones de niveles.
    private ArrayList<LevelButton> levelButtons;

    // Estados de los niveles.
    private static ArrayList<LevelState> levelStates;

    // Variables para los botones
    private int offsetX;
    private int offsetY;
    private int buttonDimension;

    // Variables para el scroll
    private int lastTouchY;
    private int scrollOffset;
    private int maxScrollOffset;
    private int minScrollOffset;
    private boolean isScrolling;

    private boolean created = false;

    // Imágenes
    private AndroidImage lockImage;
    private AndroidImage exitImage;
    private AndroidImage leftImage;
    private AndroidImage rightImage;

    // Fuentes
    private AndroidFont scoreFont;
    private AndroidFont worldFont;

    // Botones
    private Button exitButton;
    private Button rightArrow;
    private Button leftArrow;

    // GameLoader
    private GameLoader gameLoader;

    // Sonidos
    private AndroidSound clickButton;

    // Mundo actual.
    private int currentWorld;
    private int totalWorlds;
    private int levelsPerWorld;

    private ArrayList<Pair<String, Integer>> worlds;
    private String worldName;

    // Estados de los niveles.
    public enum LevelState {
        LOCKED,
        UNLOCKED_INCOMPLETE,
        UNLOCKED_COMPLETED,
    }

    /**
     * CONSTRUCTORA.
     * @param gameLoader
     */
    public AdventureScene(GameLoader gameLoader){
        this.engine = AndroidEngine.get_instance();
        this.graphics = this.engine.getGraphics();
        this.audio = this.engine.getAudio();
        this.engine.getAds().setBannerVisible(false);

        loadAssets();

        // Botones de niveles.
        this.levelButtons = new ArrayList<>();

        // Estados de los niveles.
        this.levelStates = new ArrayList<>();
        this.currentWorld = 0;

        // GameLoader.
        this.gameLoader = gameLoader;
        this.worlds = this.gameLoader.get_levels();

        // Botones de salida, mundo a la izq y mundo a la der.
        this.exitButton = new Button(this.graphics, this.exitImage, 25, 25, 50, 50);
        this.leftArrow = new Button(this.graphics, this.leftImage, 120, 35, 40, 40);
        this.rightArrow = new Button(this.graphics, this.rightImage, 480, 35, 40, 40);

        // Variables para los botones.
        buttonsVariables();

        // Sonido de clic.
        this.clickButton = this.audio.newSound("music/button.wav");
    }

    /**
    * Método que inicializa las variables necesarias para los botones.
    */
    public void buttonsVariables()
    {
        this.offsetX = 20;
        this.offsetY = 80;
        this.buttonDimension = 100;

        this.lastTouchY = -1;
        this.scrollOffset = 0;
        int rows = (levelsPerWorld + 4) / 5;
        this.maxScrollOffset = 50;
        this.minScrollOffset = 0;
        this.isScrolling = false;
    }

    /**
     * Método que crea los botones de los niveles.
     */
    public void createLevelsButtons() {
        levelButtons.clear();

        int x, y;
        int separationX = buttonDimension + 20;
        int separationY = buttonDimension + 20;

        this.worldName = this.worlds.get(this.currentWorld).first;

        this.levelsPerWorld = this.worlds.get(this.currentWorld).second;

        // ruta del style.json de cada mundo
        this.gameLoader.loadStyle(currentWorld);
        for (int i = 0; i < this.levelsPerWorld; i++) {
            int col = i % 5;
            int row = i / 5;

            x = offsetX + col * separationX;
            y = offsetY + row * separationY;

            LevelState state = levelStates.get(i);

            if (state == LevelState.LOCKED) {
                levelButtons.add(new LevelButton(
                        graphics, lockImage, x, y,
                        buttonDimension, buttonDimension,
                        this.gameLoader.get_locked(), state,
                        currentWorld + 1, i
                ));
            } else if (state == LevelState.UNLOCKED_INCOMPLETE) {
                levelButtons.add(new LevelButton(
                        graphics, scoreFont, x, y,
                        buttonDimension, buttonDimension,
                        String.valueOf(i + 1),
                        this.gameLoader.get_locked(), 0xff000000,
                        state,
                        currentWorld + 1, i
                ));
            }
            else {
                levelButtons.add(new LevelButton(
                        graphics, scoreFont, x, y,
                        buttonDimension, buttonDimension,
                        String.valueOf(i + 1),
                        this.gameLoader.get_unlocked(), 0xff000000,
                        state,
                        currentWorld + 1, i
                ));
            }
        }
    }

    /*
     * Método que carga los assets.
     * */
    private void loadAssets(){
        this.exitImage = this.graphics.loadImage("sprites/exit.png");
        this.scoreFont = this.graphics.createFont("fonts/pixellari.ttf", 50, false, false);
        this.worldFont = this.graphics.createFont("fonts/pixellari.ttf", 35, false, false);
        this.lockImage = this.graphics.loadImage("sprites/lock.png");
        this.leftImage = this.graphics.loadImage("sprites/arrow1.png");
        this.rightImage = this.graphics.loadImage("sprites/arrow2.png");
    }

    /*
     * Método de RENDERIZADO.
     * */
    @Override
    public void render() {
        // Botón de salida
        this.exitButton.render();
        this.graphics.setColor(0xff000000);

        if (!this.created){
            loadLevelStatesForCurrentWorld(this.currentWorld);
            this.created = true;
        }

        // Color según el mundo

        this.graphics.setColor(this.gameLoader.get_worldcolor());
        this.graphics.fillRectangle(150, 5, 300, 60);

        // Botones de niveles
        for(LevelButton lb : this.levelButtons){
            if (lb.getY() > 60 && lb.getY() < 600){
                lb.render();
            }
        }

        // Flechas de cambio entre mundos
        leftArrow.render();
        rightArrow.render();

        this.worldName = "Mundo " + String.valueOf(this.currentWorld + 1) + " - " + this.worlds.get(this.currentWorld).first;
        graphics.drawText(worldFont, this.worldName, 300, 50);
    }

    /**
     * Método UPDATE vacío.
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {

    }

    /**
     * Método que GESTIONA el INPUT.
     * @param events
     */
    @Override
    public void handleInput(List<IInput.TouchEvent> events) {
        for(AndroidInput.TouchEvent e : events)
        {
            switch (e.type)
            {
                // Activa scroll
                case TOUCH_DOWN:
                    lastTouchY = e.y;
                    isScrolling = true;
                    break;
                // Desactiva scroll y detecta si un botón de un nivel ha sido pulsado
                case TOUCH_UP:
                    lastTouchY = -1;
                    isScrolling = false;
                    if(exitButton.imageIsTouched(e.x, e.y))
                    {
                        this.audio.playSound(clickButton, false);
                        this.engine.setScenes(new MenuScene(gameLoader));
                    }
                    if (leftArrow.isTouched(e.x, e.y)) {
                        if (currentWorld > 0) {
                            currentWorld--;
                            loadLevelStatesForCurrentWorld(this.currentWorld);
                        }
                    }

                    if (rightArrow.isTouched(e.x, e.y)) {
                        currentWorld++;
                        if (currentWorld < this.gameLoader.get_totalWorlds()) {
                            loadLevelStatesForCurrentWorld(this.currentWorld);
                        }
                        else
                            currentWorld--;
                    }
                    handleLevelButtons(e);
                    break;
                // Ocurre scroll
                case TOUCH_MOVE:
                    if (isScrolling) {
                        int deltaY = lastTouchY - e.y;
                        lastTouchY = e.y;

                        scrollOffset += deltaY;
                        scrollOffset = Math.max(minScrollOffset, Math.min(maxScrollOffset, scrollOffset));

                        for (LevelButton lb : levelButtons) {
                            lb.setY(lb.getOriginalY() - scrollOffset);
                        }
                    }
                    break;
            }
        }
    }

    /**
     * Método que carga el estado de los niveles del mundo actual.
     * @param world
     */
    public void loadLevelStatesForCurrentWorld(int world) {
        this.levelsPerWorld = this.worlds.get(this.currentWorld).second;

        // Cargar todos los estados globales desde GameLoader
        ArrayList<LevelState> globalLevelStates = gameLoader.loadLevelStates();

        levelStates.clear();

        int baseIndex = 0;
        for (int i = 0; i < world; i++){
            baseIndex += this.worlds.get(i).second;
        }

        for (int i = baseIndex; i < baseIndex + levelsPerWorld; i++) {
            levelStates.add(globalLevelStates.get(i));
        }
        createLevelsButtons();
    }

    /**
     * Método que gestiona el PRESIONADO de los niveles.
     * @param e
     */
    public void handleLevelButtons(AndroidInput.TouchEvent e)
    {
        for (LevelButton lb : levelButtons) {
            if (lb.isTouched(e.x, e.y) && lb.getLevelState() != LevelState.LOCKED ) {
                this.audio.playSound(clickButton, false);

                int world = lb.getWorld();
                int level = lb.getLevel() + 1;
                // Carga los niveles del almacenamiento por si el nivel está guardado
                LevelData levelData = gameLoader.loadLevelFromFiles(world, level);
                // Si el nivel no está guardado, lo carga de los assets
                if(levelData == null)
                    levelData = gameLoader.loadLevelFromAssets(world, level);

                // Lanza la partida con los datos cargados
                if (levelData != null) {
                    this.engine.setScenes(new GameScene(levelData, gameLoader));
                }
            }
        }
    }
}
