package com.example.practica2;

import java.util.Collection;
import java.util.List;

public class ShopManager {

    private ShopCatalog catalog;
    private PlayerShopState playerShopState;

    public ShopManager(ShopCatalog catalog, PlayerShopState playerState) {
        this.catalog = catalog;
        this.playerShopState = playerState;
    }

    // Comprueba si el item se puede comprar
    public boolean canBuy(String itemId) {
        if (!catalog.exists(itemId)) return false;
        if (playerShopState.isPurchased(itemId)) return false;

        ShopItemData item = catalog.getItem(itemId);
        return DiamondManager.getDiamonds() >= item.getCost();
    }

    // Compra el item
    public boolean buyItem(String itemId) {
        if (!canBuy(itemId)) return false;

        ShopItemData item = catalog.getItem(itemId);
        DiamondManager.subtractDiamonds(item.getCost());

        playerShopState.purchase(itemId);
        autoSelect(item);

        return true;
    }

    // Selecciona el item
    public boolean selectItem(String itemId) {
        if (!catalog.exists(itemId)) return false;
        if (!playerShopState.isPurchased(itemId)) return false;

        ShopItemData item = catalog.getItem(itemId);
        autoSelect(item);
        /*if (item.getType() == ShopItemData.ShopItemType.TOWER) {
            playerShopState.selectTower(itemId);
        } else if (item.getType() == ShopItemData.ShopItemType.SKIN) {
            playerShopState.selectSkin(itemId);
        }*/

        return true;
    }

    // Selecciona un item al comprarlo
    private void autoSelect(ShopItemData item) {
        if (item.getType() == ShopItemData.ShopItemType.TOWER) {
            playerShopState.selectTower(item.getId());
        } else if (item.getType() == ShopItemData.ShopItemType.SKIN) {
            playerShopState.selectSkin(item.getId());
        }
    }

    // Devuelve todos los items de la tienda
    public Collection<ShopItemData> getAllItems() {
        return catalog.getAllItems();
    }

    // Devuelve todos los items torres de la tienda
    public List<ShopItemData> getTowerItems() {
        return catalog.getItemsByType(ShopItemData.ShopItemType.TOWER);
    }

    // Devuelve todos los items skins de torres de la tienda
    public List<ShopItemData> getSkinItems() {
        return catalog.getItemsByType(ShopItemData.ShopItemType.SKIN);
    }

    // Devuelve si el item ha sido comprado
    public boolean isPurchased(String itemId) {
        return playerShopState.isPurchased(itemId);
    }

    // Devuelve si el item está seleccionado
    public boolean isSelected(String itemId) {
        return itemId.equals(playerShopState.getSelectedTowerId()) ||
                itemId.equals(playerShopState.getSelectedSkinId());
    }

    // Devuelve la torre seleccionada
    public ShopItemData getSelectedTower() {
        return catalog.getItem(playerShopState.getSelectedTowerId());
    }

    // Devuelve la skin seleccionada
    public ShopItemData getSelectedSkin() {
        return catalog.getItem(playerShopState.getSelectedSkinId());
    }

    public ShopCatalog getShopCatalog() { return this.catalog; }
    public PlayerShopState getPlayerShopState() { return this.playerShopState; }

}
