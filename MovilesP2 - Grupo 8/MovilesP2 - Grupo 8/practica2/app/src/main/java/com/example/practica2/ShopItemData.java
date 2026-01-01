package com.example.practica2;

public class ShopItemData {

    private ShopItemType type;
    private String id;
    private String description;
    private String imagePath;
    private int cost;
    private int backgroundColor;
    private int buttonColor;
    private int buttonColor2;

    public enum ShopItemType {
        TOWER,
        SKIN
    }

    public ShopItemData(String id, ShopItemType type, int cost,
                        String description, String imagePath, int backgroundColor, int buttonColor, int buttonColor2) {
        this.id = id;
        this.type = type;
        this.cost = cost;
        this.description = description;
        this.imagePath = imagePath;
        this.backgroundColor = backgroundColor;
        this.buttonColor = buttonColor;
        this.buttonColor2 = buttonColor2;
    }

    public ShopItemType getType() { return this.type; }
    public String getId() { return this.id; }
    public String getDescription() { return this.description; }
    public String getImagePath() { return this.imagePath; }
    public int getCost() { return this.cost; }
    public int getBackgroundColor() { return this.backgroundColor; }
    public int getButtonColor() { return buttonColor; }
    public int getButtonColor2() { return buttonColor2; }
}
