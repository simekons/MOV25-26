package com.example.practica2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopCatalog {

    private Map<String, ShopItemData> itemsById = new HashMap<>();

    public ShopCatalog(List<ShopItemData> items) {
        for (ShopItemData item : items) {
            itemsById.put(item.getId(), item);
        }
    }

    public Collection<ShopItemData> getAllItems() {
        return itemsById.values();
    }

    public List<ShopItemData> getItemsByType(ShopItemData.ShopItemType type) {
        List<ShopItemData> result = new ArrayList<>();
        for (ShopItemData item : itemsById.values()) {
            if (item.getType() == type) {
                result.add(item);
            }
        }
        return result;
    }

    public ShopItemData getItem(String id) {
        return itemsById.get(id);
    }

    public boolean exists(String id) {
        return itemsById.containsKey(id);
    }

}
