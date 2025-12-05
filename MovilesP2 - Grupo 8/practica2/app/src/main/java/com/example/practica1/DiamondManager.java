package com.example.practica1;

public class DiamondManager {

    // Número de monedas
    private static int coins = 0;
    // Número de monedas por nivel
    private static int coinsPerLevel = 10;

    // Añade una cantidad de monedas
    public static void addCoins(int coinAmount)
    {
        coins += coinAmount;
    }

    public static void setCoins(int coins) { DiamondManager.coins = coins; }

    public static int getCoins() { return coins; }
    public static int getCoinsPerLevel() { return coinsPerLevel; }

}
