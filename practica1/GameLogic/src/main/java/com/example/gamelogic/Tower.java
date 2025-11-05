package com.example.gamelogic;

import com.example.engine.IGraphics;

enum TowerType {Rayo, Hielo, Fuego}

public abstract class Tower {
    protected IGraphics iGraphics;
    protected float x, y;
    protected int row, column;
    protected float size;
    protected int damage;
    protected int range;
    protected float cooldown;
    protected int cost;
    protected boolean selected = false;

    protected Cell cell;
    protected float timeSinceLastShot;
    protected float shotTimer;
    protected Enemy currentTarget;

    public Tower(IGraphics iGraphics, int row, int column, float size, int cost, Cell cell) {
        this.iGraphics = iGraphics;
        this.row = row;
        this.column = column;
        this.size = size;
        this.cost = cost;
        this.cell = cell;

        this.x = cell.getX() + cell.getSize() / 2f;
        this.y = cell.getY() + cell.getSize() / 2f;
    }

    public abstract void render();
    public abstract void update(float deltaTime, java.util.List<Enemy> enemies);

    public void setSelected(boolean selected) { this.selected = selected; }

    public boolean isTouched(float touchX, float touchY) {
        float cx = cell.getX();
        float cy = cell.getY();
        float s = cell.getSize();
        return touchX >= cx && touchX <= cx + s && touchY >= cy && touchY <= cy + s;
    }
}
