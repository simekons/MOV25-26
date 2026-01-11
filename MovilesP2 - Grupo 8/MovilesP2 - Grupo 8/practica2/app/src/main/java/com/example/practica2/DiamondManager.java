package com.example.practica2;

/**
 * DiamondManager gestiona los diamantes del jugador.
 */
public class DiamondManager {

    // Número de monedas
    private static int diamonds = 0;
    // Número de monedas por nivel
    private static int diamondsPerLevel = 20;

    /**
     * Método que añade una cantidad de monedas.
     * @param diamondAmount
     */
    public static void addDiamonds(int diamondAmount)
    {
        diamonds += diamondAmount;
    }

    /**
     * Método que resta una cantidad de monedas.
     * @param diamondAmount
     */
    public static void subtractDiamonds(int diamondAmount) { diamonds -= diamondAmount; }

    /**
     * SETTERS.
     */
    public static void setDiamonds(int diamonds) { DiamondManager.diamonds = diamonds; }

    /**
     * GETTERS
     */
    public static int getDiamonds() { return diamonds; }
    public static int getDiamondsPerLevel() { return diamondsPerLevel; }

}
