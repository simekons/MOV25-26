package com.example.gamelogic;

import com.example.engine.IGraphics;

public class Tower {
    private IGraphics iGraphics;

    private float x;
    private float y;
    private int row;
    private int column;

    private float size;

    // 0: RAYO, 1: FUEGO, 2: HIELO
    private int type;

    private int damage;
    private int range;
    private int cooldown;
    private int cost;

    private Cell cell;

    public Tower(IGraphics iGraphics, float x, float y, int row, int column, float size, int cost, int type, MapGrid map, Cell cell){
        this.iGraphics = iGraphics;
        this.row = row;
        this.column = column;
        this.size = size;
        this.cost = cost;
        this.type = type;

        this.cell = cell;

        int w = 900 / map.getColumns();

        this.x = w * column + w/2f;
        this.y = w * row + w/2f;

        switch(type){
            case 0: // RAYO
                this.damage = 10;
                this.range = 300;
                this.cooldown = 1;
                break;

            case 1: // FUEGO
                this.damage = 5;
                this.range = 300;
                this.cooldown = 2;
                break;

            case 2: // HIELO
                this.damage = 7;
                this.range = 300;
                this.cooldown = 3;
                break;
        }
    }

    public void render(){
        switch(this.type){
            case 0: // RAYO
                this.iGraphics.setColor(0xff0000FF);
                // this.iGraphics.drawTriangle();
                break;
            case 1: // FUEGO
                System.out.println("HGOLAWRIWODEI");
                this.iGraphics.setColor(0xffFF0000);
                this.iGraphics.drawHexagon(this.x, this.y, this.size);
                break;
            case 2: // HIELO
                this.iGraphics.setColor(0xffFF00FF);
                this.iGraphics.fillRectangle(this.x- this.size/2f, this.y- this.size/2f, this.size, this.size);
                break;
            default:
                break;
        }
    }

    public void attack(){
        switch(type){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }

    public void upgrade_dmg(){
        this.damage += 5;
    }

    public void upgrade_range(){
        this.range += 50;
    }

    public void upgrade_cooldown(){
        this.cooldown -= 0.15f;
    }

    public int get_dmg() {
        return this.damage;
    }

    public int get_range() {
        return this.range;
    }

    public int get_cooldown(){
        return this.cooldown;
    }

    public Cell get_cell(){
        return this.cell;
    }
}
