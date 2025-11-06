package com.example.gamelogic;

import com.example.engine.IAudio;
import com.example.engine.IGraphics;
import com.example.engine.ISound;

import java.util.List;

/*
* TowerRayo implementa la torre de rayo.
* */
public class TowerRayo extends Tower {

    // Audio.
    private IAudio iAudio;

    // Sonido.
    private ISound thunder;

    // CONSTRUCTORA
    public TowerRayo(IGraphics iGraphics, IAudio iAudio, int row, int column, float size, int cost, Cell cell) {
        super(iGraphics, row, column, size, cost, cell);
        this.damage = 10;
        this.range = 50;
        this.cooldown = 1f;

        this.iAudio = iAudio;

        this.thunder = this.iAudio.newSound("music/thunder.wav");
    }

    // RENDERIZADo
    @Override
    public void render() {
        float margin = size * 0.01f;
        float side = (size - 2 * margin) * 0.8f;
        float height = (float) (Math.sqrt(3) / 2 * side);

        float x1 = x;
        float y1 = y - height / 2f;
        float x2 = x - side / 2f;
        float y2 = y + height / 2f;
        float x3 = x + side / 2f;
        float y3 = y + height / 2f;

        iGraphics.setColor(0xFF000000);
        iGraphics.fillTriangle(x1, y1, x2, y2, x3, y3);

        if (selected) iGraphics.drawCircle(x, y, range);
        if (shotTimer > 0f && currentTarget != null)
        {
            iGraphics.setColor(0xff47a8dc);
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
                    e.makeDamage(damage, EnemyResist.Rayo);
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
