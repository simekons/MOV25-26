package com.example.engine;

public interface IGraphics
{
    // Escala el canvas
    public void scale(float scale);
    // Mueve el sistema de coordenadas
    public void translate(float offsetX, float offsetY);
    // Dibuja la pantalla de un color pasado por referencia
    public void clear(int color);
    // Carga una imagen en una ruta dada
    public IImage loadImage(String path);
    // Dibuja una imagen pasada por referencia en unas coordenadas y con un tamaño dado
    public void drawImage(IImage image,int x, int y, int width, int height);
    // Dibuja un rectángulo en unas coordenadas y con unas dimensiones dadas.
    public void fillRectangle(float cx, float cy, float width, float height);
    // Dibuja un rectángulo "redondeado" en unas coordenadas y con unas dimensiones dadas
    public void fillRoundRectangle(float cx, float cy, float width, float height, float arc);
    // Dibuja una línea
    public void drawLine(float initX, float initY, float endX, float endY, int width);
    // Dibuja un hexágono en unas coordenadas y con un tamaño dado
    public void drawHexagon(float x, float y, float radius);
    public void fillHexagon(float x, float y, float radius);
    // Dibuja un círculo en unas coordenadas y un con un tamaño dado
    public void drawCircle(float cx, float cy, float radius);
    public void fillCircle(float cx, float cy, float radius);
    public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3);
    // Crea una fuente con una ruta dada, tamaño y estilo
    public IFont createFont(String fontFile, int size, boolean bold, boolean italic);
    // Settea una fuente para renderizar
    public void setFont(IFont font);
    // Dibuja un texto en unas coordenadas dadas y con una fuente
    public void drawText(IFont font, String text, float x, float y);
    // Getter de la anchura del canvas
    int getWidth();
    // Getter de la altura del canvas
    int getHeight();
    // Getter de la anchura del tamaño lógico
    int getLogicWidth();
    // Getter de la altura del tamaño lógico
    int getLogicHeight();
    // Setter del color
    public void setColor(int color);
    // Setter del tamaño lógico
    public void setLogicSize(float w, float h);

    void drawRect(float cx, float cy, float width, float height);
}
