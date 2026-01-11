package com.example.androidengine;

import android.view.MotionEvent;
import android.view.View;

import com.example.engine.IInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AndroidInput implementa la gestión del input en Android.
 */
public class AndroidInput implements IInput, View.OnTouchListener {

    // Listas de eventos de input.
    private List<TouchEvent> events;
    private List<TouchEvent> pendingEvents;

    /**
     * CONSTRUCTORA.
     */
    AndroidInput()
    {
        events = new ArrayList<>();
        pendingEvents = new ArrayList<>();
    }

    /**
     * Accesor de los eventos de input.
     * @return
     */
    @Override
    synchronized public List<TouchEvent> getTouchEvents() {
        events.clear();

        events.addAll(pendingEvents);
        pendingEvents.clear();
        return events;
    }

    /**
     * Método que gestiona el input táctil.
     * @param view
     * @param motionEvent
     * @return
     */
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        TouchEvent touchEvent = new TouchEvent();
        int index = motionEvent.getActionIndex();

        touchEvent.x = (int) motionEvent.getX(index);
        touchEvent.y = (int) motionEvent.getY(index);

        switch (motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                touchEvent.type = TouchEvent.TouchEventType.TOUCH_DOWN;
                break;
            case MotionEvent.ACTION_UP:
                touchEvent.type = TouchEvent.TouchEventType.TOUCH_UP;
                break;
            case MotionEvent.ACTION_MOVE:
                touchEvent.type = TouchEvent.TouchEventType.TOUCH_MOVE;
                break;
        }

        synchronized (this)
        {
            pendingEvents.add(touchEvent);
        }

        return true;
    }
}
