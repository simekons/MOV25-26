package com.example.practica2;

import com.example.androidengine.AndroidFont;
import com.example.androidengine.AndroidGraphics;

public class ShopInfoPanel {

    private AndroidGraphics iGraphics;
    private AndroidFont iFont;

    private Button actionButton;

    private String currentItemId;

    private int x, y, width, height;

    public ShopInfoPanel(AndroidGraphics g, AndroidFont font,
                         int x, int y, int width, int height) {
        this.iGraphics = g;
        this.iFont = font;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(ShopItemData item) {
        if (item == null) return;

        iGraphics.setColor(0xff79c8d7);
        iGraphics.fillRectangle(x, y, width, height);
        iGraphics.setColor(0xff000000);

        int cursorY = y + 120;

        //iFont.setSize(30);
        //graphics.drawText(font, item.getName(), x + 20, cursorY);
        //cursorY += 35;

        iFont.setSize(26);
        iGraphics.drawText(iFont,"Coste: " + item.getCost(), x + 70, cursorY);
        cursorY += 40;

        iFont.setSize(26);
        iGraphics.drawText(iFont, "Descripción: " + item.getDescription(), x + 110, cursorY);

        if (actionButton != null) {
            actionButton.render();
        }
    }

    public void setItem(ShopItemData item, ShopManager shopManager) {
        if (item == null) {
            currentItemId = null;
            actionButton = null;
            return;
        }

        currentItemId = item.getId();

        int buttonY = y + height - 40;

        String text;
        boolean enabled = true;

        if (!shopManager.isPurchased(item.getId())) {
            text = "Comprar";
            /*if (shopManager.canBuy(item.getId())) {
                text = "Comprar";
            } else {
                text = "Sin diamantes";
                enabled = false;
            }*/
        }
        else if (!shopManager.isSelected(item.getId())) {
            text = "Seleccionar";
        }
        else {
            text = "Seleccionado";
            enabled = false;
        }

        actionButton = new Button(iGraphics, iFont, x + 125, buttonY, width - 80, 45, text, 0xff01a9c9);
    }

    public Button getActionButton() { return this.actionButton; }
}
