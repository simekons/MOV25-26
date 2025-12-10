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

    private ArrayList<LevelState> levelStates;

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

    public enum LevelState {
        LOCKED,
        UNLOCKED_INCOMPLETE,
        UNLOCKED_COMPLETED,
    }

    public AdventureScene(){
        this.engine = AndroidEngine.get_instance();
        this.graphics = this.engine.getGraphics();
        this.audio = this.engine.getAudio();
        this.engine.getAds().setBannerVisible(false);
        this.file = this.engine.getFile();

        loadAssets();

        levelButtons = new ArrayList<>();
        levelStates = new ArrayList<>();

        this.gameLoader = new GameLoader(this.file);

        this.exitButton = new Button(this.graphics, this.exitImage, 25, 25, 50, 50);

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
    public void createLevelsButtons()
    {
        levelButtons.clear();
        int y = offsetY;
        int x;
        int separationX = buttonDimension + 20;
        try
        {
            // Almacena los archivos que se encuentran dentro de "levels" (carpetas de los mundos)
            String[] worlds = engine.getFile().listFiles("levels");
            int buttonIndex = 0;

            // Recorre las carpetas de los mundos
            for(int j = 0; j < worlds.length; j++)
            {
                switch(j){ // temporal, no va
                    case 0:
                        this.worldName = "Mundo 1 - Bosque";
                        break;
                    default:
                        break;
                }
                // Almacena los niveles que se encuentran en ese mundo
                String[] levels = engine.getFile().listFiles("levels/" + worlds[j]);

                int columnIndex = 0;
                int rowStartY = y;
                // Recorre todos los niveles
                for(int i = 0; i < levels.length; i++)
                {
                    // Ignora los archivos "style.json"
                    if(!Objects.equals(levels[i], "style.json"))
                    {
                        // x = 100 + 130
                        x = offsetX + ((i%5) * separationX);
                        int currentY = rowStartY;

                        // x = 100, 100 + 110 * i
                        if (columnIndex == 5){
                            columnIndex = 0;
                            rowStartY += separationBetweenButtons;
                            currentY = rowStartY;
                        }
                        // Si el juego no tiene progreso, crea los botones por defecto
                        if(isNewGame)
                        {
                            // El primer botón estará desbloqueado y sin completar
                            if(buttonIndex == 0)
                            {
                                levelButtons.add(new LevelButton(
                                        this.graphics, scoreFont, x, currentY, buttonDimension, buttonDimension,
                                        String.valueOf(buttonIndex + 1), 0xff116D3A, 0xff000000,
                                        LevelState.UNLOCKED_INCOMPLETE, j + 1, buttonIndex));
                            }
                            // El resto estarán bloqueados
                            else
                            {
                                levelButtons.add(new LevelButton(
                                        this.graphics, lockImage, x, currentY, buttonDimension, buttonDimension,
                                        0xff88A536, LevelState.LOCKED, j + 1, buttonIndex));
                            }
                            levelStates.add(levelButtons.get(buttonIndex).getLevelState());
                        }
                        // Si el juego tiene progreso, carga el estado de los botones guardado en el almacenamiento interno
                        else
                        {
                            LevelState state = levelStates.get(buttonIndex); // Estado del nivel
                            if (state == LevelState.LOCKED || state == null) {
                                levelButtons.add(new LevelButton(
                                        this.graphics, lockImage, x, currentY, buttonDimension, buttonDimension,
                                        0xfffef3a5, LevelState.LOCKED, j + 1, buttonIndex));
                            } else {
                                levelButtons.add(new LevelButton(
                                        this.graphics, scoreFont, x, currentY, buttonDimension, buttonDimension,
                                        String.valueOf(buttonIndex + 1), 0xfffef3a5, 0xff000000,
                                        LevelState.UNLOCKED_INCOMPLETE, j + 1, buttonIndex));
                            }
                        }
                        columnIndex++;

                        buttonIndex++;

                    }
                }
                y = rowStartY + separationBetweenButtons;
            }
            maxScrollOffset = Math.max(0, (y + separationBetweenButtons) - 600);
            gameLoader.saveLevelsState(levelStates);
        } catch (Exception e)
        {

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
            loadLevelStates();
            createLevelsButtons();
            setStyleLevelButtons();
            this.created = true;
        }

        this.graphics.setColor(0xff116D3A);
        this.graphics.fillRectangle(100, 5, 400, 60);
        this.graphics.setColor(0xff000000);
        this.graphics.drawText(this.worldFont, this.worldName, 300, 50);


        for(LevelButton lb : this.levelButtons){
            if (lb.getY() > 60 && lb.getY() < 600){
                lb.render();
            }
        }
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
                        this.engine.setScenes(new MenuScene());
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
                    this.engine.setScenes(new GameScene(levelData));
                }
            }
        }
    }

    private void loadLevelStates(){

    }

    private void setStyleLevelButtons(){

    }
}
