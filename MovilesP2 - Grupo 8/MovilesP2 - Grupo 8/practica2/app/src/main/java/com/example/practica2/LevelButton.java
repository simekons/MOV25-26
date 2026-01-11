package com.example.practica2;

import com.example.androidengine.AndroidFont;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidImage;

/**
 * LevelButton es la clase que implementa los botones de niveles.
 */
public class LevelButton
{
    // Gráficos.
    private AndroidGraphics graphics;

    // Fuentes.
    private AndroidFont font;

    // Imágenes.
    private AndroidImage image;

    // Estado de los niveles.
    private AdventureScene.LevelState levelState;

    // Texto.
    private String text;

    // Variables de los botones
    private int color;
    private int textColor;
    private int level;
    private float x, y, originalY, width, height;

    // Mundo actual.
    private int world;

    /**
     * CONSTRUCTORA (desbloqueados).
     * @param graphics
     * @param font
     * @param x
     * @param y
     * @param width
     * @param height
     * @param text
     * @param color
     * @param textColor
     * @param levelState
     * @param world
     * @param level
     */
    public LevelButton(AndroidGraphics graphics, AndroidFont font, float x, float y, float width, float height, String text, int color, int textColor, AdventureScene.LevelState levelState, int world, int level)
    {
        this.graphics = graphics;
        this.font = font;
        this.x = x;
        this.y = y;
        this.originalY = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.color = color;
        this.textColor = textColor;
        this.levelState = levelState;
        this.world = world;
        this.level = level;
    }

    /**
     * CONSTRUCTORA (bloqueados).
      * @param graphics
     * @param image
     * @param x
     * @param y
     * @param width
     * @param height
     * @param color
     * @param levelState
     * @param world
     * @param level
     */
    public LevelButton(AndroidGraphics graphics, AndroidImage image, float x, float y, float width, float height, int color, AdventureScene.LevelState levelState, int world, int level)
    {
        this.graphics = graphics;
        this.image = image;
        this.x = x;
        this.y = y;
        this.originalY = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.levelState = levelState;
        this.world = world;
        this.level = level;
    }

    /**
     * Método de RENDERIZADO.
     */
    public void render()
    {
        graphics.setColor(color);
        graphics.fillRectangle(x, y, width, height);
        graphics.setColor(0xff000000);
        graphics.drawRect(x, y, width, height);
        if (image != null)
            graphics.drawImage(image, (int) (x + (width) / 2), (int) (y + (height) / 2), (int) (width) / 2, (int) (height) / 2);
        else {
            graphics.setColor(textColor);
            graphics.drawText(font, text, x + ((width) / 2), y + ((height) / 2 + 20));
        }
    }

    /**
     * GETTERS.
     */
    public boolean isTouched(int touchX, int touchY) { return touchX >= x && touchX <= (x + width) && touchY >= y && touchY <= (y + height); }
    public AdventureScene.LevelState getLevelState() { return this.levelState; }
    public int getLevel() { return this.level; }
    public int getWorld() { return this.world; }
    public float getY() { return this.y; }
    public float getOriginalY() { return originalY; }

    /**
     * SETTERS.
     */
    public void setColor(int color) { this.color = color; }
    public void setY(float newY) { this.y = newY; }
}
