package com.example.androidengine;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;

import com.example.engine.ISound;

import java.io.IOException;

/**
 * AndroidSound implementa los sonidos en Android.
 */
public class AndroidSound implements ISound
{
    // Id del sonido.
    private int soundId;
    // Id de la reproducción.
    private int streamId;

    /**
     * CONSTRUCTORA.
     * @param file
     * @param assetManager
     * @param soundPool
     */
    public AndroidSound(String file, AssetManager assetManager, SoundPool soundPool){

        try {
            AssetFileDescriptor assetDescriptor =
                    assetManager.openFd(file);
            this.soundId = soundPool.load(assetDescriptor, 1);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load sound.");
        }

        streamId = -1;
    }

    /**
     * GETTERS.
     */
    // Id del sonido.
    public int getId() { return soundId; }
    // Id de la reproducción.
    public int getStreamId()
    {
        return this.streamId;
    }

    /**
     * SETTERS.
     */
    // Id de la reproducción.
    public void setStreamId(int id)
    {
        this.streamId = id;
    }
}
