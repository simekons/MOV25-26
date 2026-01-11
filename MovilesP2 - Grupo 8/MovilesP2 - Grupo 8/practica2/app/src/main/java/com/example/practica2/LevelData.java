package com.example.practica2;

import java.util.ArrayList;
import java.util.List;

/**
 * LevelData guarda información del nivel.
 */
public class LevelData {

    // Variables de oleada.
    private ArrayList<String> waveTypes;
    private ArrayList<Integer> waveAmounts;

    // Recompensa.
    private int reward;

    // Camino.
    private ArrayList<String> road;

    // Fondo del nivel.
    private String levelBackground;

    // Variables del nivel.
    private int world;
    private int level;
    private int score;

    /**
     * CONSTRUCTORA.
     * @param waveTypes
     * @param waveAmounts
     * @param reward
     * @param road
     * @param levelBackground
     * @param score
     * @param world
     * @param level
     */
    public LevelData(ArrayList<String> waveTypes, ArrayList<Integer> waveAmounts, int reward, ArrayList<String> road,  String levelBackground, int score, int world,  int level)
    {
        this.waveTypes = waveTypes;
        this.waveAmounts = waveAmounts;
        this.reward = reward;
        this.road = road;
        this.levelBackground = levelBackground;
        this.score = score;
        this.world = world;
        this.level = level;
    }

    /**
     * GETTERS.
     */
    public ArrayList<Integer> getWaveAmounts() { return waveAmounts; }
    public int getReward() { return reward; }
    public ArrayList<String> getRoad() { return road; }
    public String getLevelBackground() { return levelBackground; }
    public int getWorld() { return world; }
    public int getLevel() { return level; }
}
