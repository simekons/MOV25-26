package com.example.practica2;

import com.example.androidengine.AndroidFont;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidImage;
import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IImage;

/**
 * Button implementa la funcionalidad de los botones.
 */
public class Button {

    // Gráficos.
    private AndroidGraphics iGraphics;

    // Fuente.
    private AndroidFont iFont;

    // Imagen de botón.
    private AndroidImage iImage;

    // Texto de botón.
    private String text;

    private TowerType tower;

    // Coordenadas, ancho y alto.
    private float x, y, width, height;

    // Color.
    private int color;

    private boolean isShopItem = false;
    private boolean isPurchased = false;
    private boolean selected = false;
    private boolean stateSelected = false;

    private IImage imgLock = null;

    /**
     * CONSTRUCTORA (con texto).
     * @param graphics
     * @param font
     * @param x
     * @param y
     * @param width
     * @param height
     * @param text
     * @param color
     */
    public Button(AndroidGraphics graphics, AndroidFont font, float x, float y, float width, float height, String text, int color)
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


    /**
     * CONSTRUCTORA (con imagen).
     * @param graphics
     * @param image
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Button(AndroidGraphics graphics, AndroidImage image, float x, float y, float width, float height)
    {
        iGraphics = graphics;
        iImage = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * CONSTRUCTORA (es ítem).
     * @param graphics
     * @param image
     * @param x
     * @param y
     * @param width
     * @param height
     * @param isItem
     * @param isPurchased
     */
    public Button(AndroidGraphics graphics, AndroidImage image, float x, float y, float width, float height, Boolean isItem, Boolean isPurchased)
    {
        iGraphics = graphics;
        iImage = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isShopItem = isItem;
        this.isPurchased = isPurchased;
        imgLock = iGraphics.loadImage("sprites/lock.png");
    }

    /**
     * CONSTRUCTORA (torre e ítem).
     * @param graphics
     * @param type
     * @param x
     * @param y
     * @param width
     * @param height
     * @param isItem
     * @param isPurchased
     */
    public Button(AndroidGraphics graphics, TowerType type, float x, float y, float width, float height, Boolean isItem, Boolean isPurchased)
    {
        iGraphics = graphics;
        tower = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isShopItem = isItem;
        this.isPurchased = isPurchased;
        imgLock = iGraphics.loadImage("sprites/lock.png");
    }

    /**
     * Método de RENDERIZADO.
     */
    public void render() {
        if (iImage != null)
            iGraphics.drawImage(iImage, (int) x, (int) y, (int) width-10, (int) height-10);
        else if(tower != null){
            switch (tower){
                case Star: {
                    float radius = width / 2f - 5;
                    iGraphics.setColor(0xFFFF0080);
                    iGraphics.fillStar(x, y, radius);
                    break;
                }
                case Stun: {
                    float radius = width / 2f - 5;
                    iGraphics.setColor(0xFF944D03);
                    iGraphics.fillOctagon(x, y, radius);
                    break;
                }
                case Poison: {
                    float radius = width / 2f - 5;
                    iGraphics.setColor(0xFF00FF00);
                    iGraphics.fillCircle(x, y, radius);
                    break;
                }
            }
        }
        else {
            iGraphics.setColor(color);
            iGraphics.fillRoundRectangle(x, y, width, height, 5);
            iGraphics.setColor(0xff000000);
            iGraphics.drawText(iFont, text, x + (width / 2), y + (height / 2) + 5);
        }
        if(isShopItem)
        {
            if(!isPurchased)
                iGraphics.drawImage(imgLock, (int)(x - (width / 2) + 8), (int)(y - (height / 2) + 8), 15,15);

            float left = x - width / 2;
            float top = y - height / 2;

            if (selected) {
                iGraphics.setColor(0xFF00FF00);
                iGraphics.drawRect(left, top, width, height);
            } else if (stateSelected) {
                iGraphics.setColor(0xFFFF0000);
                iGraphics.drawRect(left, top, width, height);
            } else {
                iGraphics.setColor(0xFF000000);
                iGraphics.drawRect(left, top, width, height);
            }
        }
    }

    /**
     * Método de botón (texto) pulsado.
     * @param touchX
     * @param touchY
     * @return
     */
    public boolean isTouched(int touchX, int touchY)
    {
        float left = (iImage != null) ? (x - width / 2) : x;
        float top = (iImage != null) ? (y - height / 2) : y;
        float right = left + width;
        float bottom = top + height;

        return touchX >= left && touchX <= right && touchY >= top && touchY <= bottom;
    }

    /**
     * Método de botón (imagen) pulsado.
     * @param touchX
     * @param touchY
     * @return
     */
    public boolean imageIsTouched(int touchX, int touchY)
    {
        return touchX >= (x - width / 2) && touchX <= (x + width / 2) && touchY >= (y - height / 2) && touchY <= (y + height / 2);
    }


    /**
     * GETTERS.
     * @return
     */
    public float getX() { return this.x; }
    public float getY() { return this.y; }

    public void setY(float y) { this.y = y;}
    /**
     * SETTERS.
     * @param s
     */
    public void setSelected(boolean s) { selected = s; }
    public void setStateSelected(boolean s) { stateSelected = s; }
    public void setPurchased(boolean p) { isPurchased = p; }
}
