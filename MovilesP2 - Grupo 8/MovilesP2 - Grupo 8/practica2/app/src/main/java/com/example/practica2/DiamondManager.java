package com.example.practica2;

public class DiamondManager {

    // Número de monedas
    private static int diamonds = 0;
    // Número de monedas por nivel
    private static int diamondsPerLevel = 10;

    // Añade una cantidad de monedas
    public static void addDiamonds(int diamondAmount)
    {
        diamonds += diamondAmount;
    }

    public static void setDiamonds(int diamonds) { DiamondManager.diamonds = diamonds; }

    public static int getDiamonds() { return diamonds; }
    public static int getDiamondsPerLevel() { return diamondsPerLevel; }

}
