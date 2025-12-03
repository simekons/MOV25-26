package com.example.practica2;

import com.example.engine.IGraphics;

/*
 *  Cell implementa la funcionalidad de cáda celda.
 */
public class Cell {
    // Gráficos.
    private IGraphics iGraphics;

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

    // CONSTRUCTORA
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

    // RENDERIZADO
    public void render() {
        // Si es camino, se dibuja llena.
        if (this.path) {
            iGraphics.setColor(0xff653c10); // color del camino (amarillo)
            iGraphics.fillRectangle(x, y, size, size);
        }
        else {
            // Luego, el borde.
            if (this.available) {
                iGraphics.setColor(0xff00AA00); // Borde verde si está disponible.
            } else {
                iGraphics.setColor(0xff808080); // Borde gris si no lo está.
            }

            // Dibuja solo el borde.
            iGraphics.drawRect(x, y, size, size);
        }
    }

    // --------------------------SETTERS--------------------------

    // Hay torre.
    public void setTower(boolean tower) { this.tower = tower; }

    // Está disponible.
    public void setAvailable(boolean available) { this.available = available; }

    // --------------------------GETTERS--------------------------

    // Está disponible.
    public boolean isAvailable() { return available; }

    // Es camino.
    public boolean getPath() { return this.path; }

    // Tiene una torre.
    public boolean getTower() { return this.tower; }

    // Filas.
    public int getRow() { return this.row; }

    // Columnas.
    public int getColumn() { return this.column; }

    // Coordenadas.
    public float getX() { return this.x; }
    public float getY() { return this.y; }

    // Tamaño.
    public float getSize() { return this.size; }

}
