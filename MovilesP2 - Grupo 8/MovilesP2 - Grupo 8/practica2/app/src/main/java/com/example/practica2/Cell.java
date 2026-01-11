package com.example.practica2;

import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidImage;
import com.example.engine.IGraphics;


/**
 * Cell implementa la funcionalidad de cada celda.
 */
public class Cell {
    // Gráficos.
    private AndroidGraphics graphics;

    // Color.
    private int color;

    // Fila y columna.
    private int row, column;

    // Coordenadas.
    private float x, y;

    // Tamaño de celda.
    private float size;

    // Es camino (sí/no).
    private boolean path;

    // Hay una torre (sí/no).
    private boolean tower;

    // Está disponible (sí/no).
    private boolean available;

    /**
     * CONSTRUCTORA.
     * @param graphics
     * @param x
     * @param y
     * @param size
     * @param color
     * @param row
     * @param col
     * @param path
     */
    public Cell(AndroidGraphics graphics, float x, float y, float size, int color, int row, int col, boolean path){
        this.graphics = graphics;
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

    /**
     * Método de RENDERIZADO.
     */
    public void render() {
        // Si es camino, se dibuja llena.
        if (this.path) {
            graphics.setColor(0xff653c10); // color del camino (amarillo)
            graphics.fillRectangle(x, y, size, size);
        }
        else {
            // Luego, el borde.
            if (this.available) {
                graphics.setColor(0xff00AA00); // Borde verde si está disponible.
            } else {
                graphics.setColor(0xff808080); // Borde gris si no lo está.
            }

            // Dibuja solo el borde.
            graphics.drawRect(x, y, size, size);
        }
    }


    /**
     * SETTERS.
     */
    public void setTower(boolean tower) { this.tower = tower; }
    public void setAvailable(boolean available) { this.available = available; }

    /**
     * GETTERS.
     */
    public boolean isAvailable() { return available; }
    public boolean getPath() { return this.path; }
    public boolean getTower() { return this.tower; }
    public int getRow() { return this.row; }
    public int getColumn() { return this.column; }
    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public float getSize() { return this.size; }

}
