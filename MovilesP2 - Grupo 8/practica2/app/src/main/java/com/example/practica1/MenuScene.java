package com.example.practica1;

import com.example.engine.IAudio;
import com.example.engine.IEngine;
import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IInput;
import com.example.engine.IScene;
import com.example.engine.ISound;

import java.util.List;

/*
 * MenuScene es la escena de menú.
 */
public class MenuScene implements IScene {

    // Motor.
    private IEngine iEngine;

    // Gráficos
    private IGraphics iGraphics;

    // Audio.
    private IAudio iAudio;

    // Botón de menú.
    private Button menuButton;

    // Fuente de título.
    private IFont titleFont;

    // Sonido de botón.
    private ISound soundButton;

    // Booleano de juego.
    private boolean startGame;

    // CONSTRUCTORA
    public MenuScene(IEngine iEngine){
        this.iEngine = iEngine;
        this.iGraphics = this.iEngine.getGraphics();
        this.iAudio = this.iEngine.getAudio();
        this.startGame = false;

        IFont fontButton = iGraphics.createFont("fonts/fff.ttf", 15, false, false);
        menuButton = new Button(iGraphics, fontButton, 300,300,100,50, "Play", 0xFF808080);
        titleFont = iGraphics.createFont("fonts/pixelGotic.ttf", 35, false, false);

        this.soundButton = this.iAudio.newSound("music/button.wav");
    }

    // RENDERIZADO
    @Override
    public void render() {
        iGraphics.setColor(0xff000000);
        iGraphics.drawText(titleFont, "TOWER", 300, 100);
        iGraphics.drawText(titleFont, "DEFENSE", 300, 200);
        menuButton.render();
    }

    // Update.
    @Override
    public void update(float deltaTime) {
        if(startGame){
            iEngine.setScenes(new DifficultyScene(iEngine));
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
                    if(menuButton.isTouched(e.x, e.y))
                    {
                        iAudio.playSound(soundButton, false);
                        startGame = true;
                    }
                    break;

                default:
                    break;
            }
        }

    }
}
