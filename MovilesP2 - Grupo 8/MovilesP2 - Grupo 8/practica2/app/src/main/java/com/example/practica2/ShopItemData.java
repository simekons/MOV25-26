package com.example.practica2;

public class ShopItemData {

    private ShopItemType type;
    private String id;
    private String description;
    private String imagePath;
    private int cost;
    private int color;

    public enum ShopItemType {
        TOWER,
        SKIN
    }

    public ShopItemData(String id, ShopItemType type, int cost,
                        String description, String imagePath, int color) {
        this.id = id;
        this.type = type;
        this.cost = cost;
        this.description = description;
        this.imagePath = imagePath;
        this.color = color;
    }

    public ShopItemType getType() { return this.type; }
    public String getId() { return this.id; }
    public String getDescription() { return this.description; }
    public String getImagePath() { return this.imagePath; }
    public int getCost() { return this.cost; }
}
