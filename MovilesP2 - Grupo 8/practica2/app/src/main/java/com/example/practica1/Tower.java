package com.example.practica1;

import com.example.engine.IGraphics;

import java.util.ArrayList;
import java.util.List;

// Tipo de torres.
enum TowerType {Rayo, Hielo, Fuego}

/*
* Tower implementa la clase padre de las torres.
*/
public abstract class Tower {
    // Gráficos.
    protected IGraphics iGraphics;

    // Coordenadas.
    protected float x, y;

    // Fila y columna.
    protected int row, column;

    // Tamaño.
    protected float size;

    // Daño, rango y cooldown.
    protected int damage, range;
    protected float cooldown;

    // Coste.
    protected int cost;

    // ¿Está seleccionado? (sí/no).
    protected boolean selected = false;

    // Celda.
    protected Cell cell;

    // Temporizadores.
    protected float timeSinceLastShot, shotTimer;

    // Objetivo actual.
    protected Enemy currentTarget;

    // Mejoras.
    protected boolean[] upgrades = new boolean[3];

    // CONSTRUCTORA
    public Tower(IGraphics iGraphics, int row, int column, float size, int cost, Cell cell) {
        this.iGraphics = iGraphics;
        this.row = row;
        this.column = column;
        this.size = size;
        this.cost = cost;
        this.cell = cell;

        this.x = cell.getX() + cell.getSize() / 2f;
        this.y = cell.getY() + cell.getSize() / 2f;

        upgrades[0] = false;
        upgrades[1] = false;
        upgrades[2] = false;
    }

    // RENDERIZADO
    public abstract void render();

    // UPDATE
    public abstract void update(float deltaTime, java.util.List<Enemy> enemies);

    // Mejoras actuales.
    public List<Integer> getActiveUpgrades() {
        List<Integer> active = new ArrayList<>();
        for (int i = 0; i < upgrades.length; i++) {
            if (upgrades[i]) active.add(i);
        }
        return active;
    }

    // --------------------------SETTERS--------------------------

    // Selección / deselección.
    public void setSelected(boolean selected) { this.selected = selected; }

    // --------------------------GETTERS--------------------------

    // ¿Está seleccionado? (sí/no).
    public boolean isTouched(float touchX, float touchY) {
        float cx = cell.getX();
        float cy = cell.getY();
        float s = cell.getSize();
        return touchX >= cx && touchX <= cx + s && touchY >= cy && touchY <= cy + s;
    }

    // Activación de mejora.
    public void activateUpgrade(int index) {
        switch (index){
            case 0:
                if(!upgrades[index]) damage *= 2;
                break;
            case 1:
                if(!upgrades[index]) range = (int)(range + range*0.25);
                break;
            case 2:
                if(!upgrades[index]) cooldown /= 2;
                break;
        }
        upgrades[index] = true;
    }
}