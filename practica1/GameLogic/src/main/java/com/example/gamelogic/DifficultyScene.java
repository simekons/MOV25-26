package com.example.gamelogic;

import com.example.engine.IAudio;
import com.example.engine.IEngine;
import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IInput;
import com.example.engine.IScene;
import com.example.engine.ISound;

import java.util.List;

/*
 * DifficultyScene es la escena de menú de seleccion de dificultad.
 */
public class DifficultyScene implements IScene {

    // Motor.
    private IEngine iEngine;

    // Gráficos
    private IGraphics iGraphics;

    // Audio.
    private IAudio iAudio;

    // Botones de menú.
    private Button shortButton;
    private Button longButton;
    private Button infButton;

    // Fuente de título.
    private IFont titleFont;

    // Sonido de botón.
    private ISound soundButton;

    // Booleano de juego.
    private boolean startGame;

    private int difficulty;

    // CONSTRUCTORA
    public DifficultyScene(IEngine iEngine){
        this.iEngine = iEngine;
        this.iGraphics = this.iEngine.getGraphics();
        this.iAudio = this.iEngine.getAudio();
        this.startGame = false;

        var fontButton = iGraphics.createFont("fonts/fff.ttf", 15, false, false);
        shortButton = new Button(iGraphics, fontButton, 300,150,150,50, "Short", 0xFF808080);
        longButton = new Button(iGraphics, fontButton, 300,225,150,50, "Long", 0xFF808080);
        infButton = new Button(iGraphics, fontButton, 300,300,150,50, "Infinity", 0xFF808080);
        titleFont = iGraphics.createFont("fonts/pixelGotic.ttf", 35, false, false);
    }

    // RENDERIZADO
    @Override
    public void render() {
        iGraphics.setColor(0xff000000);
        iGraphics.drawText(titleFont, "DIFFICULTY", 300, 100);
        shortButton.render();
        longButton.render();
        infButton.render();
    }

    // Update.
    @Override
    public void update(float deltaTime) {
        if(startGame){
            //iAudio.playSound(soundButton, false);
            iEngine.setScenes(new GameScene(iEngine));
        }
    }

    // Input.
    @Override
    public void handleInput(List<IInput.TouchEvent> events) {
        for(IInput.TouchEvent e : events)
        {
            switch (e.type)
            {
                case TOUCH_UP:
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
