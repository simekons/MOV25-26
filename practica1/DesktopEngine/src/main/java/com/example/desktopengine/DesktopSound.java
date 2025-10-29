package com.example.desktopengine;


import com.example.engine.ISound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class DesktopSound implements ISound
{
    // Id del sonido.
    private int soundId;
    // Clip del sonido.
    private Clip clip;
    // String con su ubicación.
    private String filePath;

    public DesktopSound(String file, int id)
    {
        soundId = id;
        filePath = file;
        try{
            File audioFile = new File(file);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Id del sonido.
    public int getId() {
        return soundId;
    }
    // Clip del sonido.
    public Clip getClip() {
        return clip;
    }
    // Ruta del archivo.
    public String getFilePath() {
        return filePath;
    }
}
