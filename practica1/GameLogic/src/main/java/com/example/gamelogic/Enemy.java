package com.example.gamelogic;

import com.example.engine.IGraphics;

public class Enemy {
    private IGraphics iGraphics;

    private int color;

    private float x;
    private float y;
    private float speed;
    private float radius;
    private boolean isActive;

    // enemy type

    public Enemy(IGraphics iGraphics, float speed, float radius, boolean isActive, MapGrid map) {
        this.iGraphics = iGraphics;
        this.speed = speed;
        this.radius = radius;
        this.isActive = isActive;

        // starting position
        int w = 900 / map.getColumns();
        this.x = map.getStartingPoint().getColumn() * w + w / 2f;
        this.y = map.getStartingPoint().getRow() * w + w / 2f;
    }

    public void render() {
        this.iGraphics.setColor(0xFF00FF88);
        this.iGraphics.drawCircle(x, y, radius);
    }

    public void movement(float delta) {
        this.x += (speed * delta);
    }
}
