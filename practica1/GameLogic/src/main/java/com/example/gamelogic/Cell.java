package com.example.gamelogic;

import com.example.engine.IGraphics;

public class Cell {
    private IGraphics iGraphics;
    private int color;
    private int row;
    private int column;
    private float x;
    private float y;
    private float size;

    private boolean path;

    private boolean tower;

    private boolean available;

    public Cell(IGraphics iGraphics, float x, float y, float size, int color, int row, int col, boolean path){
        this.iGraphics = iGraphics;
        this.color = color;
        this.row = row;
        this.column = col;
        this.x = x;
        this.y = y;
        this.size = size;
        this.path = path;
        this.tower = false;
        this.available = false;
    }

    public void render() {
        // Si es camino, se dibuja llena
        if (this.path) {
            iGraphics.setColor(0xffAAAA00); // color del camino (amarillo)
            iGraphics.fillRectangle(x, y, size, size);
        }
        else {
            // Primero, relleno (fondo)
            /*
            if (this.tower) {
                iGraphics.setColor(0xff444444); // gris si hay torre
                iGraphics.fillRectangle(x, y, size, size);
            } else {
                iGraphics.setColor(0xff000000); // fondo negro normal
                iGraphics.fillRectangle(x, y, size, size);
            }*/

            // Luego, el borde
            if (this.available) {
                iGraphics.setColor(0xff00AA00); // borde verde si está disponible
            } else {
                iGraphics.setColor(0xff000000); // borde negro si no lo está
            }

            // Dibuja solo el borde
            iGraphics.drawRect(x, y, size, size);
        }
    }


    public void setTower(boolean tower) { this.tower = tower; }

    public void setAvailable(boolean available) { this.available = available; }
    public boolean getPath() { return this.path; }

    public boolean getTower() { return this.tower; }
    public int getRow() { return this.row; }

    public int getColumn() { return this.column; }
    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public float getSize() { return this.size; }

}
