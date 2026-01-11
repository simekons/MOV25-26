package com.example.practica2;

import android.media.Image;

import com.example.androidengine.AndroidFont;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidImage;
import com.example.engine.IImage;

public class ShopInfoPanel {

    private AndroidGraphics iGraphics;
    private AndroidFont iFont;

    private Button actionButton;

    private String currentItemId;

    private int currentItemCost;

    private int x, y, width, height;
    private int panelColor, panelButtonColor;

    private IImage imgDiamond;

    public ShopInfoPanel(AndroidGraphics g, AndroidFont font,
                         int x, int y, int width, int height, int panelColor, int panelButtonColor) {
        this.iGraphics = g;
        this.iFont = font;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.panelColor = panelColor;
        this.panelButtonColor = panelButtonColor;

        imgDiamond = iGraphics.loadImage("sprites/diamond.png");
    }

    public void render(ShopItemData item) {
        if (item == null) return;

        iGraphics.setColor(panelColor);
        iGraphics.fillRectangle(x, y, width, height);
        iGraphics.setColor(0xff000000);

        // Icono
        iGraphics.setColor(0xFFFFFFFF);
        iGraphics.fillRectangle(x+18, y+18, 64, 64);
        iGraphics.setColor(0xFF000000);
        iGraphics.drawRect(x+18, y+18, 64, 64);

        switch (item.getImagePath()) {
            case "circle":
            {
                iGraphics.setColor(0xFF00FF00);
                iGraphics.fillCircle(x+50, y + 50, 27f);

                break;
            }
            case "star":
            {
                iGraphics.setColor(0xFFFF0080);
                iGraphics.fillStar(x + 50, y + 50, 27f);

                break;
            }
            case "octagon":
            {
                iGraphics.setColor(0xFF944D03);
                iGraphics.fillOctagon(x + 50, y + 50, 27f);

                break;
            }
            default:
                AndroidImage img = iGraphics.loadImage(item.getImagePath());

                iGraphics.drawImage(img, (int) x + 50, (int) y + 50, 54, 54);
                break;
        }

        iGraphics.setColor(0xFF000000);

        int cursorY = y + 120;

        iFont.setSize(26);
        iGraphics.drawTextNotCentered(iFont,"Coste: " + item.getCost(), x + 10, cursorY);
        iGraphics.drawImage(imgDiamond, x + 155, cursorY - 10, 20,20);
        cursorY += 40;

        iFont.setSize(26);
        iGraphics.drawTextNotCentered(iFont, "Descripción:", x + 10, cursorY);

        cursorY += 20;

        iFont.setSize(18);

        drawTextWrappedByChars(item.getDescription(), x + 10, cursorY, 25, 20);

        if (actionButton != null) {
            actionButton.render();
        }
    }
    private void drawTextWrappedByChars(String text, int startX, int startY, int maxChars, int lineHeight) {
        text = text.replace("\n", ""); // por si acaso

        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int y = startY;

        for (String word : words) {
            // +1 por el espacio
            if (line.length() + word.length() + 1 > maxChars) {
                iGraphics.drawTextNotCentered(iFont, line.toString(), startX, y);
                line = new StringBuilder(word);
                y += lineHeight;
            } else {
                if (line.length() > 0) line.append(" ");
                line.append(word);
            }
        }

        // última línea
        if (line.length() > 0) {
            iGraphics.drawTextNotCentered(iFont, line.toString(), startX, y);
        }
    }

    public void setItem(ShopItemData item, ShopManager shopManager, int panelColor, int panelButtonColor) {
        if (item == null) {
            currentItemId = null;
            actionButton = null;
            return;
        }

        this.panelColor = panelColor;
        this.panelButtonColor = panelButtonColor;

        currentItemId = item.getId();
        currentItemCost = item.getCost();

        int buttonY = y + height - 40;

        String text;

        if (!shopManager.isPurchased(item.getId())) {
            text = "Comprar";
        }
        else {
            text = "Agregado";
        }

        actionButton = new Button(iGraphics, iFont, x + 125, buttonY, width - 80, 45, text, this.panelButtonColor);
    }

    public Button getActionButton() { return this.actionButton; }

    public int itemCost() { return currentItemCost; }
}
