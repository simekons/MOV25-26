package com.example.engine;

import java.util.List;

public interface IScene
{
    // Renderizado de la escena
    void render();
    // Reproducción del comportamiento de la escena frame por frame
    void update(float deltaTime);
    // Gestión de los eventos de teclado y sus comportamientos en la escena
    void handleInput(List<IInput.TouchEvent> events);
}
