package com.example.gamelogic;

import com.example.engine.IAudio;
import com.example.engine.IEngine;
import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IInput;
import com.example.engine.IScene;

import java.util.List;

public class MenuScene implements IScene {

    private IEngine iEngine;
    private IGraphics iGraphics;
    private IAudio iAudio;
    private Button menuButton;

    private IFont fontButton;

    public MenuScene(IEngine iEngine){
        this.iEngine = iEngine;
        this.iGraphics = this.iEngine.getGraphics();
        this.iAudio = this.iEngine.getAudio();
    }

    public void loadAssets()
    {

    }

    @Override
    public void render() {
        iGraphics.setColor(0xff000000);
        IFont font = iGraphics.createFont("fonts/pixelGotic.ttf", 35, false, false);
        iGraphics.drawText(font, "TOWER", 200, 100);
        iGraphics.drawText(font, "DEFENSE", 200, 200);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void handleInput(List<IInput.TouchEvent> events) {

    }
}
