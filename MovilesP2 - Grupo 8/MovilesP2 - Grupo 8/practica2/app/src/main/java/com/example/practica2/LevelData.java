package com.example.practica2;

import java.util.ArrayList;
import java.util.List;

public class LevelData {

    private ArrayList<String> waveTypes;
    private ArrayList<Integer> waveAmounts;

    private int reward;
    private ArrayList<String> road;
    private String levelBackground;

    private int world;
    private int level;
    private int score;

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

    public ArrayList<String> getWaveTypes() { return waveTypes; }
    public ArrayList<Integer> getWaveAmounts() { return waveAmounts; }

    public int getReward() { return reward; }
    public ArrayList<String> getRoad() { return road; }
    public String getLevelBackground() { return levelBackground; }

    public int getScore() { return score; }
    public int getWorld() { return world; }
    public int getLevel() { return level; }

}
