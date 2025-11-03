package com.example.desktopengine;

import com.example.engine.IAudio;
import com.example.engine.IEngine;
import com.example.engine.IGraphics;
import com.example.engine.IInput;
import com.example.engine.IScene;

import java.util.List;

import javax.swing.JFrame;

public class DesktopEngine implements IEngine, Runnable
{
    // Gráficos de Desktop.
    private DesktopGraphics desktopGraphics;
    // Input de Desktop.
    private DesktopInput desktopInput;
    // Audio de Desktop.
    private DesktopAudio desktopAudio;
    // Escena actual.
    private IScene scene;
    // Ventana.
    private JFrame jFrame;
    // Booleano de ejecución.
    private boolean done = false;

    // CONSTRUCTORA.
    public DesktopEngine(JFrame jFrame)
    {
        this.jFrame = jFrame;
        desktopGraphics = new DesktopGraphics(jFrame);
        this.desktopInput = new DesktopInput();
        this.desktopAudio = new DesktopAudio();

        desktopGraphics.setLogicSize(900, 600);


        this.jFrame.addMouseListener(this.desktopInput);
        this.jFrame.addMouseMotionListener(this.desktopInput);
    }

    // Método con el bucle principal.
    @Override
    public void run() {
        //while(done);

        // Espera activa.
        long lastFrameTime = System.nanoTime();
        // Informes de FPS
        long informePrevio = lastFrameTime;
        int frames = 0;


        while (!done) {
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            // Informe de FPS
            double elapsedTime = (double) nanoElapsedTime / 1.0E9;
            if (currentTime - informePrevio > 1000000000l) {
                long fps = frames * 1000000000l / (currentTime - informePrevio);
                // System.out.println("" + fps + " fps");
                frames = 0;
                informePrevio = currentTime;
            }
            ++frames;
            // Gestionamos el input.
            handleInput();
            this.desktopGraphics.prepareFrame();

            scene.update((float)elapsedTime);
            scene.render();

            this.desktopGraphics.endFrame();
        }
    }

    // Método que gestiona el input.
    public void handleInput()
    {
        if(this.scene != null)
        {
            List<IInput.TouchEvent> events = this.desktopInput.getTouchEvents();

            for(IInput.TouchEvent e : events)
            {
                e.x = this.desktopGraphics.realToLogicX(e.x);
                e.y = this.desktopGraphics.realToLogicY(e.y);
            }
            this.scene.handleInput(events);
        }
    }


    // Gráficos en Desktop.
    @Override
    public IGraphics getGraphics() { return this.desktopGraphics; }
    // Audio en Desktop.
    @Override
    public IAudio getAudio() { return this.desktopAudio; }

    // Escena actual.
    @Override
    public void setScenes(IScene scene) { this.scene = scene; }
}