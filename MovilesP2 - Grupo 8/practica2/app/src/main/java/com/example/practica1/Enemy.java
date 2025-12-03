package com.example.practica1;

import com.example.engine.IAudio;
import com.example.engine.IGraphics;
import com.example.engine.ISound;

import java.util.List;

// Enumerador con las posibles resistencias de un enemigo.
enum EnemyResist { Nada, Rayo, Hielo, Fuego }

/*
 * Enemy implementa los enemigos y sus funcionalidades.
 */
public class Enemy {

    // Gráficos.
    private IGraphics iGraphics;

    // Audio.
    private IAudio iAudio;

    // Sonido de muerte.
    private ISound death;

    // Color de enemigo.
    private int color;

    // Coordenadas.
    private float x, y;

    // Velocidad.
    private float speed;

    // Tipo de enemigo.
    private EnemyResist type;

    // Vida del enemigo.
    private int vida;

    // Defensa del enemigo.
    private int defensa;

    // Radio (tamaño) del enemigo.
    private float radius;

    // Modificador de velocidad.
    private float speedModifier;

    // Está activo (sí/no).
    private boolean isActive;

    // Llega al fin (sí/no).
    private boolean reachEnd = false;

    // Camino.
    private List<float[]> path;   // Lista de posiciones (centros de celdas)

    // Destino actual.
    private int currentTarget;    // Índice del waypoint actual

    // CONSTRUCTORA
    public Enemy(IGraphics iGraphics, IAudio iAudio, float speed, float radius, boolean isActive, MapGrid map,
                 int vida, int defensa, EnemyResist type) {
        this.iGraphics = iGraphics;
        this.speed = speed;
        this.radius = radius;
        this.isActive = isActive;
        this.vida = vida;
        this.defensa = defensa;
        this.speedModifier = 1f;
        this.type = type;

        switch (type){
            case Nada:
                color = 0xFF009b3f;
                break;
            case Rayo:
                color = 0xFFFFFF00;
                break;
            case Fuego:
                color = 0xFFFF0000;
                break;
            case Hielo:
                color = 0xFF0000FF;
                break;
        }

        // Obtener la ruta del mapa
        this.path = map.getPathPositions();
        this.currentTarget = 0;

        if (!path.isEmpty()) {
            this.x = path.get(0)[0];
            this.y = path.get(0)[1];
        }

        this.iAudio = iAudio;

        this.death = this.iAudio.newSound("music/death.wav");
    }

    // RENDERIZADO
    public void render() {
        if (!isActive) return;
        this.iGraphics.setColor(color);
        this.iGraphics.fillCircle(x, y, radius);
    }

    // UPDATE
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
                isActive = false;
                reachEnd = true; // llegó al final
            }
            return;
        }

        dx /= distance;
        dy /= distance;

        // Avanzar hacia el objetivo
        x += dx * speed * delta * speedModifier;
        y += dy * speed * delta * speedModifier;
    }

    // Recibe daño.
    public void makeDamage(int amount, EnemyResist resist) {
        if(resist == type){
            amount = (amount * 3) / 4;
        }

        this.vida -= Math.max((amount - defensa), 0);

        if (this.vida <= 0) {
            this.iAudio.playSound(this.death, false);
            this.isActive = false;
        }
    }

    // Alterador de la velocidad.
    public void setSpeedModifier(float slowFactor) {
        if(type == EnemyResist.Hielo) {
            slowFactor /= 2;
        }
        speedModifier = slowFactor;
    }

    // --------------------------GETTERS--------------------------

    // ¿Está activo?
    public boolean isActive() { return isActive; }

    // ¿Ha llegado al final?
    public boolean reachedEnd() { return reachEnd; }

    // Coordenadas.
    public float getX() {return this.x;}
    public float getY() {return this.y;}




}
