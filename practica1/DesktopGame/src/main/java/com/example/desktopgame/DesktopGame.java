package com.example.desktopgame;

import com.example.desktopengine.DesktopEngine;
import com.example.gamelogic.GameScene;
import com.example.gamelogic.MenuScene;

import javax.swing.JFrame;

public class DesktopGame {

    // Método de ejecución main.
    public static void main(String[] args)
    {
        boolean isDebug = java.lang.management.ManagementFactory. getRuntimeMXBean(). getInputArguments().toString().indexOf("jdwp") >= 0;

        // Crea la ventana.
        JFrame jFrame = new JFrame("Tower Defense");
        jFrame.setSize(1200, 800);
        // Crea el motor.
        DesktopEngine desktopEngine = new DesktopEngine(jFrame);
        desktopEngine.setLogicSize(600,400);

        GameScene gameScene = new GameScene(desktopEngine);
        if(isDebug){
            System.out.println("DEBUG");
        }
        else{
            System.out.println("!DEBUG");

        }

        // Crea la escena de menú y la asigna como escena actual.
        MenuScene menuScene = new MenuScene(desktopEngine);
        desktopEngine.setScenes(menuScene);
        // Ejecuta la aplicación.
        desktopEngine.run();
    }
}