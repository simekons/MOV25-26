package com.example.desktopengine;

import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IImage;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferStrategy;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class DesktopGraphics implements IGraphics
{
    // Ventana actual.
    private JFrame jFrame;
    // BufferStrategy.
    private BufferStrategy bufferStrategy;
    // Gráficos sobre los que se dibuja.
    private Graphics2D graphics2D;
    // Color en que se dibuja actualmente.
    private Color currentColor;

    // Variables de renderizado.
    private float logicWidth;
    private float logicHeight;
    private float scale;
    private float offsetX;
    private float offsetY;

    // String con la ruta de los archivos.
    private String assetsRoute = "assets/";

    // CONSTRUCTORA.
    DesktopGraphics(JFrame jFrame)
    {
        this.jFrame = jFrame;
        init();

        this.scale = 1;
        this.offsetX = 0;
        this.offsetY = 0;

        //Insets insets = this.jFrame.getInsets();

    }

    // Método que inicializa la ventana y las variables.
    public void init()
    {
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jFrame.setVisible(true);

        int intentos = 100;
        boolean created=false;
        while(intentos-- > 0&&!created) {
            try {
                jFrame.createBufferStrategy(2);
                created=true;
            }
            catch(Exception e) {  }
        } // while pidiendo la creación de la buffeStrategy
        if (intentos == 0) {
            System.err.println("No pude crear la BufferStrategy");
            return;
        }
        else {
            // En "modo debug" podríamos querer escribir esto.
            //System.out.println("BufferStrategy tras " + (100 - intentos) + " intentos.");
        }
        this.bufferStrategy = this.jFrame.getBufferStrategy();
        this.graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
    }

    // Método que prepara el frame antes de dibujar.
    public void prepareFrame()
    {
        this.graphics2D = (Graphics2D) this.bufferStrategy.getDrawGraphics();
        this.graphics2D.setPaintMode();
        this.graphics2D.getTransform();
        this.clear(0xffffffff);

        this.calculateTransform();
        this.translate(this.offsetX, this.offsetY);
        this.scale(this.scale);
    }

    // Método que finaliza el frame actual.
    public boolean endFrame()
    {
        this.graphics2D.dispose();

        if(this.bufferStrategy.contentsRestored())
            return true;

        this.bufferStrategy.show();
        return this.bufferStrategy.contentsLost();

    }

    // Método que calcula el posicionamiento de los elementos.
    private void calculateTransform()
    {
        Insets insets = this.jFrame.getInsets();

        float tempScaleX = (this.jFrame.getWidth() - insets.left - insets.right) / this.logicWidth;
        float tempScaleY = (this.jFrame.getHeight() - insets.top - insets.bottom) / this.logicHeight;

        this.scale = Math.min(tempScaleX, tempScaleY);

        this.offsetX = (this.jFrame.getWidth() - insets.left - insets.right - this.logicWidth * this.scale) / 2 + insets.left;
        this.offsetY = (this.jFrame.getHeight() - insets.top - insets.bottom - this.logicHeight * this.scale) / 2 + insets.top;
    }

    // Método que cambia la escala de dibujado.
    @Override
    public void scale(float scale) { this.graphics2D.scale(scale, scale); }

    // Método que cambia la posición del dibujado.
    @Override
    public void translate(float offsetX, float offsetY) { this.graphics2D.translate(offsetX, offsetY); }

    // Método que limpia la pantalla.
    @Override
    public void clear(int color) {
        this.graphics2D.setColor(new Color(color));
        this.graphics2D.fillRect(0, 0, jFrame.getWidth(), jFrame.getHeight());
    }

    // Método que carga una imagen.
    @Override
    public IImage loadImage(String path) {
        java.awt.Image img = null;
        try {
            img = ImageIO.read(new FileInputStream(assetsRoute + path));
        } catch (IOException exception) {
            System.out.println("NO HAY IMAGEN");
        }
        return new DesktopImage(img);
    }

    // Método que dibuja una imagen.
    @Override
    public void drawImage(IImage image, int x, int y, int width, int height) {
        java.awt.Image image1 = ((DesktopImage)image).getImage();
        this.graphics2D.drawImage(image1, x - width / 2, y - height / 2, width, height, null);
    }

    // Método que dibuja un rectángulo relleno.
    @Override
    public void fillRectangle(float cx, float cy, float width, float height) {

        graphics2D.fillRect((int)cx, (int)cy, (int)width, (int)height);
    }

    @Override
    public void drawRect(float cx, float cy, float width, float height){
        graphics2D.drawRect((int)cx, (int)cy, (int)width, (int)height);
    }

    // Método que dibuja un rectángulo relleno con los bordes redondeados.
    @Override
    public void fillRoundRectangle(float cx, float cy, float width, float height, float arc) {
        height -= cy;
        width -= cx;
        graphics2D.fillRoundRect((int)cx, (int)cy, (int)width, (int)height, (int)arc * 2, (int)arc * 2);
    }

    // Método que dibuja una línea.
    @Override
    public void drawLine(float initX, float initY, float endX, float endY, int width) {
        graphics2D.drawLine((int)initX, (int)initY, (int)endX, (int)endY);
    }

    // Método que dibuja un hexágono.
    @Override
    public void drawHexagon(float x, float y, float radius) {
        int sides = 6;
        int[] xPoints = new int[sides];
        int[] yPoints = new int[sides];

        for (int i = 0; i < sides; i++) {
            xPoints[i] = (int) (x + radius * Math.cos(Math.toRadians(60 * i - 30)));
            yPoints[i] = (int) (y + radius * Math.sin(Math.toRadians(60 * i - 30)));
        }

        graphics2D.drawPolygon(xPoints, yPoints, sides);
    }

    // Método que dibuja un círculo.
    @Override
    public void drawCircle(float cx, float cy, float radius) {
        cx -= radius;
        cy -= radius;
        graphics2D.fillOval((int)cx, (int)cy, (int)radius * 2, (int)radius * 2);
    }

    // Método que crea una fuente.
    @Override
    public IFont createFont(String fontFile, int size, boolean bold, boolean italic) {
        Font font = null;
        InputStream is;
        try{
            is = new FileInputStream(assetsRoute + fontFile);
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        }catch (Exception e)
        {

        }
        try {
            return new DesktopFont(font, size, bold, italic);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    // Método que asigna una fuente.
    @Override
    public void setFont(IFont _font) {
        this.graphics2D.setFont(((DesktopFont)_font).getFont());
    }

    // Método que renderiza el texto.
    @Override
    public void drawText(IFont iFont, String text, float x, float y) {
        DesktopFont font = (DesktopFont) iFont;
        font.setStyle(font.getStyle());
        this.setFont(font);

        FontMetrics metrics = this.graphics2D.getFontMetrics(font.getFont());
        int textWidth = metrics.stringWidth(text);
        float centerX = x - (textWidth / 2.0f);

        this.graphics2D.drawString(text, centerX, y);
    }

    // Método que hace la transformación en X.
    public int realToLogicX(int x){
        return (int)(x / scale)-(int)(offsetX / scale);
    }

    // Método que hace la transformación en Y.
    public int realToLogicY(int y){
        return (int)(y / scale)-(int)(offsetY / scale);
    }

    // Ancho de la ventana.
    @Override
    public int getWidth() { return this.jFrame.getWidth(); }
    // Alto de la ventana.
    @Override
    public int getHeight() { return this.jFrame.getHeight(); }
    // Ancho lógico.
    @Override
    public int getLogicWidth() { return (int)logicWidth; }
    // Alto lógico.
    @Override
    public int getLogicHeight() { return (int)logicHeight; }

    // Color en el que se dibuja.
    @Override
    public void setColor(int color) {
        this.currentColor = new Color(color);
        this.graphics2D.setColor(currentColor);
    }
    // Tamaño lógico.
    @Override
    public void setLogicSize(float w, float h) {
        this.logicWidth = w;
        this.logicHeight = h;
    }
}
