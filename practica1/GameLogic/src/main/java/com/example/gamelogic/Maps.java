package com.example.gamelogic;

public class Maps {

    public int rows;
    public int cols;
    public String map; // contendrá todas las filas concatenadas

    public Maps() {
    }

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
