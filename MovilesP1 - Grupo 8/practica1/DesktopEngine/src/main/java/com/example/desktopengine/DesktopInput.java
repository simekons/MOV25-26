package com.example.desktopengine;


import com.example.engine.IInput;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class DesktopInput implements IInput, MouseListener, MouseMotionListener
{
    // Listas de eventos de input.
    private List<TouchEvent> events;
    private List<TouchEvent> pendingEvents;

    DesktopInput()
    {
        this.events = new ArrayList<>();
        this.pendingEvents = new ArrayList<>();
    }

    // Click del ratón.
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    // Pulsación del ratón.
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseEvent.BUTTON1)
        {
            TouchEvent event = new TouchEvent();
            event.type = TouchEvent.TouchEventType.TOUCH_DOWN;
            event.finger = 0;
            event.x = mouseEvent.getX();
            event.y = mouseEvent.getY();

            synchronized (this)
            {
                this.pendingEvents.add(event);
            }
        }
    }

    // Soltar pulsación del ratón.
    public void mouseReleased(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseEvent.BUTTON1)
        {
            TouchEvent event = new TouchEvent();
            event.type = TouchEvent.TouchEventType.TOUCH_UP;
            event.finger = 0;
            event.x = mouseEvent.getX();
            event.y = mouseEvent.getY();

            synchronized (this)
            {
                this.pendingEvents.add(event);
            }
        }
    }

    // Entra en posición X,Y.
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    // Sale de posición X,Y.
    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    // Arrastre del ratón.
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if(mouseEvent.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK)
        {
            TouchEvent event = new TouchEvent();
            event.type = TouchEvent.TouchEventType.TOUCH_MOVE;
            event.finger = 0;
            event.x = mouseEvent.getX();
            event.y = mouseEvent.getY();

            synchronized (this)
            {
                this.pendingEvents.add(event);
            }
        }
    }

    // Movimiento del ratón.
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    // Lista de eventos del input.
    @Override
    synchronized public List<TouchEvent> getTouchEvents() {
        this.events.clear();

        this.events.addAll(this.pendingEvents);
        this.pendingEvents.clear();

        return this.events;
    }
}
