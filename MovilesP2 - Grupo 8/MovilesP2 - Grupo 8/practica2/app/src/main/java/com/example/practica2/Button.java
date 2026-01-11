package com.example.practica2;

import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IImage;

/**
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

    private TowerType tower;

    // Coordenadas, ancho y alto.
    private float x, y, width, height;

    // Color.
    private int color;

    private boolean isShopItem = false;
    private boolean selected = false;

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


    /**
     * CONSTRUCTORA (con imagen).
     * @param graphics
     * @param image
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Button(IGraphics graphics, IImage image, float x, float y, float width, float height)
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
     */
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

    /**
     * CONSTRUCTORA (torre e ítem).
     * @param graphics
     * @param type
     * @param x
     * @param y
     * @param width
     * @param height
     * @param isItem
     */
    public Button(IGraphics graphics, TowerType type, float x, float y, float width, float height, Boolean isItem)
    {
        iGraphics = graphics;
        tower = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isShopItem = isItem;
    }

    /**
     * Método de RENDERIZADO.
     */
    public void render() {
        if(isShopItem)
        {
            iGraphics.setColor(selected ? 0xFF00FF00 : 0xFF000000);

            iGraphics.drawRect(x - width/2, y - height/2, width, height);
        }
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

    /**
     * SETTERS.
     * @param s
     */
    public void setSelected(boolean s) { selected = s; }
}
