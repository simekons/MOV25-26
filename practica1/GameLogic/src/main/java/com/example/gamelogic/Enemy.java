package com.example.gamelogic;

import com.example.engine.IGraphics;
import java.util.List;

enum EnemyResist { Nada, Rayo, Hielo, Fuego }

public class Enemy {

    private IGraphics iGraphics;
    private int color;

    private float x;
    private float y;
    private float speed;
    private EnemyResist type;
    private int vida;
    private int defensa;
    private float radius;

    private float speedModifier;
    private boolean isActive;

    private List<float[]> path;   // Lista de posiciones (centros de celdas)
    private int currentTarget;    // Índice del waypoint actual

    public Enemy(IGraphics iGraphics, float speed, float radius, boolean isActive, MapGrid map) {
        this.iGraphics = iGraphics;
        this.speed = speed;
        this.radius = radius;
        this.isActive = isActive;
        this.vida = 30;
        this.defensa = 0;
        this.speedModifier = 1f;

        // Obtener la ruta del mapa
        this.path = map.getPathPositions();
        this.currentTarget = 0;

        if (!path.isEmpty()) {
            this.x = path.get(0)[0];
            this.y = path.get(0)[1];
        }
    }

    public void render() {
        if (!isActive) return;
        this.iGraphics.setColor(0xFF00FF00);
        this.iGraphics.fillCircle(x, y, radius);
    }

    public void update(float delta) {
        if (!isActive || path.isEmpty() || currentTarget >= path.size()) return;

        float targetX = path.get(currentTarget)[0];
        float targetY = path.get(currentTarget)[1];

        // Calcular dirección normalizada
        float dx = targetX - x;
        float dy = targetY - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < 1f) {
            // Llegó al siguiente punto
            currentTarget++;
            if (currentTarget >= path.size()) {
                isActive = false; // llegó al final
            }
            return;
        }

        dx /= distance;
        dy /= distance;

        // Avanzar hacia el objetivo
        x += dx * speed * delta * speedModifier;
        y += dy * speed * delta * speedModifier;
    }

    public boolean isActive() {
        return isActive;
    }

    public float getX() {return this.x;}
    public float getY() {return this.y;}

    public void makeDamage(int amount) {
        this.vida -= Math.max((amount - defensa), 0);
        if (this.vida <= 0) {
            this.isActive = false;
        }
    }

    public void setSpeedModifier(float slowFactor) {
        speedModifier = slowFactor;
    }
}
