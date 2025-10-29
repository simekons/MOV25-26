package com.example.desktopengine;

import com.example.engine.IFont;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

public class DesktopFont implements IFont
{
    // La fuente.
    private Font font;
    // El tamaño de la fuente.
    private float size;
    // Booleano que indica si está en negrita.
    private boolean isBold;
    // Booleano que indica si está en cursiva.
    private boolean isItalic;
    // Entero del estilo.
    private int style;

    // CONSTRUCTORA.
    public DesktopFont(Font font, float size, boolean bold, boolean italic) throws IOException, FontFormatException {
        this.font = font;
        this.size = size;
        isBold = bold;
        isItalic = italic;

        style = 0;
        if(isBold)
            style = Font.BOLD;
        else if(isItalic)
            style = Font.ITALIC;
    }

    // Tamaño de la fuente.
    @Override
    public int getSize() { return (int)this.size; }
    // La fuente.
    public Font getFont() { return this.font; }
    // El estilo de la fuente.
    public int getStyle() { return this.style; }
    // Si está en negrita o no.
    @Override
    public boolean isBold() { return isBold; }


    // Estilo de la fuente.
    public void setStyle(int style) { this.font = font.deriveFont(style, this.size); }
}
