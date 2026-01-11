package com.example.androidengine;

import android.app.Activity;
import android.content.res.AssetManager;
import android.media.SoundPool;

import com.example.engine.IAudio;
import com.example.engine.ISound;

/**
 * AndroidAudio implementa los sonidos en Android.
 */
public class AndroidAudio implements IAudio
{

    // Pool de sonidos.
    private SoundPool soundPool;
    // Asset Manager.
    private AssetManager assetManager;
    // Actividad principal.
    private Activity activity;

    /**
     * CONSTRUCTORA.
     * @param activity
     */
    AndroidAudio(Activity activity) {
        this.soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        this.activity = activity;
        this.assetManager = this.activity.getAssets();
    }

    /**
     * Método que registra un nuevo sonido.
     * @param file
     * @return
     */
    @Override
    public AndroidSound newSound(String file) {
        AndroidSound sound = new AndroidSound(file, assetManager, soundPool);
        return sound;
    }

    /**
     * Método que ejecuta un sonido.
     * @param sound
     * @param loop
     */
    @Override
    public void playSound(ISound sound, boolean loop) {
        AndroidSound s = (AndroidSound) sound;
        int soundID = s.getId();

        int loopInt = 0;
        if (loop)
            loopInt = -1;

        if (s.getStreamId() != -1)
            stopSound(sound);

        int streamID = soundPool.play(soundID, 1.0f, 1.0f,
                1, loopInt, 1.0f);

        ((AndroidSound) sound).setStreamId(streamID);
    }

    /**
     * Método que para la reproducción de un sonido.
     * @param sound
     */
    @Override
    public void stopSound(ISound sound) {
        AndroidSound s = (AndroidSound) sound;
        soundPool.stop(s.getStreamId());
    }
}
