package com.example.practica2;

import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidImage;
import com.example.androidengine.AndroidSound;

import java.util.List;

// Enumerador con las posibles resistencias de un enemigo.
enum EnemyResist { Nada, Rayo, Hielo, Fuego }

/**
 * Enemy implementa los enemigos.
 */
public class Enemy {

    // Gráficos.
    private AndroidGraphics graphics;

    // Audio.
    private AndroidAudio iAudio;

    // Sonido de muerte.
    private AndroidSound death;

    private AndroidImage image;

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

    private int poison;
    private float poisonTimer = 0; // acumula delta para el tick
    private int poisonTicksRemaining = 0; // cuántos ticks quedan por aplicar

    private float stunTimeRemaining = 0f;

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

    /**
     * CONSTRUCTORA.
     * @param graphics
     * @param audio
     * @param image
     * @param speed
     * @param radius
     * @param isActive
     * @param map
     * @param vida
     * @param defensa
     * @param type
     */
    public Enemy(AndroidGraphics graphics, AndroidAudio audio, AndroidImage image, float speed, float radius, boolean isActive, MapGrid map,
                 int vida, int defensa, EnemyResist type) {
        this.graphics = graphics;
        this.image = image;
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

        this.iAudio = audio;

        this.death = this.iAudio.newSound("music/death.wav");
    }

    /**
     * Método de RENDERIZADO.
     */
    public void render() {
        if (!isActive) return;

        if (this.image != null){
            this.graphics.drawImage(this.image, (int)x, (int)y, 20, 20);
        }
        else{
            this.graphics.setColor(color);
            this.graphics.fillCircle(x, y, radius);
        }
    }

    /**
     * Método de UPDATE.
     * @param delta
     */
    public void update(float delta) {
        if (!isActive || path.isEmpty() || currentTarget >= path.size()) return;

        // Aplicar poison por ticks de 0.5s
        if (poisonTicksRemaining > 0) {
            poisonTimer += delta;

            while (poisonTimer >= 0.5f && poisonTicksRemaining > 0) {
                poisonTimer -= 0.5f;

                vida -= poison; // aplicamos el daño del poison
                poisonTicksRemaining--;

                if (vida <= 0) {
                    this.iAudio.playSound(this.death, false);
                    this.isActive = false;
                    return;
                }
            }
        }

        // Actualizar STUN
        if (stunTimeRemaining > 0f) {
            stunTimeRemaining -= delta;

            if (stunTimeRemaining < 0f) {
                stunTimeRemaining = 0f;
            }

            // Si está stuneado, NO se mueve
            return;
        }

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

    /**
     * Método que gestiona recibir daño.
     * @param amount
     * @param resist
     */
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

    /**
     * Método que gestiona recibir veneno.
     * @param amount
     * @param ticksToApply
     */
    public void putPoison(int amount, int ticksToApply) {
        this.poison += amount;
        this.poisonTicksRemaining += ticksToApply;
    }

    /**
     * Método que paraliza a los enemigos.
     * @param time
     */
    public void putStun(float time) {
        this.stunTimeRemaining += time;
    }

    /**
     * Método que altera la velocidad.
     * @param slowFactor
     */
    public void setSpeedModifier(float slowFactor) {
        if(type == EnemyResist.Hielo) {
            slowFactor /= 2;
        }
        speedModifier = slowFactor;
    }

    /**
     * GETTERS.
     * @return
     */
    public boolean isActive() { return isActive; }
    public boolean reachedEnd() { return reachEnd; }
    public float getX() {return this.x;}
    public float getY() {return this.y;}




}
