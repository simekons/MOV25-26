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
    private Set<String> selectedTowerIds = new HashSet<>();
    private Set<String> selectedSkinIds = new HashSet<>();
    private String selectedColorId;

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
        if (itemId == null) {
            selectedTowerIds.clear();
            return;
        }
        selectedTowerIds.add(itemId);
    }

    /**
     * Método que deselecciona una torre.
     * @param itemId
     */
    public void deselectTower(String itemId) {
        if (itemId == null) {
            selectedTowerIds.clear();
            return;
        }
        selectedTowerIds.remove(itemId);
    }

    /**
     * Método que selecciona un aspecto.
     * @param itemId
     */
    public void selectSkin(String itemId) {
        if (itemId == null) {
            selectedSkinIds.clear();
            return;
        }
        selectedSkinIds.add(itemId);
    }

    /**
     * Método que deselecciona un aspecto.
     * @param itemId
     */
    public void deselectSkin(String itemId) {
        if (itemId == null) {
            selectedSkinIds.clear();
            return;
        }
        selectedSkinIds.remove(itemId);
    }

    /**
     * GETTERS & SETTERS.
     */
    public boolean isTowerSelected(String itemId) {
        return selectedTowerIds.contains(itemId);
    }
    public boolean isSkinSelected(String itemId) {
        return selectedSkinIds.contains(itemId);
    }
    public Set<String> getSelectedTowerIds() { return Collections.unmodifiableSet(selectedTowerIds); }
    public Set<String> getSelectedSkinIds() {
        return Collections.unmodifiableSet(selectedSkinIds);
    }
    public Set<String> getPurchasedItems() { return Collections.unmodifiableSet(purchasedItems); }
    public String getSelectedColorId() { return selectedColorId; }

    public void selectColor(String itemId){
        selectedColorId = itemId;
    }
}
