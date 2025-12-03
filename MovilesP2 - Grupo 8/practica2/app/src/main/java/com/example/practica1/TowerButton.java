package com.example.practica1;

import com.example.engine.IFont;
import com.example.engine.IGraphics;

/*
* TowerButton implementa los botones de torres.
 */

public class TowerButton {

    // Gráficos.
    private IGraphics iGraphics;

    // Fuente.
    private IFont iFont;

    // Texto.
    private String text;

    // Coordenadas, ancho y alto.
    private float x, y, width, height;

    // Variables.
    private int buttonColor, textColor, cost;

    // ¿Está seleccionado? (sí/no).
    private boolean selected;

    // Tipo de torre.
    private TowerType tipo;

    // CONSTRUCTORA
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

    // RENDERIZADO
    public void render() {
        iGraphics.setColor(buttonColor);
        iGraphics.fillRoundRectangle(x, y, width, height, 5);


        float textHeight = height * 0.25f;
        float imageHeight = height - textHeight;


        float marginX = width * 0.1f;
        float marginY = imageHeight * 0.1f;


        float availableWidth = width - (2 * marginX);
        float availableHeight = imageHeight - (2 * marginY);


        float drawX = x + (width / 2);
        float drawY = y + (imageHeight / 2);


        switch (tipo) {
            case Rayo:
                iGraphics.setColor(0xFF000000);
                float halfBase = availableWidth / 2;
                float halfHeight = availableHeight / 2;

                float x1 = drawX;
                float y1 = drawY - halfHeight;
                float x2 = drawX - halfBase;
                float y2 = drawY + halfHeight;
                float x3 = drawX + halfBase;
                float y3 = drawY + halfHeight;

                iGraphics.fillTriangle(x1, y1, x2, y2, x3, y3);
                break;
            case Hielo:
                iGraphics.setColor(0xFF88539E);

                float side = Math.min(availableWidth, availableHeight);

                float squareX = drawX - (side / 2);
                float squareY = drawY - (side / 2);

                iGraphics.fillRectangle(squareX, squareY, side, side);
                break;

            case Fuego:
                iGraphics.setColor(0xFFE1050F);
                float radius = Math.min(availableWidth, availableHeight) / 2;
                iGraphics.fillHexagon(drawX, drawY, radius);
                break;
        }


        if (text != null && iFont != null) {
            iGraphics.setColor(textColor);
            float textX = x + (width / 2);
            float textY = y + imageHeight + (textHeight / 2) + (iFont.getSize() / 3);
            iGraphics.drawText(iFont, text, textX, textY);
        }
    }

    // ¿Está seleccionado? (sí/no).
    public boolean isTouched(int touchX, int touchY) {
        float left = x;
        float top = y;
        float right = x + width;
        float bottom = y + height;

        return touchX >= left && touchX <= right && touchY >= top && touchY <= bottom;
    }


    // --------------------------SETTERS--------------------------

    // Selección / deselección.
    public void setSelected(boolean selected) {
        this.selected = selected;
        buttonColor = this.selected ? 0xFFD3D3D3 : 0xFFFFFFFF;
    }

    // --------------------------GETTERS--------------------------

    // Coste.
    public int getCost() {
        return this.cost;
    }

    // Tipo.
    public TowerType getTipo() {
        return this.tipo;
    }
}
