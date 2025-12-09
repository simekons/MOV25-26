package com.example.practica2;

import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidengine.AndroidAds;
import com.example.androidengine.AndroidEngine;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    // Motor de Android.
    private AndroidEngine androidEngine;

    // Método que inicia la app.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        FinalScene finalScene = new FinalScene(0, true);
        androidEngine.setScenes(finalScene);
        /*MenuScene menuScene = new MenuScene();
        androidEngine.setScenes(menuScene);*/
    }

    // Método que delega la gestión de la reanudación al motor.
    @Override
    protected void onResume() {
        super.onResume();
        androidEngine.onResume();
    }

    // Método que delega la gestión del pausado al motor.
    @Override
    protected void onPause() {
        super.onPause();
        androidEngine.showNotification("Vuelve", "Vuelve a jugar, ¡obtendrás recompensas!",R.drawable.ic_launcher_foreground,getPackageName());
        androidEngine.onPause();
    }

}