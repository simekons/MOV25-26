package com.example.desktopengine;

import com.example.engine.IImage;

public class DesktopImage implements IImage
{
    // Imagen.
    java.awt.Image image;

    public DesktopImage(java.awt.Image image)
    {
        this.image = image;
    }

    // Ancho de la imagen.
    @Override
    public int getWidth() { return image.getWidth(null); }
    // Alto de la imagen.
    @Override
    public int getHeight() { return image.getHeight(null); }
    // Imagen.
    protected java.awt.Image getImage() { return image; }
}
