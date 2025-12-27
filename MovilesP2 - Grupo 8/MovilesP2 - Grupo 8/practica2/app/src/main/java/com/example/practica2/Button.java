package com.example.practica2;

import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IImage;

/*
 * Button implementa la funcionalidad de los botones.
 */
public class Button {

    // Gráficos.
    private IGraphics iGraphics;

    // Fuente.
    private IFont iFont;

    // Imagen de botón.
    private IImage iImage;

    // Texto de botón.
    private String text;

    // Coordenadas, ancho y alto.
    private float x, y, width, height;

    // Color.
    private int color;

    private boolean isShopItem = false;

    // CONSTRUCTORA (con texto)
    public Button(IGraphics graphics, IFont font, float x, float y, float width, float height, String text, int color)
    {
        iGraphics = graphics;
        iFont = font;
        this.x = x - (width/2);
        this.y = y - (height/2);
        this.width = width;
        this.height = height;
        this.text = text;
        this.color = color;
    }

    // CONSTRUCTORA (con imagen)
    public Button(IGraphics graphics, IImage image, float x, float y, float width, float height)
    {
        iGraphics = graphics;
        iImage = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Button(IGraphics graphics, IImage image, float x, float y, float width, float height, Boolean isItem)
    {
        iGraphics = graphics;
        iImage = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isShopItem = isItem;
    }

    // RENDERIZADO
    public void render() {
        if (iImage != null)
            iGraphics.drawImage(iImage, (int) x, (int) y, (int) width, (int) height);
        else if(isShopItem)
        {
            iGraphics.drawRect(x, y, width, height);
        }
        else {
            iGraphics.setColor(color);
            iGraphics.fillRoundRectangle(x, y, width, height, 5);
            iGraphics.setColor(0xff000000);
            iGraphics.drawText(iFont, text, x + (width / 2), y + (height / 2) + 5);
        }
    }

    // Botón pulsado (texto).
    public boolean isTouched(int touchX, int touchY)
    {
        float left = (iImage != null) ? (x - width / 2) : x;
        float top = (iImage != null) ? (y - height / 2) : y;
        float right = left + width;
        float bottom = top + height;

        return touchX >= left && touchX <= right && touchY >= top && touchY <= bottom;
    }

    // Botón pulsado (imagen).
    public boolean imageIsTouched(int touchX, int touchY)
    {
        return touchX >= (x - width / 2) && touchX <= (x + width / 2) && touchY >= (y - height / 2) && touchY <= (y + height / 2);
    }

    // --------------------------GETTERS--------------------------
    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    // --------------------------SETTERS--------------------------

    // Imagen de botón.
    public void setImage(IImage image) { this.iImage = image; }
}
