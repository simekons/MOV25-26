package com.example.gamelogic;

import com.example.engine.IAudio;
import com.example.engine.IGraphics;
import com.example.engine.IScene;
import com.example.engine.ISound;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TowerHielo extends Tower {

    private final float slowFactor = 0.5f; // 50% de velocidad
    private Set<Enemy> slowedEnemies = new HashSet<>(); // enemigos que esta torre está ralentizando

    private IAudio iAudio;

    private ISound ice;

    public TowerHielo(IGraphics iGraphics, IAudio iAudio, int row, int column, float size, int cost, Cell cell) {
        super(iGraphics, row, column, size, cost, cell);
        this.damage = 7;
        this.range = 60;
        this.cooldown = 3f;

        this.iAudio = iAudio;

        this.ice = this.iAudio.newSound("music/ice.wav");
    }

    @Override
    public void render() {
        iGraphics.setColor(0xFF88539E);
        float side = size;
        iGraphics.fillRectangle(x - side / 2f, y - side / 2f, side, side);

        if (selected) {
            iGraphics.setColor(0xFF000000);
            iGraphics.drawCircle(x, y, range);
        }

        if (shotTimer > 0f && currentTarget != null) {
            iGraphics.setColor(0xFF0000FF);
            iGraphics.drawLine(x, y, currentTarget.getX(), currentTarget.getY(), 5);
        }
    }

    @Override
    public void update(float deltaTime, List<Enemy> enemies) {
        timeSinceLastShot += deltaTime;

        if (shotTimer > 0f) {
            shotTimer -= deltaTime;
            if (shotTimer < 0f) {
                shotTimer = 0f;
                currentTarget = null;
            }
        }

        Enemy target = null;

        // Restaurar velocidad de enemigos que ya no están en rango
        Set<Enemy> stillInRange = new HashSet<>();
        for (Enemy e : slowedEnemies) {
            if (!e.isActive()) continue;

            float dx = e.getX() - x;
            float dy = e.getY() - y;
            float dist = (float) Math.sqrt(dx*dx + dy*dy);

            if (dist <= range) {
                stillInRange.add(e); // siguen ralentizados
                if (target == null) target = e; // elegir primer enemigo como objetivo
            } else {
                e.setSpeedModifier(1f); // fuera de rango, velocidad normal
            }
        }
        slowedEnemies = stillInRange;

        // Revisar enemigos nuevos que entran en rango
        for (Enemy e : enemies) {
            if (!e.isActive() || slowedEnemies.contains(e)) continue;

            float dx = e.getX() - x;
            float dy = e.getY() - y;
            float dist = (float) Math.sqrt(dx*dx + dy*dy);

            if (dist <= range) {
                e.setSpeedModifier(slowFactor);
                slowedEnemies.add(e);
                if (target == null) target = e;
            }
        }

        // Disparo al primer enemigo dentro del rango
        if (timeSinceLastShot >= cooldown && target != null) {
            target.makeDamage(damage);
            currentTarget = target;
            shotTimer = 0.2f;
            timeSinceLastShot = 0f;
            this.iAudio.playSound(this.ice, false);
        }
    }
}
