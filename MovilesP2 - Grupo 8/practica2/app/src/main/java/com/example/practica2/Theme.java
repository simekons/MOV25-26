package com.example.practica1;

import java.util.ArrayList;

public class Theme {


    private static Theme currentTheme;

    private static ArrayList<Theme> themes;

    private int buttonColor;
    private int backgroundColor;

    // Constructora del Theme (color de fondo y color secundario)
    public Theme(int buttonColor, int backgroundColor)
    {
        this.buttonColor = buttonColor;
        this.backgroundColor = backgroundColor;
        themes = new ArrayList<>();
    }

    public static Theme getCurrentTheme() { return currentTheme; }
    public static Theme getTheme(int index) { return themes.get(index); }
    public int getButtonColor() {
        return buttonColor;
    }
    public int getBackgroundColor() { return backgroundColor; }

    public static void setCurrentTheme(Theme theme) { currentTheme = theme; }
    public static void setThemes(ArrayList<Theme> themes) { Theme.themes = themes; }
}
