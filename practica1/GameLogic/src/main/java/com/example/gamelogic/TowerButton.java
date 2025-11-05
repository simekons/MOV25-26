package com.example.gamelogic;

import com.example.engine.IFont;
import com.example.engine.IGraphics;

public class TowerButton {

    private IGraphics iGraphics;
    private IFont iFont;
    private String text;
    private float x, y, width, height;
    private int buttonColor, textColor;
    private int cost;
    private boolean selected;
    private TowerType tipo; // 0, 1 o 2

    public TowerButton(IGraphics graphics, IFont font,
                       float x, float y, float width, float height,
                       int cost, TowerType tipo, int buttonColor, int textColor) {
        this.iGraphics = graphics;
        this.iFont = font;
        this.cost = cost;
        this.tipo = tipo;
        this.text = "" + cost;
        this.x = x - (width / 2);
        this.y = y - (height / 2);
        this.width = width;
        this.height = height;
        this.buttonColor = buttonColor;
        this.textColor = textColor;
        this.selected = false;
    }

    public void render() {
        iGraphics.setColor(buttonColor);
        iGraphics.fillRoundRectangle(x, y, width, height, 5);

        // Altura reservada para el texto (25 % del alto total)
        float textHeight = height * 0.25f;
        float imageHeight = height - textHeight;

        // --- Margen del 10 % del botón ---
        float marginX = width * 0.1f;
        float marginY = imageHeight * 0.1f;

        // Área disponible para el dibujo (según tipo)
        float availableWidth = width - (2 * marginX);
        float availableHeight = imageHeight - (2 * marginY);

        // Dibujar torre según el tipo
        float drawX = x + (width / 2);
        float drawY = y + (imageHeight / 2);

        // Aquí puedes poner el renderizado que quieras según el tipo
        switch (tipo) {
            case Rayo:
                // Triángulo negro apuntando hacia arriba
                iGraphics.setColor(0xFF000000); // negro
                float halfBase = availableWidth / 2;
                float halfHeight = availableHeight / 2;

                // Coordenadas del triángulo
                float x1 = drawX;                 // vértice superior
                float y1 = drawY - halfHeight;
                float x2 = drawX - halfBase;      // base izquierda
                float y2 = drawY + halfHeight;
                float x3 = drawX + halfBase;      // base derecha
                float y3 = drawY + halfHeight;

                iGraphics.fillTriangle(x1, y1, x2, y2, x3, y3);
                break;
            case Hielo:
                iGraphics.setColor(0xFF88539E);

                // Hacer el cuadrado del tamaño máximo posible dentro del área disponible
                float side = Math.min(availableWidth, availableHeight);

                // Calcular la esquina superior izquierda para centrar el cuadrado
                float squareX = drawX - (side / 2);
                float squareY = drawY - (side / 2);

                // Dibujar el cuadrado
                iGraphics.fillRectangle(squareX, squareY, side, side);
                break;

            case Fuego:
                iGraphics.setColor(0xFFE1050F);
                float radius = Math.min(availableWidth, availableHeight) / 2;
                iGraphics.fillHexagon(drawX, drawY, radius);
                break;
        }

        // Dibujar texto debajo del dibujo
        if (text != null && iFont != null) {
            iGraphics.setColor(textColor);
            float textX = x + (width / 2);
            float textY = y + imageHeight + (textHeight / 2) + (iFont.getSize() / 3);
            iGraphics.drawText(iFont, text, textX, textY);
        }
    }

    public boolean isTouched(int touchX, int touchY) {
        float left = x;
        float top = y;
        float right = x + width;
        float bottom = y + height;

        return touchX >= left && touchX <= right && touchY >= top && touchY <= bottom;
    }

    public void changeButtonColor()
    {
        this.selected = !this.selected;
        if(selected)
            buttonColor = 0xFF800080;
        else
            buttonColor = 0xFFFFFFFF;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        buttonColor = this.selected ? 0xFFD3D3D3 : 0xFFFFFFFF;
    }

    public int getCost() {
        return this.cost;
    }

    public TowerType getTipo() {
        return this.tipo;
    }
}
