package com.example.practica2;

import com.example.androidengine.AndroidAudio;
import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidFile;
import com.example.androidengine.AndroidFont;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidImage;
import com.example.androidengine.AndroidSound;
import com.example.engine.IFont;
import com.example.engine.IInput;
import com.example.engine.IScene;

import java.util.HashMap;
import java.util.Map;

import java.util.List;

public class ShopScene implements IScene {

    private AndroidEngine iEngine;
    private AndroidGraphics iGraphics;
    private AndroidAudio iAudio;
    private AndroidFile iFile;

    private AndroidImage exitImage;
    private AndroidSound clickSound;
    private AndroidFont buttonFont;

    private GameLoader gameLoader;
    private ShopManager shopManager;
    private ShopInfoPanel infoPanel;

    private Button exitButton;

    private Map<String, Button> towerButtons = new HashMap<>();
    private Map<String, Button> skinButtons = new HashMap<>();
    private String selectedItemId = null;

    public ShopScene(GameLoader gameLoader)
    {
        this.iEngine = AndroidEngine.get_instance();
        this.iGraphics = this.iEngine.getGraphics();
        this.iAudio = this.iEngine.getAudio();
        this.iFile = this.iEngine.getFile();
        this.gameLoader = gameLoader;

        loadAssets();

        this.shopManager = this.gameLoader.getShopManager();
        this.exitButton = new Button(this.iGraphics, this.exitImage, 0, 25, 50, 50);

        infoPanel = new ShopInfoPanel(iGraphics, this.buttonFont, 400, 10, 250, 325);
        initShopButtons();
    }

    private void loadAssets(){
        this.exitImage = this.iGraphics.loadImage("sprites/exit.png");
        this.clickSound = this.iAudio.newSound("music/button.wav");
        this.buttonFont = iGraphics.createFont("fonts/pixellari.ttf", 35, false, false);
    }

    @Override
    public void render() {

        this.exitButton.render();
        iGraphics.drawText(buttonFont, "Tienes " + DiamondManager.getDiamonds(), 180, 40);

        buttonFont.setSize(30);
        iGraphics.drawText(buttonFont, "Nuevas torres", 100, 95);
        buttonFont.setSize(35);

        for (Map.Entry<String, Button> entry : towerButtons.entrySet()) {
            String itemId = entry.getKey();
            Button b = entry.getValue();

            boolean selected = itemId.equals(selectedItemId);

            if (selected)
                iGraphics.drawRect(b.getX() - (b.getWidth() / 2), b.getY() - (b.getHeight() / 2), b.getWidth(), b.getHeight());

            b.render();
        }

        buttonFont.setSize(30);
        iGraphics.drawText(buttonFont, "Apariencias de torres", 147, 225);
        buttonFont.setSize(35);

        for (Map.Entry<String, Button> entry : skinButtons.entrySet()) {
            String itemId = entry.getKey();
            Button b = entry.getValue();

            boolean selected = itemId.equals(selectedItemId);

            if (selected)
                iGraphics.drawRect(b.getX() - (b.getWidth() / 2), b.getY() - (b.getHeight() / 2), b.getWidth(), b.getHeight());

            b.render();
        }

        ShopItemData selectedItem = shopManager.getShopCatalog().getItem(selectedItemId);
        infoPanel.render(selectedItem);
    }

    @Override
    public void update(float deltaTime) {

    }

    private void initShopButtons()
    {
        List<ShopItemData> towers = shopManager.getTowerItems();
        List<ShopItemData> skins = shopManager.getSkinItems();

        int startX = 45;
        int startY = 150;

        int size = 64;
        int margin = 30;

        int x = startX;

        for (ShopItemData item : towers) {
            AndroidImage img = iGraphics.loadImage(item.getImagePath());

            Button b = new Button(iGraphics, img, x, startY, size, size, true);
            towerButtons.put(item.getId(), b);

            x += size + margin;
        }

        startX = 45;
        startY = 280;
        x = startX;

        for (ShopItemData item : shopManager.getSkinItems()) {
            AndroidImage img = iGraphics.loadImage(item.getImagePath());

            Button b = new Button(iGraphics, img, x, startY, size, size, true);
            skinButtons.put(item.getId(), b);

            x += size + margin;
        }
    }

    @Override
    public void handleInput(List<IInput.TouchEvent> events) {
        for(IInput.TouchEvent e : events)
        {
            switch (e.type)
            {
                case TOUCH_UP:
                    if(exitButton.imageIsTouched(e.x, e.y))
                    {
                        this.iAudio.playSound(clickSound, false);
                        this.iEngine.setScenes(new MenuScene(gameLoader));
                    }
                    if (checkButtons(towerButtons, e)) return;
                    if (checkButtons(skinButtons, e)) return;
                    if (infoPanel.getActionButton().isTouched(e.x, e.y)) {

                        if (!shopManager.isPurchased(selectedItemId)) {
                            shopManager.buyItem(selectedItemId);
                            gameLoader.savePlayerShopState(shopManager.getPlayerShopState());
                            gameLoader.saveDiamonds(DiamondManager.getDiamonds());
                        }
                        else if (!shopManager.isSelected(selectedItemId)) {
                            shopManager.selectItem(selectedItemId);
                        }

                        infoPanel.setItem(
                                shopManager.getShopCatalog().getItem(selectedItemId),
                                shopManager
                        );
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private boolean checkButtons(Map<String, Button> buttons, IInput.TouchEvent e) {
        for (Map.Entry<String, Button> entry : buttons.entrySet()) {
            if (entry.getValue().imageIsTouched(e.x, e.y)) {
                selectedItemId = entry.getKey();
                iAudio.playSound(clickSound, false);

                ShopItemData item = shopManager.getShopCatalog().getItem(selectedItemId);
                infoPanel.setItem(item, shopManager);

                return true;
            }
        }
        return false;
    }


    /*public void drawButtons()
    {
        List<ShopItemData> towers = shopManager.getTowerItems();
        int x = startX;
        for (ShopItemData item : towers) {

            boolean selected = item.getId().equals(selectedItemId);

            int bgColor = selected ? 0xFFAA00 : 0xFFFFFFFF;

            iGraphics.drawRect(x, y, ITEM_SIZE, ITEM_SIZE, bgColor);

            IImage img = iGraphics.newImage(item.getImagePath());
            iGraphics.drawImage(img, x + 8, y + 8);

            // guardar rect para input (ver punto 4)
            //itemRects.put(item.getId(), new Rect(x, y, ITEM_SIZE, ITEM_SIZE));

            x += ITEM_SIZE + ITEM_MARGIN;
        }
    }*/
}
