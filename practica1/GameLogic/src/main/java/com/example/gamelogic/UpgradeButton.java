package com.example.gamelogic;

import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IImage;

/*
* UpgradeButton implementa los botones de mejora.
* */

public class UpgradeButton {

    // Gráficos.
    private IGraphics iGraphics;

    // Fuente.
    private IFont iFont;

    // Imagen.
    private IImage iImage;

    // Texto.
    private String text;

    // Coordenadas, ancho y alto.
    private float x, y, width, height;

    // Variables.
    private int buttonColor, textColor, cost;

    // ¿Está activo? (sí/no).
    private boolean active;

    // CONSTRUCTORA
    public UpgradeButton(IGraphics graphics, IFont font, IImage image,
                         float x, float y, float width, float height,
                         int cost, int buttonColor, int textColor) {
        this.iGraphics = graphics;
        this.iFont = font;
        this.iImage = image;
        this.cost = cost;
        this.text = "" + cost;
        this.x = x - (width / 2);
        this.y = y - (height / 2);
        this.width = width;
        this.height = height;
        this.buttonColor = buttonColor;
        this.textColor = textColor;
        this.active = true;
    }

    // RENDERIZADO
    public void render() {
        if(!active) return;

        iGraphics.setColor(buttonColor);
        iGraphics.fillRoundRectangle(x, y, width, height, 5);

        // Altura reservada para el texto (25 % del alto total)
        float textHeight = height * 0.25f;
        float imageHeight = height - textHeight;

        // --- Margen del 10 % del botón ---
        float marginX = width * 0.1f;
        float marginY = imageHeight * 0.1f;

        // Área disponible para la imagen dentro del botón
        float availableWidth = width - (2 * marginX);
        float availableHeight = imageHeight - (2 * marginY);

        // Dibujar imagen dentro del margen manteniendo proporciones
        if (iImage != null) {
            int originalWidth = iImage.getWidth();
            int originalHeight = iImage.getHeight();

            // Calcular factor de escala proporcional
            float scale = Math.min(availableWidth / originalWidth, availableHeight / originalHeight);

            // Dimensiones finales de la imagen escalada
            float finalWidth = originalWidth * scale;
            float finalHeight = originalHeight * scale;

            // Centrar imagen dentro del área disponible
            float imageX = x + (width / 2);
            float imageY = y + (imageHeight / 2);

            iGraphics.drawImage(iImage, (int) imageX, (int) imageY,
                    (int) finalWidth, (int) finalHeight);
        }

        // Dibujar texto debajo de la imagen
        if (text != null && iFont != null) {
            iGraphics.setColor(textColor);
            float textX = x + (width / 2);
            float textY = y + imageHeight + (textHeight / 2) + (iFont.getSize() / 3);
            iGraphics.drawText(iFont, text, textX, textY);
        }
    }

    // ¿Ha sido presionado? (sí/no).
    public boolean isTouched(int touchX, int touchY) {
        if(!active) return false;

        float left = x;
        float top = y;
        float right = x + width;
        float bottom = y + height;

        return touchX >= left && touchX <= right && touchY >= top && touchY <= bottom;
    }


    // --------------------------SETTERS--------------------------

    // Activación/desactivación.
    public void setActive(boolean a) { active = a; }

    // --------------------------GETTERS--------------------------

    // Coste
    public int getCost() {
        return this.cost;
    }

}
