package com.example.gamelogic;

import com.example.engine.IGraphics;

public class Tower {
    private IGraphics iGraphics;

    private float x;
    private float y;
    private int row;
    private int column;

    private float size;
    private int cost;

    private int type;

    public Tower(IGraphics iGraphics, float x, float y, int row, int column, float size, int cost, int type, MapGrid map){
        this.iGraphics = iGraphics;
        this.row = row;
        this.column = column;
        this.size = size;
        this.cost = cost;
        this.type = type;

        int w = 900 / map.getColumns();

        this.x = w * column + w/2f;
        this.y = w * row + w/2f;
    }

    public void render(){
        switch(type){
            case 0:
                this.iGraphics.setColor(0xffFF0000);
                this.iGraphics.drawHexagon(this.x, this.y, this.size);
                break;
            case 1:
                this.iGraphics.setColor(0xff00FF00);
                this.iGraphics.fillRectangle(this.x- this.size/2f, this.y- this.size/2f, this.size, this.size);
                break;
            case 2:
                this.iGraphics.setColor(0xff0000FF);
                // this.iGraphics.drawTriangle();
                break;
            default:
                break;
        }
    }
}
