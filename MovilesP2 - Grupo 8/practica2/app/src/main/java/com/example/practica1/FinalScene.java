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
* FinalScene implementa la escena de victoria.
*/
public class FinalScene implements IScene {

    // Motor.
    private IEngine iEngine;

    // Gráficos
    private IGraphics iGraphics;

    // Audio.
    private IAudio iAudio;

    // Botones de menú.
    private Button playAgainButton;
    private Button menuButton;

    // Fuente de título.
    private IFont titleFont;

    // Fuente de botones.
    private IFont fontButton;

    // Sonido de botón.
    private ISound soundButton;

    // Dificultad previa.
    private int difficulty;

    private boolean win;

    // CONSTRUCTORA
    public FinalScene(IEngine iEngine, int difficulty, boolean win) {
        this.iEngine = iEngine;
        this.iGraphics = iEngine.getGraphics();
        this.iAudio = iEngine.getAudio();

        this.difficulty = difficulty;
        this.win = win;

        this.fontButton = iGraphics.createFont("fonts/fff.ttf", 15, false, false);
        this.titleFont = iGraphics.createFont("fonts/pixelGotic.ttf", 35, false, false);

        this.playAgainButton = new Button(this.iGraphics, this.fontButton, 300,225,150,50, "Volver a jugar", 0xFF808080);
        this.menuButton = new Button(this.iGraphics, this.fontButton, 300,300,150,50, "Menu", 0xFF808080);

        this.soundButton = this.iAudio.newSound("music/button.wav");
    }

    // RENDER
    @Override
    public void render() {
        iGraphics.setColor(0xff000000);
        String mensaje = "";
        mensaje = (win == true) ? "VICTORIA" : "DERROTA";
        iGraphics.drawText(titleFont, mensaje, 300, 100);
        playAgainButton.render();
        menuButton.render();
    }

    // UPDATE
    @Override
    public void update(float deltaTime) { }

    // INPUT
    @Override
    public void handleInput(List<IInput.TouchEvent> events) {
        for(IInput.TouchEvent e : events)
        {
            switch (e.type)
            {
                case TOUCH_UP:
                    if(playAgainButton.isTouched(e.x, e.y))
                    {
                        this.iEngine.setScenes(new GameScene(this.iEngine, this.difficulty));
                    }
                    if(menuButton.isTouched(e.x, e.y))
                    {
                        this.iEngine.setScenes(new MenuScene(this.iEngine));
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
