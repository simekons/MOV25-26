package com.example.practica2;

import com.example.androidengine.AndroidFont;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidImage;

public class ShopItem {


    private AndroidGraphics iGraphics;

    private AndroidFont iFont;
    private AndroidImage lockImage;
    private AndroidImage coinImage;
    private AndroidImage fruitImage;

    private String text;

    private int color, selectedColor;
    private int prize;
    private int colorCircle;
    private float x, y, width, height;
    private boolean locked;
    private boolean selected;

    // Constructora de los items de la primera fila
    public ShopItem(AndroidGraphics iGraphics, AndroidFont font, AndroidImage lockImage, AndroidImage coinImage, float x, float y, float width, float height, int color, int prize, boolean locked)
    {
        this.iGraphics = iGraphics;
        this.lockImage = lockImage;
        this.coinImage = coinImage;
        this.iFont = font;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.selectedColor = 0xff000000;
        this.prize = prize;
        this.locked = locked;
        this.selected = false;
    }

    // Constructora de los items de la segunda fila
    public ShopItem(AndroidGraphics iGraphics, AndroidFont font, AndroidImage fruitImage, AndroidImage lockImage, AndroidImage coinImage, float x, float y, float width, float height, int color, int prize, boolean locked)
    {
        this.iGraphics = iGraphics;
        this.lockImage = lockImage;
        this.coinImage = coinImage;
        this.fruitImage = fruitImage;
        this.iFont = font;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = 0xffffffff;
        this.selectedColor = 0xff000000;
        this.colorCircle = color;
        this.prize = prize;
        this.locked = locked;
        this.selected = false;
    }

    public void render()
    {
        iGraphics.setColor(color);
        iGraphics.fillRoundRectangle(x, y, width + x, height + y, 8);
        // Si está seleccionado, dibuja el recuadro de otro color
        if(selected)
            iGraphics.setColor(0xff69f555);
        else
            iGraphics.setColor(0xff000000);
        iGraphics.drawRect(x, y, width + x, height + y);
        // Si está bloqueado dibuja el candado y el precio
        if(locked)
        {
            iGraphics.drawImage(lockImage, (int) (x + (width / 5)), (int) (y + (height / 4)), (int) (width / 3) * 2, (int) (height / 3) * 2);
            iGraphics.drawImage(coinImage, (int) (x + 10), (int) (y + height + 20), (int)width / 3, (int)height / 3);
            iGraphics.drawText(iFont, String.valueOf(prize),x + ((width / 3) * 2) , (y + height + 40));
        }
        if (fruitImage != null)
        {
            iGraphics.setColor(colorCircle);
            iGraphics.drawCircle(x + (width / 4), (y + (height / 4) * 3), width / 5);
            iGraphics.drawImage(fruitImage, (int) (x + ((width / 3) * 2)), (int) ((y + (height) / 2) - 5), (int) (width) / 2, (int) (height) / 2);
        }
    }

    public boolean isTouched(int touchX, int touchY)
    {
        return touchX >= x && touchX <= (x + width) && touchY >= y && touchY <= (y + height);
    }

    public int getPrize() {return prize; }
    public int getColor() { return color; }
    public float getX() { return x; }
    public float getY() { return y; }
    public boolean getState() { return locked; }
    public boolean isSelected() { return selected; }

    public void setSelectedColor(int selectedColor) { this.selectedColor = selectedColor; }
    public void setState(boolean locked) { this.locked = locked; }
    public void setSelection(boolean selected) { this.selected = selected; }

}
