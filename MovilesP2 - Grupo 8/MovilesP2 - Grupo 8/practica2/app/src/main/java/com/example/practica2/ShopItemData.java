package com.example.practica2;

/**
 * ShopItemData contiene la información de cada ítem individual.
 */
public class ShopItemData {

    // Variables del item.
    private ShopItemType type;
    private String id;
    private String description;
    private String imagePath;
    private int cost;

    /**
     * Tipo de ítem.
     */
    public enum ShopItemType {
        TOWER,
        SKIN
    }

    /**
     * CONSTRUCTORA.
     * @param id
     * @param type
     * @param cost
     * @param description
     * @param imagePath
     */
    public ShopItemData(String id, ShopItemType type, int cost,
                        String description, String imagePath) {
        this.id = id;
        this.type = type;
        this.cost = cost;
        this.description = description;
        this.imagePath = imagePath;
    }

    /**
     * GETTERS.
     */
    public ShopItemType getType() { return this.type; }
    public String getId() { return this.id; }
    public String getDescription() { return this.description; }
    public String getImagePath() { return this.imagePath; }
    public int getCost() { return this.cost; }
}
