package com.example.androidengine;

import android.app.Activity;
import android.view.SurfaceView;

import com.example.engine.IAudio;
import com.example.engine.IEngine;
import com.example.engine.IGraphics;
import com.example.engine.IInput;
import com.example.engine.IScene;

import java.util.List;

import kotlin.jvm.Volatile;

public class AndroidEngine implements IEngine, Runnable {

    // SurfaceView.
    private SurfaceView surfaceView;
    // Gráficos en Android.
    private AndroidGraphics androidGraphics;
    // Input en Android.
    private AndroidInput androidInput;
    // Audio en Android.
    private AndroidAudio androidAudio;
    // Actividad principal.
    private Activity activity;
    // Hebra de renderizado.
    private Thread renderThread;
    // Escena actual.
    private IScene scene;
    // Booleano de ejecución.
    @Volatile
    private boolean running = false;

    public AndroidEngine(SurfaceView surfaceView, Activity a)
    {
        activity = a;
        this.surfaceView = surfaceView;
        this.androidInput = new AndroidInput();
        this.androidGraphics = new AndroidGraphics(surfaceView, a);
        this.androidAudio = new AndroidAudio(a);

        this.surfaceView.setOnTouchListener(this.androidInput);
    }

    // Método con el bucle principal.
    @Override
    public void run()
    {
        // Comprobamos que la hebra actual ejecutándose sea la del renderizado.
        if (renderThread != Thread.currentThread()) {
            // Evita que cualquiera que no sea esta clase llame a este Runnable en un Thread
            // Programación defensiva
            throw new RuntimeException("run() should not be called directly");
        }

        while(this.running && this.surfaceView.getWidth() == 0);

        // Espera activa.
        long lastFrameTime = System.nanoTime();
        // Informes de FPS
        long informePrevio = lastFrameTime;
        int frames = 0;


        // Bucle de juego principal.
        while (running) {
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
            this.androidGraphics.prepareFrame();

            scene.update((float)elapsedTime);
            scene.render();

            this.androidGraphics.endFrame();
        }
    }

    // Método que gestiona el input.
    public void handleInput()
    {
        // Siempre que haya escena.
        if(this.scene != null)
        {
            // Registra los eventos de input.
            List<IInput.TouchEvent> events = this.androidInput.getTouchEvents();

            for(IInput.TouchEvent e : events)
            {
                e.x = this.androidGraphics.realToLogicX(e.x);
                e.y = this.androidGraphics.realToLogicY(e.y);
            }
            this.scene.handleInput(events);
        }
    }

    // Método que gestiona la reanudación.
    public void onResume()
    {
        if (!this.running) {
            // Solo hacemos algo si no nos estábamos ejecutando ya
            // (programación defensiva)
            this.running = true;
            // Lanzamos la ejecución de nuestro método run() en un nuevo Thread.
            this.renderThread = new Thread(this);
            this.renderThread.start();
        }

    }

    // Método que gestiona el pausado.
    public void onPause()
    {
        if (this.running) {
            this.running = false;
            while (true) {
                try {
                    this.renderThread.join();
                    this.renderThread = null;
                    break;
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    // Gráficos de Android.
    @Override
    public IGraphics getGraphics() { return this.androidGraphics; }
    // Audio de Android.
    @Override
    public IAudio getAudio() { return this.androidAudio; }

    // Escena actual.
    @Override
    public void setScenes(IScene scene) { this.scene = scene; }
}
