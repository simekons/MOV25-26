package com.example.gamelogic;

import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IImage;

public class UpgradeButton {

    private IGraphics iGraphics;
    private IFont iFont;
    private IImage iImage;
    private String text;
    private float x, y, width, height;
    private int buttonColor, textColor;
    private int cost;

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

    public boolean isTouched(int touchX, int touchY) {
        float left = x;
        float top = y;
        float right = x + width;
        float bottom = y + height;

        return touchX >= left && touchX <= right && touchY >= top && touchY <= bottom;
    }

    public void setImage(IImage image) {
        this.iImage = image;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    public int getCost() {
        return this.cost;
    }
}
