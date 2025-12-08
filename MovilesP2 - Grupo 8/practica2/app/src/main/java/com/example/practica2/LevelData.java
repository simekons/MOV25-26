package com.example.practica2;

import java.util.List;

public class LevelData {

    private List<String> waveTypes;
    private List<Integer> waveAmounts;

    private int reward;
    private List<String> road;
    private String levelBackground;

    private int world;
    private int level;
    private int score;

    public LevelData(List<String> waveTypes, List<Integer> waveAmounts, int reward, List<String> road,  String levelBackground, int score, int world,  int level)
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

    public List<String> getWaveTypes() { return waveTypes; }
    public List<Integer> getWaveAmounts() { return waveAmounts; }

    public int getReward() { return reward; }
    public List<String> getRoad() { return road; }
    public String getLevelBackground() { return levelBackground; }

    public int getScore() { return score; }
    public int getWorld() { return world; }
    public int getLevel() { return level; }

}
