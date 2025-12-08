package com.example.practica2;

import com.example.androidengine.AndroidAds;
import com.example.androidengine.AndroidEngine;
import com.example.engine.IAudio;
import com.example.engine.IEngine;
import com.example.engine.IFont;
import com.example.engine.IGraphics;
import com.example.engine.IInput;
import com.example.engine.IScene;
import com.example.engine.ISound;
import com.google.android.gms.ads.AdView;

import java.util.List;

/*
 * MenuScene es la escena de menú.
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
    private IFont titleFont;

    // Sonido de botón.
    private ISound soundButton;

    // Booleano de juego.
    private boolean startGame;

    // Booleano de aventura.
    private boolean adventure = false;

    // Booleano de tienda.
    private boolean shop = false;

    // CONSTRUCTORA
    public MenuScene(){
        this.iEngine = AndroidEngine.get_instance();
        this.iGraphics = this.iEngine.getGraphics();
        this.iAudio = this.iEngine.getAudio();
        this.iEngine.getAds().setBannerVisible(true);
        this.startGame = false;

        IFont fontButton = iGraphics.createFont("fonts/fff.ttf", 20, false, false);
        playButton = new Button(iGraphics, fontButton, 225,200,125,50, "Jugar", 0xFF808080);
        adventureButton = new Button(iGraphics, fontButton, 375 ,200,125,50, "Aventura", 0xFF808080);
        shopButton = new Button(iGraphics, fontButton, 225,275,125,50, "Tienda", 0xff9CE4F5);

        titleFont = iGraphics.createFont("fonts/pixelGotic.ttf", 30, false, false);

        this.soundButton = this.iAudio.newSound("music/button.wav");
    }

    // RENDERIZADO
    @Override
    public void render() {
        iGraphics.setColor(0xff000000);
        iGraphics.drawText(titleFont, "TOWER", 300, 75);
        iGraphics.drawText(titleFont, "DEFENSE", 300, 150);
        playButton.render();
        adventureButton.render();
        shopButton.render();
    }

    // Update.
    @Override
    public void update(float deltaTime) {
        if(startGame){
            iEngine.setScenes(new DifficultyScene());
        }
        else if (shop){

        }
        else if (adventure){

        }
    }

    // Input.
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
