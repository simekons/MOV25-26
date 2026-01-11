package com.example.androidengine;

import android.graphics.Typeface;

import com.example.engine.IFont;

/**
 * AndrroidFont gestiona las fuentes de Android.
 */
public class AndroidFont implements IFont
{
    // La fuente.
    private Typeface typeface;
    // Tamaño de la fuente.
    private float size;
    // Booleano que indica si está en negrita.
    private boolean bold;
    // Booleano que indica si está en cursiva.
    private boolean italic;
    // Entero del estilo.
    private int style;

    /**
     * CONSTRUCTORA.
     * @param typeface
     * @param size
     * @param bold
     * @param italic
     */
    public AndroidFont(Typeface typeface, float size, boolean bold, boolean italic)
    {
        this.typeface = typeface;
        this.size = size;
        this.bold = bold;
        this.italic = italic;

        style = 0;

        if(bold)
            style = Typeface.BOLD;
        else if(italic)
            style = Typeface.ITALIC;
        else if(bold && italic)
            style = Typeface.BOLD_ITALIC;
    }

    /**
     * GETTERS.
     * @return
     */
    // Tamaño de la fuente.
    @Override
    public int getSize() { return (int)this.size; }
    // La fuente.
    public Typeface getFont() { return this.typeface; }
    // El estilo de la fuente.
    public int getStyle() { return this.style; }
    // Si es negrita o no.
    @Override
    public boolean isBold() { return this.bold; }

    /**
     * SETTERS.
     * @param size
     */
    public void setSize(int size) {this.size = size; }
    // El estilo de la fuente.
    public void setStyle(int style) { this.typeface = Typeface.create(typeface, style); }
}
