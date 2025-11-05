package com.example.gamelogic;

import com.example.engine.IGraphics;

enum TowerType {Rayo, Fuego, Hielo}

public class Tower {
    private IGraphics iGraphics;

    private float x;
    private float y;
    private int row;
    private int column;

    private float size;
    private TowerType type;
    private int damage;
    private int range;
    private int cooldown;
    private int cost;
    private boolean selected = false;

    private Cell cell;

    public Tower(IGraphics iGraphics, float x, float y, int row, int column, float size, int cost, TowerType type, MapGrid map, Cell cell) {
        this.iGraphics = iGraphics;
        this.row = row;
        this.column = column;
        this.size = size;
        this.cost = cost;
        this.type = type;
        this.cell = cell;

        this.x = cell.getX() + cell.getSize() / 2f;
        this.y = cell.getY() + cell.getSize() / 2f;

        switch (type) {
            case Rayo:
                this.damage = 10;
                this.range = 50;
                this.cooldown = 1;
                break;
            case Fuego:
                this.damage = 5;
                this.range = 75;
                this.cooldown = 2;
                break;
            case Hielo:
                this.damage = 7;
                this.range = 60;
                this.cooldown = 3;
                break;
        }
    }

    public void render() {
        float margin = size * 0.01f; // margen interno para no pegar al borde de la celda
        float available = size - (2 * margin);

        float centerX = x;
        float centerY = y;

        switch (type) {
            case Rayo: {
                iGraphics.setColor(0xFF0000FF); // azul

                // Usamos el tamaño disponible dentro de la celda
                float side = available * 0.8f; // longitud del lado del triángulo
                float height = (float) (Math.sqrt(3) / 2 * side); // altura del triángulo equilátero

                float cx = centerX;
                float cy = centerY;

                // Coordenadas de los 3 vértices
                float x1 = cx;          // vértice superior
                float y1 = cy - height / 2f;

                float x2 = cx - side / 2f; // base izquierda
                float y2 = cy + height / 2f;

                float x3 = cx + side / 2f; // base derecha
                float y3 = cy + height / 2f;

                iGraphics.fillTriangle(x1, y1, x2, y2, x3, y3);
                break;
            }
            case Fuego: {
                iGraphics.setColor(0xFFFF0000);
                float radius = available / 2f;
                iGraphics.fillHexagon(centerX, centerY, radius);
                break;
            }

            case Hielo: {
                iGraphics.setColor(0xFF8000FF);
                float side = available;
                iGraphics.fillRectangle(centerX - side / 2f, centerY - side / 2f, side, side);
                break;
            }
        }

        // Dibuja el rango si está seleccionada
        if (selected) {
            iGraphics.setColor(0xFF000000); // negro
            iGraphics.drawCircle(x, y, range);
        }

    }


    public void attack(){
        switch(type){
            case Rayo:
                break;
            case Fuego:
                break;
            case Hielo:
                break;
        }
    }

    public void upgrade_dmg(){
        this.damage += 5;
    }

    public void upgrade_range(){
        this.range += 50;
    }

    public void upgrade_cooldown(){
        this.cooldown -= 0.15f;
    }

    public int get_dmg() {
        return this.damage;
    }

    public int get_range() {
        return this.range;
    }

    public int get_cooldown(){
        return this.cooldown;
    }

    public Cell get_cell(){
        return this.cell;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isTouched(float touchX, float touchY) {
        float cellX = cell.getX();
        float cellY = cell.getY();
        float cellSize = cell.getSize();

        // Verifica si el toque está dentro del área de la celda
        return (touchX >= cellX && touchX <= cellX + cellSize &&
                touchY >= cellY && touchY <= cellY + cellSize);
    }
}
