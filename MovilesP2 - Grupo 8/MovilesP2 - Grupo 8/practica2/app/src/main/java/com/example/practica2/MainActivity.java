package com.example.practica2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.os.Build;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.androidengine.AndroidAds;
import com.example.androidengine.AndroidEngine;
import com.google.android.gms.ads.AdView;

/**
 * MainActivity es la actividad principal.
 */
public class MainActivity extends AppCompatActivity {

    // Motor de Android.
    private AndroidEngine androidEngine;

    /**
     * Método que inicia la app.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Solicitamos permisos de notificaciones
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        boolean fromNotification =
                getIntent().getBooleanExtra("FROM_NOTIFICATION", false);

        if (fromNotification) {
            AndroidEngine.reset();
        }

        setContentView(R.layout.activity_main);

        // SurfaceView
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        FrameLayout frameLayout = new FrameLayout(this);
        androidEngine = AndroidEngine.Instance(surfaceView,this);
        androidEngine.getGraphics().setLogicSize(600,400);

        // AdView
        AndroidAds androidAds = androidEngine.getAds();
        AdView adView = findViewById(R.id.adView);

        androidAds.loadBannerAd(adView.getId());

        androidAds.loadRewardedAd("ca-app-pub-3940256099942544/5224354917");

        /*Log.d(
                "NOTIFICATION",
                "Permiso: " +
                        checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
        );
        */

        GameLoader gameLoader = new GameLoader(androidEngine.getFile());
        gameLoader.loadGenericData();

        MenuScene menuScene = new MenuScene(gameLoader);
        androidEngine.setScenes(menuScene);
    }

    /**
     * Método que delega la gestión de la reanudación al motor.
     */
    @Override
    protected void onResume() {
        super.onResume();
        androidEngine.onResume();
    }

    /**
     * Método que delega la gestión del pausado al motor.
     */
    @Override
    protected void onPause() {
        super.onPause();
        androidEngine.programNotification(60L, "Vuelve", "Vuelve a jugar, ¡obtendrás recompensas!",R.drawable.ic_launcher_foreground,getPackageName());
        androidEngine.onPause();
    }

}