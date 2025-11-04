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

    public void render(){

        this.color = this.path ? 0xffAAAA00 : 0xff000000;
        iGraphics.setColor(this.color);

        if (!this.path){
            if (this.available){
                iGraphics.setColor(0xff00AA00);
            }
            iGraphics.drawRect(x, y, size, size);
        }
        else {
            iGraphics.fillRectangle(x, y, size, size);
        }
    }

    public void setTower(boolean tower) { this.tower = tower; }

    public void setAvailable(boolean available) { this.available = available; }
    public boolean getPath() { return this.path; }

    public boolean getTower() { return this.tower; }
    public int getRow() { return this.row; }

    public int getColumn() { return this.column; }
}
