package com.example.practica2;

import java.util.List;

public class LevelData {

    private List<Integer> bubblesToLaunch;
    private List<Integer> bubblesInBoard;

    private int numRows;
    private int world;
    private int level;
    private int score;

    // Información del nivel (burbujas a lanzar, burbujas en el tablero, número de filas, puntuación, mundo y nivel)
    public LevelData(List<Integer> bubblesToLaunch, int numRows, List<Integer> bubblesInBoard, int score, int world, int level)
    {
        this.bubblesToLaunch = bubblesToLaunch;
        this.numRows = numRows;
        this.bubblesInBoard = bubblesInBoard;
        this.score = score;
        this.world = world;

        this.level = (5 * (this.world - 1)) + level;
    }

    public List<Integer> getBubblesToLaunch() { return bubblesToLaunch; }
    public List<Integer> getBubblesInBoard() { return bubblesInBoard; }
    public int getNumRows() { return numRows; }
    public int getScore() { return score; }
    public int getLevel() { return level; }
    public int getWorld() { return world; }

}
