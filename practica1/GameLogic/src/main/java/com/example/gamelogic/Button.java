package com.example.gamelogic;

import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IImage;

public class Button {

    private IGraphics iGraphics;
    private IFont iFont;
    private IImage iImage;
    private String text;
    private float x, y, width, height;
    private int color;

    // Botón con texto
    public Button(IGraphics graphics, IFont font, float x, float y, float width, float height, String text, int color)
    {
        iGraphics = graphics;
        iFont = font;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.color = color;
    }

    // Botón con imagen
    public Button(IGraphics graphics, IImage image, float x, float y, float width, float height)
    {
        iGraphics = graphics;
        iImage = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render() {
        if (iImage != null)
            iGraphics.drawImage(iImage, (int) x, (int) y, (int) width, (int) height);
        else {
            iGraphics.setColor(color);
            iGraphics.fillRoundRectangle(x, y, width, height, 20);
            iGraphics.setColor(0xff000000);
            //iGraphics.drawText(iFont, text, x + ((width - x) / 2), y + ((height - y) / 2 + 20));
        }
    }

    public boolean isTouched(int touchX, int touchY)
    {
        return touchX >= x && touchX <= (x + (width - x)) && touchY >= y && touchY <= (y + (height - y));
    }

    public boolean imageIsTouched(int touchX, int touchY)
    {
        return touchX >= (x - width / 2) && touchX <= (x + width / 2) && touchY >= (y - height / 2) && touchY <= (y + height / 2);
    }

    public void setImage(IImage image)
    {
        this.iImage = image;
    }
}
