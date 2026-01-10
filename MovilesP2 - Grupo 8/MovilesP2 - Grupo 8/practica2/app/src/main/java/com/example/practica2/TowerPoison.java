package com.example.practica2;

import com.example.engine.IAudio;
import com.example.engine.IGraphics;
import com.example.engine.ISound;

import java.util.List;

/*
 * TowerPoison implementa la torre de veneno
 * */
public class TowerPoison extends Tower {

    // Audio.
    private IAudio iAudio;

    // Sonido.
    private ISound thunder;

    // CONSTRUCTORA
    public TowerPoison(IGraphics iGraphics, IAudio iAudio, int row, int column, float size, int cost, Cell cell) {
        super(iGraphics, row, column, size, cost, cell);
        this.damage = 3;
        this.range = 50;
        this.cooldown = 1f;

        this.iAudio = iAudio;

        this.thunder = this.iAudio.newSound("music/thunder.wav");
    }

    // RENDERIZADo
    @Override
    public void render() {
        float radius = size / 2f;
        iGraphics.setColor(0xFF00FF00);
        iGraphics.fillCircle(x, y, radius);

        if (selected) iGraphics.drawCircle(x, y, range);
        if (shotTimer > 0f && currentTarget != null)
        {
            iGraphics.setColor(0x8800FF00);
            iGraphics.drawLine(x, y, currentTarget.getX(), currentTarget.getY(), 2);
        }
    }

    // UPDATE
    @Override
    public void update(float deltaTime, List<Enemy> enemies) {
        timeSinceLastShot += deltaTime;

        if (shotTimer > 0f) {
            shotTimer -= deltaTime;
            if (shotTimer < 0f) shotTimer = 0f;
        }

        if (timeSinceLastShot >= cooldown) {
            for (Enemy e : enemies) {
                if (!e.isActive()) continue;

                float dx = e.getX() - x;
                float dy = e.getY() - y;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                if (dist <= range) {
                    e.putPoison(damage,3);
                    currentTarget = e;
                    shotTimer = 0.2f;
                    timeSinceLastShot = 0f;
                    this.iAudio.playSound(this.thunder, false);
                    break;
                }
            }
        }
    }
}
