package com.example.androidengine;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IImage;

import java.io.IOException;
import java.io.InputStream;

public class AndroidGraphics implements IGraphics
{
    // SurfaceView.
    private SurfaceView surfaceView;
    // SurfaceHolder.
    private SurfaceHolder surfaceHolder;
    // Canvas sobre el que pintamos.
    private Canvas canvas;
    // Paint.
    private Paint paint;
    // AssetManager.
    private AssetManager assetManager;
    // Actividad principal.
    private Activity activity;

    // Variables de renderizado.
    private Rect rect;
    private Path path;

    private float logicWidth;
    private float logicHeight;
    private float scale;
    private float offsetX;
    private float offsetY;

    // CONSTRUCTORA
    public AndroidGraphics(SurfaceView surfaceView, Activity activity)
    {
        this.surfaceView = surfaceView;
        this.surfaceHolder = surfaceView.getHolder();
        this.paint = new Paint();
        this.paint.setColor(0xFF000000);
        this.activity = activity;
        this.assetManager = this.activity.getAssets();

        this.scale = 1;
        this.offsetX = 0;
        this.offsetY = 0;
        this.rect = new Rect();
        this.path = new Path();
    }

    // Método que prepara el frame antes de dibujar.
    protected void prepareFrame()
    {
        while (!this.surfaceView.getHolder().getSurface().isValid() );
        canvas = this.surfaceView.getHolder().lockCanvas();
        this.calculateTransform();
        this.translate(this.offsetX, this.offsetY);
        this.scale(this.scale);
        this.canvas.drawColor(0xFFFFFFFF);
    }

    // Método que finaliza el frame actual.
    protected void endFrame()
    {
        this.surfaceView.getHolder().unlockCanvasAndPost(canvas);
    }

    // Método que calcula el posicionamiento de los elementos.
    private void calculateTransform()
    {
        float tempScaleX = this.surfaceView.getWidth() / this.logicWidth;
        float tempScaleY = this.surfaceView.getHeight() / this.logicHeight;
        this.scale = Math.min(tempScaleX, tempScaleY);

        this.offsetX = (this.surfaceView.getWidth() - this.logicWidth * this.scale) / 2;
        this.offsetY = (this.surfaceView.getHeight() - this.logicHeight * this.scale) / 2;
    }

    // Método que cambia la escala del canvas.
    @Override
    public void scale(float scale) {
        this.canvas.scale(scale, scale);
    }

    // Método que cambia la posición del canvas.
    @Override
    public void translate(float offsetX, float offsetY) {
        this.canvas.translate(offsetX, offsetY);
    }

    // Método que limpia la escena.
    @Override
    public void clear(int color) {}

    // Método que carga una imagen.
    @Override
    public IImage loadImage(String path) {
        Bitmap bitmap = null;
        try
        {
            InputStream is = assetManager.open(path);
            bitmap = BitmapFactory.decodeStream(is);
        }
        catch (IOException exception)
        {

        }
        return new AndroidImage(bitmap);
    }

    // Método que dibuja una imagen.
    @Override
    public void drawImage(IImage image, int x, int y, int width, int height) {
        AndroidImage img = (AndroidImage)image;

        rect.top = y - height / 2;
        rect.left = x - width / 2;
        rect.bottom = y + height / 2;
        rect.right = x + width / 2;

        canvas.drawBitmap(img.getImage(), null, rect, this.paint);
    }

    // Método que dibuja un rectángulo relleno.
    @Override
    public void fillRectangle(float x, float y, float width, float height) {
        float left = x;
        float top = y;
        float right = x + width;
        float bottom = y + height;
        canvas.drawRect(left, top, right, bottom, this.paint);
    }


    // Método que dibuja un rectángulo relleno con los bordes redondeados.
    @Override
    public void fillRoundRectangle(float x, float y, float width, float height, float arc) {
        float left = x;
        float top = y;
        float right = x + width;
        float bottom = y + height;
        canvas.drawRoundRect(left, top, right, bottom, arc, arc, paint);
    }


    // Dibujo de rectángulo
    @Override
    public void drawRect(float x, float y, float width, float height) {
        Paint stroke = new Paint(paint);
        stroke.setStyle(Paint.Style.STROKE);
        float left = x;
        float top = y;
        float right = x + width;
        float bottom = y + height;
        canvas.drawRect(left, top, right, bottom, stroke);
    }

    // Método que dibuja una línea.
    @Override
    public void drawLine(float initX, float initY, float endX, float endY, int width) {
        float stroke = this.paint.getStrokeWidth();
        this.paint.setStrokeWidth(width);
        canvas.drawLine(initX, initY, endX, endY, this.paint);
        this.paint.setStrokeWidth(stroke);
    }

    // Método que dibuja un hexágono.
    @Override
    public void drawHexagon(float x, float y, float radius) {
        path.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        for (int i = 0; i < 6; i++) {
            float cx = (float) (x + radius * Math.cos(Math.toRadians(60 * i - 30)));
            float cy = (float) (y + radius * Math.sin(Math.toRadians(60 * i - 30)));
            if (i == 0) {
                path.moveTo(cx, cy);
            } else {
                path.lineTo(cx, cy);
            }
        }
        path.close();
        canvas.drawPath(path, paint);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void fillHexagon(float x, float y, float radius) {
        path.reset();
        for (int i = 0; i < 6; i++) {
            float cx = (float) (x + radius * Math.cos(Math.toRadians(60 * i - 30)));
            float cy = (float) (y + radius * Math.sin(Math.toRadians(60 * i - 30)));
            if (i == 0) {
                path.moveTo(cx, cy);
            } else {
                path.lineTo(cx, cy);
            }
        }
        path.close();
        canvas.drawPath(path, paint);
    }

    // Método que dibuja un círculo.
    @Override
    public void drawCircle(float cx, float cy, float radius) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawCircle(cx, cy, radius, this.paint);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void fillCircle(float cx, float cy, float radius) {
        canvas.drawCircle(cx, cy, radius, this.paint);
    }

    @Override
    public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        path.reset();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.close();

        canvas.drawPath(path, paint);
    }


    // Método que crea una fuente.
    @Override
    public IFont createFont(String fontFile, int size, boolean bold, boolean italic) {

        Typeface typeface = Typeface.createFromAsset(assetManager, fontFile);
        return new AndroidFont(typeface, size, bold, italic);
    }

    // Método que asigna una fuente.
    @Override
    public void setFont(IFont font) {
        this.paint.setTypeface(((AndroidFont)font).getFont());
    }

    // Método que renderiza texto.
    @Override
    public void drawText(IFont iFont, String text, float x, float y) {
        AndroidFont font = (AndroidFont)iFont;
        font.setStyle(font.getStyle());
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(font.getSize());
        paint.setTypeface(font.getFont());

        canvas.drawText(text, x, y, paint);
    }

    // Método que hace la transformación en X.
    public int realToLogicX(int x){
        return (int)(x / scale)-(int)(offsetX / scale);
    }

    // Método que hace la transformación en Y.
    public int realToLogicY(int y){
        return (int)(y / scale)-(int)(offsetY / scale);
    }

    @Override
    public int getWidth(){ return this.surfaceView.getWidth(); }
    // Alto de la pantalla.
    @Override
    public int getHeight(){ return this.surfaceView.getHeight(); }
    // Ancho lógico.
    @Override
    public int getLogicWidth(){ return (int)logicWidth; }
    // Alto lógico.
    @Override
    public int getLogicHeight(){ return (int)logicHeight; }

    // Color del Paint.
    @Override
    public void setColor(int color) { this.paint.setColor(color); }
    // Tamaño lógico.
    @Override
    public void setLogicSize(float w, float h) {
        this.logicWidth = w;
        this.logicHeight = h;
    }
}
