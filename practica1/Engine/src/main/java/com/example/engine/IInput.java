package com.example.engine;

import java.util.List;

public interface IInput
{
    // Clase TouchEvent que representa un evento de teclado con un tipo y coordenadas
    class TouchEvent
    {
        public static enum TouchEventType{
            TOUCH_UP,
            TOUCH_DOWN,
            TOUCH_MOVE;
        }
        public TouchEventType type;
        public int finger;
        public int x;
        public int y;

    }

    // Getter de la lista de eventos
    public List<TouchEvent> getTouchEvents();
}
