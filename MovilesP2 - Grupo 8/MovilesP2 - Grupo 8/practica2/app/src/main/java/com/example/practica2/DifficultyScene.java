package com.example.practica2;

import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidFont;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidImage;
import com.example.androidengine.AndroidSound;
import com.example.engine.IAudio;
import com.example.engine.IEngine;
import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IImage;
import com.example.engine.IInput;
import com.example.engine.IScene;
import com.example.engine.ISound;

import java.util.List;

/**
 * DifficultyScene implementa la pantalla de juego rápido con dificultades.
 */
public class DifficultyScene implements IScene {

    // Motor.
    private AndroidEngine iEngine;

    // Gráficos
    private AndroidGraphics iGraphics;

    // Audio.
    private AndroidAudio iAudio;

    private GameLoader gameLoader;

    // Botones de menú.
    private Button exitButton;
    private Button shortButton;
    private Button longButton;
    private Button infButton;

    private AndroidImage exitImage;

    // Fuente de título.
    private AndroidFont titleFont;

    // Sonido de botón.
    private AndroidSound soundButton;

    // Booleano de juego.
    private boolean startGame;

    private int difficulty;

    /**
     * CONSTRUCTORA.
     * @param gameLoader
     */
    public DifficultyScene(GameLoader gameLoader){
        this.iEngine = AndroidEngine.get_instance();
        this.iGraphics = this.iEngine.getGraphics();
        this.iAudio = this.iEngine.getAudio();
        this.startGame = false;

        this.gameLoader = gameLoader;

        AndroidFont fontButton = iGraphics.createFont("fonts/pixellari.ttf", 25, false, false);
        this.exitImage = this.iGraphics.loadImage("sprites/exit.png");
        this.exitButton = new Button(iGraphics, this.exitImage, 25, 25, 50, 50);
        shortButton = new Button(iGraphics, fontButton, 300,150,150,50, "Short", this.gameLoader.getButtonColor());
        longButton = new Button(iGraphics, fontButton, 300,225,150,50, "Long", this.gameLoader.getButtonColor());
        infButton = new Button(iGraphics, fontButton, 300,300,150,50, "Infinity", this.gameLoader.getButtonColor());
        titleFont = iGraphics.createFont("fonts/pixelGotic.ttf", 30, false, false);

        this.soundButton = this.iAudio.newSound("music/button.wav");
    }

    /**
     * RENDERIZAO.
     */
    @Override
    public void render() {
        iGraphics.clear(gameLoader.getBackgroundColor());
        this.exitButton.render();

        iGraphics.setColor(0xff000000);
        iGraphics.drawText(titleFont, "DIFFICULTY", 300, 100);
        shortButton.render();
        longButton.render();
        infButton.render();
    }

    /**
     * Método de UPDATE.
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        if(startGame){
            iAudio.playSound(soundButton, false);
            iEngine.setScenes(new GameScene(gameLoader, this.difficulty));
        }
    }

    /**
     * Método que GESTIONA el INPUT.
     * @param events
     */
    @Override
    public void handleInput(List<IInput.TouchEvent> events) {
        for(IInput.TouchEvent e : events)
        {
            switch (e.type)
            {
                case TOUCH_UP:
                    if(exitButton.imageIsTouched(e.x, e.y))
                    {
                        this.iAudio.playSound(soundButton, false);
                        this.iEngine.setScenes(new MenuScene(gameLoader));
                    }
                    if(shortButton.isTouched(e.x, e.y))
                    {
                        startGame = true;
                        difficulty = 0;
                    }
                    if(longButton.isTouched(e.x, e.y))
                    {
                        startGame = true;
                        difficulty = 1;
                    }
                    if(infButton.isTouched(e.x, e.y))
                    {
                        startGame = true;
                        difficulty = 2;
                    }
                    break;

                default:
                    break;
            }
        }

    }
}
