package com.example.desktopgame;

import com.example.desktopengine.DesktopEngine;
import com.example.gamelogic.MenuScene;

import javax.swing.JFrame;

public class DesktopGame {

    // Método de ejecución main.
    public static void main(String[] args)
    {
        boolean isDebug = java.lang.management.ManagementFactory. getRuntimeMXBean(). getInputArguments().toString().indexOf("jdwp") >= 0;

        // Crea la ventana.
        JFrame jFrame = new JFrame("Puzzle Bobble");
        jFrame.setSize(800, 800);
        // Crea el motor.
        DesktopEngine DesktopEngine = new DesktopEngine(jFrame);

        if(isDebug){
            System.out.println("DEBUG");
        }
        else{
            System.out.println("!DEBUG");

        }

        // Crea la escena de menú y la asigna como escena actual.
        MenuScene menuScene = new MenuScene(DesktopEngine);
        DesktopEngine.setScenes(menuScene);
        // Ejecuta la aplicación.
        DesktopEngine.run();
    }
}