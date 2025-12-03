package com.example.androidengine;

import android.graphics.Bitmap;

import com.example.engine.IImage;

public class AndroidImage implements IImage
{
    // Bitmap de la imagen.
    private Bitmap bitmap;

    public AndroidImage(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    // Ancho de la imagen.
    @Override
    public int getWidth() { return this.bitmap.getWidth(); }
    // Alto de la imagen.
    @Override
    public int getHeight() { return this.bitmap.getHeight(); }
    // Bitmap de la imagen.
    protected Bitmap getImage() { return this.bitmap; }
}
