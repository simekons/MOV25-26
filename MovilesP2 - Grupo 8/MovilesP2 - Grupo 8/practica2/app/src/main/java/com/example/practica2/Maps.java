package com.example.practica2;

import java.util.ArrayList;

/*
 * Maps guarda la información de los niveles.
 */
public class Maps {

    // Filas.
    private int rows;

    // Columnas.
    private int cols;

    // Todas las filas concatenadas.
    private String map;

    private String background;

    private LevelData levelData;

    // CONSTRUCTORA
    public Maps() {
    }

    public Maps(LevelData levelData){
        this.levelData = levelData;

        createMap();
    }

    private void createMap(){
        ArrayList<String> road = this.levelData.getRoad();

        this.rows = road.size();
        this.cols = road.get(0).length(); // Siempre será cuadrado.

        StringBuilder builder = new StringBuilder();

        for (String row : road){
            builder.append(row);
        }

        this.map = builder.toString();

        this.background = this.levelData.getLevelBackground();
    }

    // Nivel 1.
    public static Maps level1() {
        Maps m = new Maps();
        m.rows = 8;
        m.cols = 15;
        m.map = "..............." +
                ".....#########." +
                ".....#.......#." +
                ".....######..#." +
                "..........#..#." +
                "###########..#." +
                ".............##" +
                "...............";
        return m;
    }

    public int getRows() { return this.rows;}
    public int getCols() { return this.cols; }
    public String getMap() { return this.map; }

    public String getBackground() { return this.background; }
}
