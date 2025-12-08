package com.example.practica2;

import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidFile;
import com.example.androidengine.AndroidFont;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidImage;
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

    private boolean isNewGame;
    private boolean created = false;

    private AndroidImage lockImage;

    private AndroidFont scoreFont;

    private GameLoader gameLoader;

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

        loadAssets();

        levelButtons = new ArrayList<>();
        levelStates = new ArrayList<>();

        this.gameLoader = new GameLoader(this.file);
    }

    public void buttonsVariables()
    {
        this.levelsNumber = 18;
        this.offsetX = 20;
        this.offsetY = 75;
        this.buttonDimension = 110;
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

        try
        {
            // Almacena los archivos que se encuentran dentro de "levels" (carpetas de los mundos)
            String[] worlds = engine.getFile().listFiles("levels");
            int buttonIndex = 0;

            // Recorre las carpetas de los mundos
            for(int j = 0; j < worlds.length; j++)
            {
                // Almacena los niveles que se encuentran en ese mundo
                String[] levels = engine.getFile().listFiles("levels/" + worlds[j]);
                // Recorre todos los niveles
                for(int i = 0; i < levels.length; i++)
                {
                    // Ignora los archivos "style.json"
                    if(!Objects.equals(levels[i], "style.json"))
                    {
                        int x = offsetX + (buttonIndex % 3) * separationBetweenButtons;
                        if (buttonIndex % 3 == 0 && buttonIndex != 0) y += separationBetweenButtons;
                        // Si el juego no tiene progreso, crea los botones por defecto
                        if(isNewGame)
                        {
                            // El primer botón estará desbloqueado y sin completar
                            if(buttonIndex == 0)
                            {
                                levelButtons.add(new LevelButton(
                                        this.graphics, scoreFont, x, y, buttonDimension, buttonDimension,
                                        String.valueOf(buttonIndex + 1), 0xfffef3a5, 0xff000000,
                                        LevelState.UNLOCKED_INCOMPLETE, j + 1, buttonIndex));
                            }
                            // El resto estarán bloqueados
                            else
                            {
                                levelButtons.add(new LevelButton(
                                        this.graphics, lockImage, x, y, buttonDimension, buttonDimension,
                                        0xfffef3a5, LevelState.LOCKED, j + 1, buttonIndex));
                            }
                            levelStates.add(levelButtons.get(buttonIndex).getLevelState());
                        }
                        // Si el juego tiene progreso, carga el estado de los botones guardado en el almacenamiento interno
                        else
                        {
                            LevelState state = levelStates.get(buttonIndex); // Estado del nivel
                            if (state == LevelState.LOCKED || state == null) {
                                levelButtons.add(new LevelButton(
                                        this.graphics, lockImage, x, y, buttonDimension, buttonDimension,
                                        0xfffef3a5, LevelState.LOCKED, j + 1, buttonIndex));
                            } else {
                                levelButtons.add(new LevelButton(
                                        this.graphics, scoreFont, x, y, buttonDimension, buttonDimension,
                                        String.valueOf(buttonIndex + 1), 0xfffef3a5, 0xff000000,
                                        LevelState.UNLOCKED_INCOMPLETE, j + 1, buttonIndex));
                            }
                        }
                        buttonIndex++;
                    }

                }
            }
            maxScrollOffset = Math.max(0, (y + separationBetweenButtons) - 600);
            gameLoader.saveLevelsState(levelStates);
        } catch (Exception e)
        {

        }
    }
    private void loadAssets(){

    }

    @Override
    public void render() {

    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void handleInput(List<IInput.TouchEvent> events) {

    }
}
