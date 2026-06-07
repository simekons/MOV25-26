package com.example.practica2;

import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidFile;
import com.example.androidengine.AndroidFont;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidImage;
import com.example.androidengine.AndroidInput;
import com.example.androidengine.AndroidSound;
import com.example.engine.IFont;
import com.example.engine.IImage;
import com.example.engine.IInput;
import com.example.engine.IScene;

import java.util.HashMap;
import java.util.Map;

import java.util.List;

/**
 * ShopScene implementa la escena de la tienda.
 */
public class ShopScene implements IScene {

    // Motor de Android.
    private AndroidEngine iEngine;

    // Gráficos de Android.
    private AndroidGraphics iGraphics;

    // Audio en Android.
    private AndroidAudio iAudio;

    // Imágenes.
    private AndroidImage exitImage;
    private AndroidImage imgDiamond;

    // Sonidos.
    private AndroidSound clickSound;

    // Fuentes.
    private AndroidFont buttonFont;

    // GameLoader.
    private GameLoader gameLoader;

    // Manager de la tienda.
    private ShopManager shopManager;
    private ShopInfoPanel infoPanel;

    // Botones.
    private Button exitButton;
    private Map<String, Button> towerButtons = new HashMap<>();
    private Map<String, Button> skinButtons = new HashMap<>();
    private Map<String, Button> colorButtons = new HashMap<>();

    // Variables de los botones.
    private String selectedItemId = null;
    private int panelColor, panelButtonColor;

    // Variables de scroll
    private int lastTouchY;
    private int scrollOffset;
    private int maxScrollOffset;
    private int minScrollOffset;
    private boolean isScrolling;

    private float y1, y2, y3;
    private float og_y1, og_y2, og_y3;
    /**
     * CONSTRUCTORA.
     * @param gameLoader
     */
    public ShopScene(GameLoader gameLoader)
    {
        this.iEngine = AndroidEngine.get_instance();
        this.iGraphics = this.iEngine.getGraphics();
        this.iAudio = this.iEngine.getAudio();
        this.iEngine.getAds().setBannerVisible(false);
        this.gameLoader = gameLoader;
        this.lastTouchY = -1;
        this.scrollOffset = 0;
        this.maxScrollOffset = 50;
        this.minScrollOffset = 25;
        this.isScrolling = false;
        og_y1 = y1 = 150;
        og_y2 = y2 = 280;
        og_y3 = y3 = 410;
        loadAssets();

        this.shopManager = this.gameLoader.getShopManager();
        this.exitButton = new Button(this.iGraphics, this.exitImage, 0, 25, 50, 50);

        panelColor = gameLoader.getPanelColor();
        panelButtonColor = gameLoader.getPanelButtonColor();

        imgDiamond = iGraphics.loadImage("sprites/diamond.png");

        infoPanel = new ShopInfoPanel(iGraphics, this.buttonFont, 400, 10, 250, 325, panelColor, panelButtonColor);
        initShopButtons();
    }

    /**
     * Método que carga los assets.
     */
    private void loadAssets(){
        this.exitImage = this.iGraphics.loadImage("sprites/exit.png");
        this.clickSound = this.iAudio.newSound("music/button.wav");
        this.buttonFont = iGraphics.createFont("fonts/pixellari.ttf", 35, false, false);
    }

    /**
     * Método de RENDERIZADO.
     */
    @Override
    public void render() {
        iGraphics.clear(gameLoader.getBackgroundColor());

        this.exitButton.render();
        buttonFont.setSize(30);
        iGraphics.drawTextNotCentered(buttonFont, "Tienes ", 60, 40);
        iGraphics.drawImage(imgDiamond, 200, 30, 40,40);
        iGraphics.drawTextNotCentered(buttonFont, String.valueOf(DiamondManager.getDiamonds()), 225, 40);

        iGraphics.setColor(0xFF000000);
        buttonFont.setSize(30);

        // Render de las torres
        if(y1 > 100)
            iGraphics.drawText(buttonFont, "Nuevas torres", 100, y1 - 55);
        buttonFont.setSize(35);

        for (Map.Entry<String, Button> entry : towerButtons.entrySet()) {
            String itemId = entry.getKey();
            Button b = entry.getValue();

            // Estado del item (comprado, seleccionado,etc.)
            boolean selected = itemId.equals(selectedItemId);
            boolean purchased = shopManager.isPurchased(itemId);
            boolean stateSelected = shopManager.isSelected(itemId);

            b.setSelected(selected);
            b.setStateSelected(stateSelected);
            b.setPurchased(purchased);

            b.render();
        }

        iGraphics.setColor(0xFF000000);
        buttonFont.setSize(30);

        // Render de las skins de las torres
        iGraphics.drawText(buttonFont, "Apariencias de torres", 147, y2 - 55);
        buttonFont.setSize(35);

        for (Map.Entry<String, Button> entry : skinButtons.entrySet()) {
            String itemId = entry.getKey();
            Button b = entry.getValue();

            // Estado del item (comprado, seleccionado,etc.)
            boolean selected = itemId.equals(selectedItemId);
            boolean purchased = shopManager.isPurchased(itemId);
            boolean stateSelected = shopManager.isSelected(itemId);

            b.setSelected(selected);
            b.setStateSelected(stateSelected);
            b.setPurchased(purchased);

            b.render();
        }

        iGraphics.setColor(0xFF000000);
        buttonFont.setSize(30);

        // Render de los temas de colores
        iGraphics.drawText(buttonFont, "Apariencias de fondo", 147, y3 - 55);
        buttonFont.setSize(35);

        for (Map.Entry<String, Button> entry : colorButtons.entrySet()) {
            String itemId = entry.getKey();
            Button b = entry.getValue();

            // Estado del item (comprado, seleccionado,etc.)
            boolean selected = itemId.equals(selectedItemId);
            boolean purchased = shopManager.isPurchased(itemId);
            boolean stateSelected = shopManager.isSelected(itemId);

            b.setSelected(selected);
            b.setStateSelected(stateSelected);
            b.setPurchased(purchased);

            b.render();
        }

        ShopItemData selectedItem = shopManager.getShopCatalog().getItem(selectedItemId);
        infoPanel.render(selectedItem);
    }

    /**
     * Método de UPDATE (vacío).
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {

    }

    /**
     * Método que inicializa los botones de la tienda.
     */
    private void initShopButtons()
    {
        List<ShopItemData> towers = shopManager.getTowerItems();
        List<ShopItemData> skins = shopManager.getSkinItems();
        List<ShopItemData> colors = shopManager.getColorItems();

        int startX = 45;
        int startY = 150;

        int size = 64;
        int margin = 30;

        int x = startX;

        int i = 0;
        for (ShopItemData item : towers) {
            TowerType type;
            switch (item.getImagePath()) {
                case "circle":
                    type = TowerType.Poison;
                    break;
                case "star":
                    type = TowerType.Star;
                    break;
                case "octagon":
                    type = TowerType.Stun;
                    break;
                default:
                    continue;
            }

            Button b = new Button(iGraphics, type, x, startY, size, size, true, shopManager.isPurchased(item.getId()));
            towerButtons.put(item.getId(), b);

            x += size + margin;
            i++;
        }

        startX = 45;
        startY = 280;
        x = startX;

        for (ShopItemData item : skins) {
            AndroidImage img = iGraphics.loadImage(item.getImagePath());

            Button b = new Button(iGraphics, img, x, startY, size, size, true,shopManager.isPurchased(item.getId()));
            skinButtons.put(item.getId(), b);

            x += size + margin;
        }


        startX = 45;
        startY = 410;

        x = startX;

        for (ShopItemData item : colors){
            AndroidImage img = iGraphics.loadImage(item.getImagePath());

            Button b = new Button(iGraphics, img, x, startY, size, size, true, shopManager.isPurchased(item.getId()));
            colorButtons.put(item.getId(), b);

            x+= size + margin;
        }
    }

    /**
     * Método que GESTIONA el INPUT.
     * @param events
     */
    @Override
    public void handleInput(List<IInput.TouchEvent> events) {
        for(IInput.TouchEvent e : events)
        {
            switch (e.type)
            {
                case TOUCH_UP:
                    lastTouchY = -1;
                    isScrolling = false;
                    // Gestión de salir al menú principal
                    if(exitButton.imageIsTouched(e.x, e.y))
                    {
                        this.iAudio.playSound(clickSound, false);
                        this.iEngine.setScenes(new MenuScene(gameLoader));
                    }
                    // Gestión de los items
                    if (checkButtons(towerButtons, e)) return;
                    if (checkButtons(skinButtons, e)) return;
                    if (checkButtons(colorButtons, e)) return;
                    // Gestión del panel de item
                    if(infoPanel.getActionButton() != null)
                    {
                        // Gestión del botón de acción en el panel
                        if (infoPanel.getActionButton().isTouched(e.x, e.y)) {
                            // Diamantes suficientes¿?
                            int diamonds = DiamondManager.getDiamonds();
                            if(diamonds < infoPanel.itemCost())
                                return;
                            // Compra del item
                            if (!shopManager.isPurchased(selectedItemId)) {
                                shopManager.buyItem(selectedItemId);
                                gameLoader.saveDiamonds(diamonds - infoPanel.itemCost());
                                DiamondManager.subtractDiamonds(infoPanel.itemCost());
                                gameLoader.savePlayerShopState(shopManager.getPlayerShopState());
                            }
                            // Selección del item
                            else if (!shopManager.isSelected(selectedItemId)) {
                                shopManager.selectItem(selectedItemId);
                                gameLoader.savePlayerShopState(shopManager.getPlayerShopState());
                            }
                            // Deselección del item
                            else {
                                shopManager.toggleSelectItem(selectedItemId);
                                gameLoader.savePlayerShopState(shopManager.getPlayerShopState());
                            }
                            infoPanel.setItem(shopManager.getShopCatalog().getItem(selectedItemId), shopManager, gameLoader.getPanelColor(), gameLoader.getPanelButtonColor());
                        }
                    }
                    break;// Activa scroll
                case TOUCH_DOWN:
                    lastTouchY = e.y;
                    isScrolling = true;
                    break;
                case TOUCH_MOVE:
                    if (isScrolling) {
                        int deltaY = lastTouchY - e.y;
                        lastTouchY = e.y;

                        scrollOffset += deltaY;
                        scrollOffset = Math.max(minScrollOffset, Math.min(maxScrollOffset, scrollOffset));

                        for (Map.Entry<String, Button> b : towerButtons.entrySet()){
                            b.getValue().setY(og_y1 - scrollOffset);
                        }
                        for (Map.Entry<String, Button> b : skinButtons.entrySet()){
                            b.getValue().setY(og_y2 - scrollOffset);
                        }
                        for (Map.Entry<String, Button> b : colorButtons.entrySet()){
                            b.getValue().setY(og_y3 - scrollOffset);
                        }
                        y1 = og_y1 - scrollOffset;
                        y2 = og_y2 - scrollOffset;
                        y3 = og_y3 - scrollOffset;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Método que gestiona el focus de un item cuando se hace click.
     * @param buttons
     * @param e
     * @return
     */
    private boolean checkButtons(Map<String, Button> buttons, IInput.TouchEvent e) {
        for (Map.Entry<String, Button> entry : buttons.entrySet()) {
            if (entry.getValue().imageIsTouched(e.x, e.y)) {
                selectedItemId = entry.getKey();
                iAudio.playSound(clickSound, false);

                ShopItemData item = shopManager.getShopCatalog().getItem(selectedItemId);
                infoPanel.setItem(item, shopManager, gameLoader.getPanelColor(), gameLoader.getPanelButtonColor());

                return true;
            }
        }
        return false;
    }
}
