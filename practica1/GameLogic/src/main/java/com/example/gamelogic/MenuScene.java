package com.example.gamelogic;

import com.example.engine.IAudio;
import com.example.engine.IEngine;
import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IInput;
import com.example.engine.IScene;
import com.example.engine.ISound;

import java.util.List;

public class MenuScene implements IScene {

    private IEngine iEngine;
    private IGraphics iGraphics;
    private IAudio iAudio;
    private Button menuButton;
    private IFont titleFont;
    private ISound soundButton;
    private boolean startGame;

    public MenuScene(IEngine iEngine){
        this.iEngine = iEngine;
        this.iGraphics = this.iEngine.getGraphics();
        this.iAudio = this.iEngine.getAudio();
        this.startGame = false;

        var fontButton = iGraphics.createFont("fonts/fff.ttf", 15, false, false);
        menuButton = new Button(iGraphics, fontButton, 300,300,100,50, "Play", 0xFF808080);
        titleFont = iGraphics.createFont("fonts/pixelGotic.ttf", 35, false, false);
    }

    @Override
    public void render() {
        iGraphics.setColor(0xff000000);
        iGraphics.drawText(titleFont, "TOWER", 300, 100);
        iGraphics.drawText(titleFont, "DEFENSE", 300, 200);
        menuButton.render();
    }

    @Override
    public void update(float deltaTime) {
        if(startGame){
            //iAudio.playSound(soundButton, false);
            iEngine.setScenes(new GameScene(iEngine));
        }
    }

    @Override
    public void handleInput(List<IInput.TouchEvent> events) {
        for(IInput.TouchEvent e : events)
        {
            switch (e.type)
            {
                case TOUCH_UP:
                    if(menuButton.isTouched(e.x, e.y))
                    {
                        startGame = true;
                    }
                    break;

                default:
                    break;
            }
        }

    }
}
