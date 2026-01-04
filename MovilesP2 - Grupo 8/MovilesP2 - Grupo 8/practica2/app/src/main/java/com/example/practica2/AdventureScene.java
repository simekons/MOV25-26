package com.example.practica2;

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
import java.util.Objects;

public class AdventureScene implements IScene {


    // Motor.
    private AndroidEngine engine;

    // Gráficos
    private AndroidGraphics graphics;

    // Audio.
    private AndroidAudio audio;

    private AndroidFile file;
    private ArrayList<LevelButton> levelButtons;

    private static ArrayList<LevelState> levelStates;

    //variables botones
    private int levelsNumber;
    private int offsetX;
    private int offsetY;
    private int buttonDimension;
    private int separationBetweenButtons;

    //variables scroll
    private int lastTouchY;
    private int scrollOffset;
    private int scrollSpeed;
    private int maxScrollOffset;
    private int minScrollOffset;
    private boolean isScrolling;

    private boolean isNewGame = true;
    private boolean created = false;

    private AndroidImage lockImage;

    private AndroidFont scoreFont;
    private AndroidFont worldFont;

    private GameLoader gameLoader;

    private Button exitButton;
    private AndroidImage exitImage;

    private AndroidSound clickButton;

    private String worldName;

    private Button leftArrow;

    private Button rightArrow;

    private int currentWorld;

    private int totalWorlds;
    public enum LevelState {
        LOCKED,
        UNLOCKED_INCOMPLETE,
        UNLOCKED_COMPLETED,
    }

    public AdventureScene(GameLoader gameLoader){
        this.engine = AndroidEngine.get_instance();
        this.graphics = this.engine.getGraphics();
        this.audio = this.engine.getAudio();
        this.engine.getAds().setBannerVisible(false);
        this.file = this.engine.getFile();

        loadAssets();

        levelButtons = new ArrayList<>();
        levelStates = new ArrayList<>();
        this.totalWorlds = 2;
        this.currentWorld= 0;

        this.gameLoader = gameLoader;

        this.exitButton = new Button(this.graphics, this.exitImage, 25, 25, 50, 50);
        leftArrow = new Button(graphics, this.exitImage, 50, 300, 50, 50);
        rightArrow = new Button(graphics, this.exitImage, 500, 300, 50, 50);

        buttonsVariables();

        this.clickButton = this.audio.newSound("music/button.wav");
    }

    public void buttonsVariables()
    {
        this.levelsNumber = 14;
        this.offsetX = 20;
        this.offsetY = 75;
        this.buttonDimension = 100;
        this.separationBetweenButtons = buttonDimension + offsetX;

        this.lastTouchY = -1;
        this.scrollOffset = 0;
        this.maxScrollOffset = 0;
        this.minScrollOffset = 0;
        this.scrollSpeed = 10;
        this.isScrolling = false;
    }
    public void createLevelsButtons() {
        levelButtons.clear();

        int x, y;
        int separationX = buttonDimension + 20;
        int separationY = buttonDimension + 20;

        int levelsPerWorld = 14;
        int baseIndex = currentWorld * levelsPerWorld;

        // ruta del style.json de cada mundo
        this.gameLoader.loadStyle(currentWorld);
        for (int i = 0; i < levelsPerWorld; i++) {
            int col = i % 5;
            int row = i / 5;

            x = offsetX + col * separationX;
            y = offsetY + row * separationY;

            LevelState state = levelStates.get(i);

            if (state == LevelState.LOCKED) {
                levelButtons.add(new LevelButton(
                        graphics, lockImage, x, y,
                        buttonDimension, buttonDimension,
                        this.gameLoader.get_locked(), LevelState.LOCKED,
                        currentWorld + 1, i
                ));
            } else {
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


    private void loadAssets(){
        this.exitImage = this.graphics.loadImage("sprites/bow.png");
        this.scoreFont = this.graphics.createFont("fonts/pixelGotic.ttf", 30, false, false);
        this.worldFont = this.graphics.createFont("fonts/pixelGotic.ttf", 20, false, false);
        this.lockImage = this.graphics.loadImage("sprites/lock.png");
    }

    @Override
    public void render() {

        this.exitButton.render();
        this.graphics.setColor(0xff000000);
        //this.graphics.drawText(this.scoreFont, "Adventure", 300, 40);

        if (!this.created){
            loadLevelStatesForCurrentWorld(this.currentWorld);
            this.created = true;
        }

        switch(currentWorld){
            case 0:
                this.graphics.setColor(0xff116D3A);
                break;
            case 1:
                this.graphics.setColor(0xffffdd77);
                break;
        }
        this.graphics.fillRectangle(100, 5, 400, 60);

        for(LevelButton lb : this.levelButtons){
            if (lb.getY() > 60 && lb.getY() < 600){
                lb.render();
            }
        }

        leftArrow.render();
        rightArrow.render();

        String[] worldNames = {
                "Mundo 1 - Bosque",
                "Mundo 2 - Desierto"
        };
        graphics.drawText(worldFont, worldNames[currentWorld], 300, 50);
    }

    @Override
    public void update(float deltaTime) {

    }

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
                        if (currentWorld < 2 - 1) {
                            currentWorld++;
                            loadLevelStatesForCurrentWorld(this.currentWorld);
                        }
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

    /*
    public void completeLevel(int world, int levelIndexInWorld) {

        int levelsPerWorld = 14;
        int globalIndex = world * levelsPerWorld + levelIndexInWorld;

        ArrayList<LevelState> globalStates = gameLoader.loadLevelStates();

        // Completa el nivel actual
        globalStates.set(globalIndex, LevelState.UNLOCKED_COMPLETED);

        // Desbloquea el siguiente nivel global
        if (globalIndex + 1 < globalStates.size()) {
            if (globalStates.get(globalIndex + 1) == LevelState.LOCKED) {
                globalStates.set(globalIndex + 1, LevelState.UNLOCKED_INCOMPLETE);
            }
        }

        // Recarga el mundo actual en pantalla
        loadLevelStatesForCurrentWorld(currentWorld);
        createLevelsButtons();
    }

*/
    public void loadLevelStatesForCurrentWorld(int world) {
        int levelsPerWorld = 14;

        // Cargar todos los estados globales desde GameLoader
        ArrayList<LevelState> globalLevelStates = gameLoader.loadLevelStates();

        levelStates.clear();

        int baseIndex = world * levelsPerWorld; // solo este mundo
        for (int i = 0; i < levelsPerWorld; i++) {
            int globalIndex = baseIndex + i;
            if (globalIndex < globalLevelStates.size()) {
                levelStates.add(globalLevelStates.get(globalIndex));
            } else {
                levelStates.add(LevelState.LOCKED);
            }
        }
        createLevelsButtons();
        setStyleLevelButtons();
    }


    public void handleLevelButtons(AndroidInput.TouchEvent e)
    {
        for (LevelButton lb : levelButtons) {
            if (lb.isTouched(e.x, e.y) && lb.getLevelState() == LevelState.UNLOCKED_INCOMPLETE) {
                this.audio.playSound(clickButton, false);

                int world = lb.getWorld();
                int level = lb.getLevel();
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

    private void setStyleLevelButtons(){

    }

    public static ArrayList<LevelState> getLevelStates() { return levelStates; }

    public static void setLevelState(int i, LevelState levelState) { levelStates.set(i, levelState); }
}
