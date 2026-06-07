package com.example.practica2;

import java.util.Collection;
import java.util.List;

/**
 * ShopManager es el gestor de la tienda.
 */
public class ShopManager {

    // Catálogo de la tienda.
    private ShopCatalog catalog;

    // Estado de los ítems.
    private PlayerShopState playerShopState;

    /**
     * CONSTRUCTORA.
     * @param catalog
     * @param playerState
     */
    public ShopManager(ShopCatalog catalog, PlayerShopState playerState) {
        this.catalog = catalog;
        this.playerShopState = playerState;
    }

    /**
     * Método que comprueba si el ítem se puede comprar.
     * @param itemId
     * @return
     */
    public boolean canBuy(String itemId) {
        if (!catalog.exists(itemId)) return false;
        if (playerShopState.isPurchased(itemId)) return false;

        ShopItemData item = catalog.getItem(itemId);
        return DiamondManager.getDiamonds() >= item.getCost();
    }

    /**
     * Método que compra un objeto.
     * @param itemId
     * @return
     */
    public boolean buyItem(String itemId) {
        if (!canBuy(itemId)) return false;

        ShopItemData item = catalog.getItem(itemId);

        playerShopState.purchase(itemId);
        autoSelect(item);

        return true;
    }

    /**
     * Método que selecciona un ítem.
     * @param itemId
     * @return
     */
    public boolean selectItem(String itemId) {
        if (!catalog.exists(itemId)) return false;
        if (!playerShopState.isPurchased(itemId)) return false;

        ShopItemData item = catalog.getItem(itemId);
        autoSelect(item);

        return true;
    }

    /**
     * Método que selecciona un ítem al comprarlo.
     * @param item
     */
    private void autoSelect(ShopItemData item) {
        if (item.getType() == ShopItemData.ShopItemType.TOWER) {
            playerShopState.selectTower(item.getId());
        } else if (item.getType() == ShopItemData.ShopItemType.SKIN) {
            playerShopState.selectSkin(item.getId());
        }
        else if (item.getType() == ShopItemData.ShopItemType.FONDO){
            playerShopState.selectColor(item.getId());
        }
    }

    /**
     * Método que deselecciona un ítem al comprarlo.
     * @param item
     */
    private void deselect(ShopItemData item) {
        if (item.getType() == ShopItemData.ShopItemType.TOWER) {
            playerShopState.deselectTower(item.getId());
        }
        else if (item.getType() == ShopItemData.ShopItemType.SKIN) {
            playerShopState.deselectSkin(item.getId());
        }
        else if (item.getType() == ShopItemData.ShopItemType.FONDO) {
            playerShopState.selectColor(null);
        }
    }

    /**
     * Método que gestiona la selección/deselección de un ítem al comprarlo.
     * @param itemId
     */
    public boolean toggleSelectItem(String itemId) {
        if (!catalog.exists(itemId)) return false;
        if (!playerShopState.isPurchased(itemId)) return false;

        ShopItemData item = catalog.getItem(itemId);

        if (isSelected(itemId)) {
            deselect(item);
        } else {
            autoSelect(item);
        }

        return true;
    }

    /**
     * GETTERS.
     */
    // Devuelve todos los items torres de la tienda
    public List<ShopItemData> getTowerItems() {
        return catalog.getItemsByType(ShopItemData.ShopItemType.TOWER);
    }

    // Devuelve todos los items skins de torres de la tienda
    public List<ShopItemData> getSkinItems() {
        return catalog.getItemsByType(ShopItemData.ShopItemType.SKIN);
    }

    // Devuelve todos los items temas de colores de la tienda
    public List<ShopItemData> getColorItems() {
        return catalog.getItemsByType(ShopItemData.ShopItemType.FONDO);
    }

    // Devuelve si el item ha sido comprado
    public boolean isPurchased(String itemId) {
        return playerShopState.isPurchased(itemId);
    }

    // Devuelve si el item está seleccionado
    public boolean isSelected(String itemId) {
        return playerShopState.isTowerSelected(itemId) ||
                playerShopState.isSkinSelected(itemId) ||
                itemId.equals(playerShopState.getSelectedColorId());
    }

    // Devuelve el tema de color seleccionado
    public ShopItemData getSelectedColor() {
        return catalog.getItem(playerShopState.getSelectedColorId());
    }

    public ShopCatalog getShopCatalog() { return this.catalog; }
    public PlayerShopState getPlayerShopState() { return this.playerShopState; }

}
