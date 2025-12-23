package com.example.practica2;

import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidFile;
import com.example.androidengine.AndroidGraphics;
import com.example.engine.IInput;
import com.example.engine.IScene;

import java.util.List;

public class ShopScene implements IScene {

    private AndroidEngine iEngine;
    private AndroidGraphics iGraphics;
    private AndroidAudio iAudio;
    private AndroidFile iFile;

    public ShopScene()
    {
        this.iEngine = AndroidEngine.get_instance();
        this.iGraphics = this.iEngine.getGraphics();
        this.iAudio = this.iEngine.getAudio();
        this.iFile = this.iEngine.getFile();
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
