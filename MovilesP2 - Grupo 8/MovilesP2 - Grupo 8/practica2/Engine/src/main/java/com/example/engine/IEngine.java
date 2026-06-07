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

    // Programa una notificación local para lanzarse tras 'seconds' segundos
    void scheduleNotification(long seconds, String title, String text, int icon);
}