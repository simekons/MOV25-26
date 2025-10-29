package com.example.desktopengine;

import com.example.engine.IAudio;
import com.example.engine.ISound;

import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class DesktopAudio implements IAudio
{

    // Lista de los sonidos.
    private ArrayList<Clip> clips;
    // String para la ruta de los archivos de sonido.
    private String assetsRoute = "assets/";

    public DesktopAudio()
    {
        clips = new ArrayList<>();
    }

    @Override
    public ISound newSound(String file) {
        DesktopSound sound = new DesktopSound(assetsRoute + file, clips.size());
        clips.add(sound.getClip());
        return sound;
    }

    @Override
    public void playSound(ISound iSound, boolean loop) {
        DesktopSound desktopSound = (DesktopSound) iSound;

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(desktopSound.getFilePath()));
            Clip newClip = AudioSystem.getClip();
            newClip.open(audioStream);

            if (loop) {
                newClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            newClip.setFramePosition(0);
            newClip.start();

            clips.add(newClip);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopSound(ISound iSound) {
        DesktopSound desktopSound = (DesktopSound) iSound;

        for (Clip clip : clips) {
            if (clip.isRunning()) {
                clip.stop();
            }
        }
    }
}
