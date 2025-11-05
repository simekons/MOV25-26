package com.example.gamelogic;

import com.example.engine.IAudio;
import com.example.engine.IGraphics;
import com.example.engine.ISound;

import java.util.List;

public class TowerFuego extends Tower {

    private final float areaRadius = 30f; // radio del área de efecto
    private float explosionX = -1, explosionY = -1; // posición de la explosión

    private IAudio iAudio;
    private ISound fire;
    public TowerFuego(IGraphics iGraphics, IAudio iAudio, int row, int column, float size, int cost, Cell cell) {
        super(iGraphics, row, column, size, cost, cell);
        this.damage = 5;
        this.range = 75;
        this.cooldown = 2f;

        this.iAudio = iAudio;

        this.fire = this.iAudio.newSound("music/fire.wav");
    }

    @Override
    public void render() {
        // Renderiza la torre
        float radius = size / 2f;
        iGraphics.setColor(0xFFFF0000);
        iGraphics.fillHexagon(x, y, radius);

        // Renderiza el rango si está seleccionada
        if (selected) {
            iGraphics.setColor(0xFF000000);
            iGraphics.drawCircle(x, y, range);
        }

        // Renderiza la línea de disparo
        if (shotTimer > 0f && currentTarget != null) {
            iGraphics.setColor(0xFF0000FF);
            iGraphics.drawLine(x, y, currentTarget.getX(), currentTarget.getY(), 5);
        }

        // Renderiza el área de efecto en la posición de la explosión
        if (shotTimer > 0f && explosionX >= 0 && explosionY >= 0) {
            iGraphics.setColor(0x88FF0000); // rojo semitransparente
            iGraphics.drawCircle(explosionX, explosionY, areaRadius);
        }
    }

    @Override
    public void update(float deltaTime, List<Enemy> enemies) {
        timeSinceLastShot += deltaTime;

        if (shotTimer > 0f) {
            shotTimer -= deltaTime;
            if (shotTimer < 0f) {
                shotTimer = 0f;
                // Reinicia la posición de la explosión
                explosionX = -1;
                explosionY = -1;
                currentTarget = null;
            }
        }

        if (timeSinceLastShot >= cooldown) {
            for (Enemy e : enemies) {
                if (!e.isActive()) continue;

                float dx = e.getX() - x;
                float dy = e.getY() - y;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);

                if (dist <= range) {
                    // Daño al enemigo principal
                    e.makeDamage(damage);
                    currentTarget = e;
                    shotTimer = 0.2f;
                    timeSinceLastShot = 0f;

                    // Guardamos la posición de la explosión (donde estaba el enemigo)
                    explosionX = e.getX();
                    explosionY = e.getY();

                    // Daño en área a otros enemigos cercanos
                    for (Enemy other : enemies) {
                        if (other == e || !other.isActive()) continue;

                        float ddx = other.getX() - explosionX;
                        float ddy = other.getY() - explosionY;
                        float distanceToTarget = (float) Math.sqrt(ddx * ddx + ddy * ddy);

                        if (distanceToTarget <= areaRadius) {
                            other.makeDamage(damage);
                        }
                    }
                    this.iAudio.playSound(this.fire, false);
                    break; // ya atacó a un enemigo, salir del loop
                }
            }
        }
    }
}
