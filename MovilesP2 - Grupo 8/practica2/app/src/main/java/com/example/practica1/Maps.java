package com.example.practica1;

/*
 * Maps guarda la información de los niveles.
 */
public class Maps {

    // Filas.
    public int rows;

    // Columnas.
    public int cols;

    // Todas las filas concatenadas.
    public String map;

    // CONSTRUCTORA
    public Maps() {
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
}
