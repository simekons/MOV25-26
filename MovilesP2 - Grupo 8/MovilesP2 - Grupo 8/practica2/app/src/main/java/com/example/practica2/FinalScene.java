package com.example.practica2;

import com.example.androidengine.AndroidAds;
import com.example.androidengine.AndroidEngine;
import com.example.androidengine.AndroidFile;
import com.example.engine.IAudio;
import com.example.engine.IEngine;
import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IImage;
import com.example.engine.IInput;
import com.example.engine.IScene;
import com.example.engine.ISound;

import java.util.List;


/**
 * FinalScene implementa la escena de victoria.
 */
public class FinalScene implements IScene {

    // Motor.
    private AndroidEngine iEngine;

    // Gráficos
    private IGraphics iGraphics;

    // Audio.
    private IAudio iAudio;

    // Archivos
    private AndroidFile androidFile;

    // Ads
    private AndroidAds androidAds;

    // Botones de menú.
    private Button playAgainButton;
    private Button menuButton;
    private Button adButton;
    private Button shareButton;

    // Fuente de título.
    private IFont titleFont;

    // Fuente de botones.
    private IFont fontButton;

    // Sonido de botón.
    private ISound soundButton;

    private GameLoader gameLoader;

    // Dificultad previa.
    private int difficulty;
    private int diamondsPerLevel;

    private boolean win;
    private boolean firstTime = true;
    private boolean adWatched = false;

    private IImage imgDiamond;

    /**
     * CONSTRUCTORA.
     * @param gameLoader
     * @param difficulty
     * @param win
     */
    public FinalScene(GameLoader gameLoader, int difficulty, boolean win, boolean firstTime) {
        this.iEngine = AndroidEngine.get_instance();
        this.iGraphics = iEngine.getGraphics();
        this.iAudio = iEngine.getAudio();
        this.androidFile = iEngine.getFile();
        this.androidAds = iEngine.getAds();

        this.gameLoader = gameLoader;

        this.difficulty = difficulty;
        this.win = win;
        this.diamondsPerLevel = DiamondManager.getDiamondsPerLevel();

        this.firstTime = firstTime;

        if(win && this.firstTime){
            DiamondManager.addDiamonds(diamondsPerLevel);
            this.gameLoader.saveDiamonds(DiamondManager.getDiamonds());
        }

        this.fontButton = iGraphics.createFont("fonts/fff.ttf", 15, false, false);
        this.titleFont = iGraphics.createFont("fonts/pixelGotic.ttf", 35, false, false);

        this.adButton = new Button(this.iGraphics, this.fontButton, 255,275,50,50, "x2", this.gameLoader.getButtonColor2());
        this.shareButton = new Button(this.iGraphics, this.fontButton, 370,275,150,50, "Compartir", this.gameLoader.getButtonColor2());
        this.playAgainButton = new Button(this.iGraphics, this.fontButton, 205,200,150,50, "Volver a jugar", this.gameLoader.getButtonColor());
        this.menuButton = new Button(this.iGraphics, this.fontButton, 370 ,200,150,50, "Menu", this.gameLoader.getButtonColor());

        imgDiamond = iGraphics.loadImage("sprites/diamond.png");

        this.soundButton = this.iAudio.newSound("music/button.wav");
    }

    /**
     * Método de RENDERIZADO.
     */
    @Override
    public void render() {
        iGraphics.clear(gameLoader.getBackgroundColor());

        iGraphics.setColor(0xff000000);
        String mensaje = "";
        mensaje = (win == true) ? "VICTORIA" : "DERROTA";
        iGraphics.drawText(titleFont, mensaje, 300, 100);
        if(win)
        {
            if (firstTime){
                iGraphics.drawTextNotCentered(fontButton, "+20", 180, 285);
                iGraphics.drawImage(imgDiamond, 150, 275, 30, 30);
                if(!adWatched)
                    adButton.render();
            }
            shareButton.render();
        }

        playAgainButton.render();
        menuButton.render();
    }

    /**
     * Método de UPDATE.
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) { }

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
                    if(playAgainButton.isTouched(e.x, e.y))
                    {
                        this.iEngine.setScenes(new GameScene(gameLoader, this.difficulty));
                    }
                    if(menuButton.isTouched(e.x, e.y))
                    {
                        this.iEngine.setScenes(new MenuScene(gameLoader));
                    }
                    if(adButton.isTouched(e.x, e.y) && win && this.firstTime)
                    {
                        if(!adWatched)
                        {
                            DiamondManager.addDiamonds(diamondsPerLevel);
                            gameLoader.saveDiamonds(DiamondManager.getDiamonds());
                            androidAds.showRewardedAd(() -> diamondsPerLevel *= 2);
                            adWatched = true;
                        }
                    }
                    if(shareButton.isTouched(e.x, e.y) && win)
                    {
                        iEngine.getAndroidShare().screenshot();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
