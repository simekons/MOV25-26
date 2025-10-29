package com.example.engine;

public interface IAudio
{
    // Crea "sonido" a partir de una ruta
    ISound newSound(String file);
    // Reproduce sonido
    void playSound(ISound iSound, boolean loop);
    // Para sonido
    void stopSound(ISound iSound);
}
