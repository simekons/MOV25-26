package com.example.practica1;

import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidengine.AndroidEngine;

public class MainActivity extends AppCompatActivity {

    // Motor de Android.
    private AndroidEngine androidEngine;

    // Método que inicia la app.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SurfaceView surfaceView = new SurfaceView(this);
        FrameLayout frameLayout = new FrameLayout(this);
        setContentView(surfaceView);
        androidEngine = new AndroidEngine(surfaceView,this);
        androidEngine.setLogicSize(600,400);
        MenuScene menuScene = new MenuScene(androidEngine);
        androidEngine.setScenes(menuScene);
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
        androidEngine.onPause();
    }

}