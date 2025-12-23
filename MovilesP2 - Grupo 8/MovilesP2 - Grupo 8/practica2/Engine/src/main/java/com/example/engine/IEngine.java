package com.example.engine;

public interface IEngine
{
    // Getter del Graphics
    IGraphics getGraphics();
    // Getter del Audio
    IAudio getAudio();
    // Setter de las escenas
    void setScenes(IScene scene);

    IFile getFile();
}