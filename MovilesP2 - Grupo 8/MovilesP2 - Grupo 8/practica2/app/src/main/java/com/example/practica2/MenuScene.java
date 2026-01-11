package com.example.practica2;

import com.example.androidengine.AndroidAds;
import com.example.androidengine.AndroidEngine;
import com.example.engine.IAudio;
import com.example.engine.IEngine;
import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IImage;
import com.example.engine.IInput;
import com.example.engine.IScene;
import com.example.engine.ISound;
import com.google.android.gms.ads.AdView;

import java.util.List;

/**
 * MenúScene implementa la escena del menú.
 */
public class MenuScene implements IScene {

    // Motor.
    private AndroidEngine iEngine;

    // Gráficos
    private IGraphics iGraphics;

    // Audio.
    private IAudio iAudio;

    // Ads
    private AndroidAds androidAds;

    // Botón de menú.
    private Button playButton;

    // Botón de aventura.
    private Button adventureButton;

    // Botón de tienda.
    private Button shopButton;

    // Fuente de título.
    private IFont titleFont, fontButton;

    // Sonido de botón.
    private ISound soundButton;

    private GameLoader gameLoader;

    // Booleano de juego.
    private boolean startGame;

    // Booleano de aventura.
    private boolean adventure = false;

    // Booleano de tienda.
    private boolean shop = false;

    private IImage imgDiamond;

    /**
     * CONSTRUCTORA.
     * @param gameLoader
     */
    public MenuScene(GameLoader gameLoader){
        this.iEngine = AndroidEngine.get_instance();
        this.iGraphics = this.iEngine.getGraphics();
        this.iAudio = this.iEngine.getAudio();
        this.iEngine.getAds().setBannerVisible(true);
        this.startGame = false;
        this.gameLoader = gameLoader;

        fontButton = iGraphics.createFont("fonts/pixellari.ttf", 30, false, false);
        playButton = new Button(iGraphics, fontButton, 225,200,125,50, "Jugar", this.gameLoader.getButtonColor());
        adventureButton = new Button(iGraphics, fontButton, 375 ,200,125,50, "Aventura", this.gameLoader.getButtonColor());
        shopButton = new Button(iGraphics, fontButton, 225,275,125,50, "Tienda", this.gameLoader.getButtonColor2());

        titleFont = iGraphics.createFont("fonts/pixelGotic.ttf", 30, false, false);

        this.imgDiamond = iGraphics.loadImage("sprites/diamond.png");

        this.soundButton = this.iAudio.newSound("music/button.wav");
    }

    /**
     * Método de RENDERIZADO.
     */
    @Override
    public void render() {
        iGraphics.clear(gameLoader.getBackgroundColor());

        iGraphics.setColor(0xff000000);
        iGraphics.drawText(titleFont, "TOWER", 300, 75);
        iGraphics.drawText(titleFont, "DEFENSE", 300, 150);
        playButton.render();
        adventureButton.render();
        shopButton.render();

        iGraphics.drawImage(imgDiamond, 350, 275, 40,40);
        iGraphics.drawTextNotCentered(fontButton, String.valueOf(DiamondManager.getDiamonds()), 375, 285);
    }

    /**
     * Método de UPDATE.
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        if(startGame){
            iEngine.setScenes(new DifficultyScene(gameLoader));
        }
        else if (shop){
            iEngine.setScenes(new ShopScene(gameLoader));
        }
        else if (adventure){
            iEngine.setScenes(new AdventureScene(gameLoader));
        }
    }

    /**
     * Método que GESTIONA el input.
     * @param events
     */
    @Override
    public void handleInput(List<IInput.TouchEvent> events) {
        for(IInput.TouchEvent e : events)
        {
            switch (e.type)
            {
                case TOUCH_UP:
                    if(playButton.isTouched(e.x, e.y))
                    {
                        iAudio.playSound(soundButton, false);
                        startGame = true;
                    }
                    if(adventureButton.isTouched(e.x, e.y)){
                        adventure = true;
                    }
                    if(shopButton.isTouched(e.x, e.y)){
                        shop = true;
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
