package com.example.androidengine;

import android.app.Activity;
import android.content.res.AssetManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AndroidFile {

    private Activity activity;
    private AssetManager assetManager;

    // Constructora de AndroidFile
    public AndroidFile(Activity activity)
    {
        this.activity = activity;
        this.assetManager = this.activity.getAssets();
    }

    // Lee un archivo según una ruta dentro del proyecto (ej: assets)
    public byte[] readFile(String path) {
        try {
            InputStream inputStream = assetManager.open(path);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            return buffer;
        }
        catch (Exception e) {
            return null;
        }
    }

    // Elimina un archivo del almacenamiento interno del móvil
    public void deleteFile(String fileName) {
        try {
            File file = new File(activity.getFilesDir().getPath(), fileName);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("Archivo eliminado: " + fileName);
                } else {
                    System.out.println("No se pudo eliminar el archivo: " + fileName);
                }
            } else {
                System.out.println("El archivo no existe: " + fileName);
            }
        } catch (Exception e) {

        }
    }

    // Devuelve los archivos que se encuentran dentro de una ruta
    public String[] listFiles(String path) {
        try {
            return assetManager.list(path);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }
}
