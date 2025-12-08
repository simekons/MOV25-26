package com.example.androidengine;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceView;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.engine.IAudio;
import com.example.engine.IEngine;
import com.example.engine.IGraphics;
import com.example.engine.IInput;
import com.example.engine.IScene;

import java.util.List;
import java.util.concurrent.TimeUnit;

import kotlin.jvm.Volatile;

public class AndroidEngine implements Runnable {

    // SurfaceView.
    private SurfaceView surfaceView;

    private static AndroidEngine _instance;
    // Gráficos en Android.
    private AndroidGraphics androidGraphics;
    // Input en Android.
    private AndroidInput androidInput;
    // Audio en Android.
    private AndroidAudio androidAudio;
    // Archivo.
    private AndroidFile androidFile;
    // Ads
    private AndroidAds androidAds;
    // Actividad principal.
    private Activity activity;
    // Hebra de renderizado.
    private Thread renderThread;
    // Escena actual.
    private IScene scene;
    // Booleano de ejecución.
    @Volatile
    private boolean running = false;
    private static String channelId = "Notification";


    public static AndroidEngine Instance(SurfaceView sw, Activity a){
        if (_instance == null){
            _instance = new AndroidEngine(sw, a);
        }
        return _instance;
    }
    public AndroidEngine(SurfaceView surfaceView, Activity a)
    {
        activity = a;
        this.surfaceView = surfaceView;
        this.androidInput = new AndroidInput();
        this.androidGraphics = new AndroidGraphics(surfaceView, a);
        this.androidAudio = new AndroidAudio(a);
        this.androidFile = new AndroidFile(a);
        this.androidAds = new AndroidAds(a);

        this.surfaceView.setOnTouchListener(this.androidInput);
    }

    public void showNotification(String title, String desc, int icon, String mainName){

        if (Build.VERSION. SDK_INT >= Build.VERSION_CODES. O) {
            CharSequence name = "notification";
            String description = "description";
            int importance = NotificationManager. IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance) ;
            channel.setDescription(description) ;
            NotificationManager notificationManager = (NotificationManager) activity.getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);//NotificationManagerCompat.from(getApplicationContext());
            notificationManager.createNotificationChannel(channel) ;
        }

        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(AndroidWorker.class)
                .setInputData(new Data.Builder()
                        .putString("title", title)
                        .putString("description", desc)
                        .putInt("notifications_icon", icon)
                        .putString("notifications_channel_id", channelId)
                        .putString("class",mainName)
                        .build())
                .setInitialDelay(5, TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(this.activity.getBaseContext()).enqueue(notificationWork);
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

    // Método para crear el hash
    public static native String createHash(String data);

    public static AndroidEngine get_instance() { return _instance;}
    // Gráficos de Android.
    public AndroidGraphics getGraphics() { return this.androidGraphics; }
    // Audio de Android.
    public AndroidAudio getAudio() { return this.androidAudio; }
    // Archivos
    public AndroidFile getFile() { return this.androidFile; }
    // Anuncios
    public AndroidAds getAds() { return this.androidAds; }
    // Escena actual.
    public void setScenes(IScene scene) { this.scene = scene; }

    public void setLogicSize(float x, float y) {
        androidGraphics.setLogicSize(x,y);
    }
}
