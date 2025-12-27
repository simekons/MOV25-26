package com.example.practica2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PlayerShopState {

    private Set<String> purchasedItems = new HashSet<>();

    private String selectedTowerId;
    private String selectedSkinId;

    // Compras
    public boolean isPurchased(String itemId) {
        return purchasedItems.contains(itemId);
    }

    public void purchase(String itemId) {
        purchasedItems.add(itemId);
    }

    // Selección
    public void selectTower(String itemId) {
        selectedTowerId = itemId;
    }

    public void selectSkin(String itemId) {
        selectedSkinId = itemId;
    }

    public String getSelectedTowerId() {
        return selectedTowerId;
    }

    public String getSelectedSkinId() {
        return selectedSkinId;
    }

    public Set<String> getPurchasedItems() {
        return Collections.unmodifiableSet(purchasedItems);
    }
}
