package com.example.practica2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * PlayerShopState gestiona el estado de los items de la tienda.
 */
public class PlayerShopState {

    // Items comprados.
    private Set<String> purchasedItems = new HashSet<>();

    // Variables de selección.
    private String selectedTowerId;
    private String selectedSkinId;

    /**
     * Método que devuelve si está comprado o no un ítem.
     * @param itemId
     * @return
     */
    public boolean isPurchased(String itemId) {
        return purchasedItems.contains(itemId);
    }

    /**
     * Método que marca un ítem como comprado.
     * @param itemId
     */
    public void purchase(String itemId) {
        purchasedItems.add(itemId);
    }

    /**
     * Método que selecciona una torre.
     * @param itemId
     */
    public void selectTower(String itemId) {
        selectedTowerId = itemId;
    }

    /**
     * Método que selecciona un aspecto.
     * @param itemId
     */
    public void selectSkin(String itemId) {
        selectedSkinId = itemId;
    }

    /**
     * GETTERS.
     */
    public String getSelectedTowerId() { return selectedTowerId; }
    public String getSelectedSkinId() { return selectedSkinId; }
    public Set<String> getPurchasedItems() { return Collections.unmodifiableSet(purchasedItems); }
}
