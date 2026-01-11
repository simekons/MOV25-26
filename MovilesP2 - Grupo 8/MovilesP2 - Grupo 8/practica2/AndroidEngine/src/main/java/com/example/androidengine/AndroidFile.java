package com.example.androidengine;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;

import com.example.engine.IFile;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import android.content.res.AssetManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * AndroidFile gestiona los archivos en Android.
 */
public class AndroidFile {

    // Actividad principal.
    private Activity activity;

    // AssetManager.
    private AssetManager assetManager;

    /**
     * CONSTRUCTORA.
     * @param activity
     */
    public AndroidFile(Activity activity)
    {
        this.activity = activity;
        this.assetManager = this.activity.getAssets();
    }

    /**
     * Método que lee un archivo según una ruta dentro del proyecto (ej. assets)
     * @param path
     * @return
     */
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

    /**
     * Método que elimina un archivo del almacenamiento interno del móvil
     * @param fileName
     */
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

    /**
     * Método que devuelve los archivos que se encuentran dentro de una ruta.
     * @param path
     * @return
     */
    public String[] listFiles(String path) {
        try {
            return assetManager.list(path);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    /**
     * Método que carga un archivo del almacenamiento interno del móvil con un hash.
     * @param path
     * @return
     */
    public JSONObject loadDataWithHash(String path)
    {
        try{
            // Bloque para leer el archivo
            FileInputStream fis = activity.openFileInput(path);
            StringBuilder builder = new StringBuilder();
            int content;
            while ((content = fis.read()) != -1) {
                builder.append((char) content);
            }
            fis.close();

            JSONObject jsonObject = new JSONObject(builder.toString());

            // Se lee el hash original
            String originalHash = jsonObject.optString("hash", "");
            jsonObject.remove("hash");
            // Se genera un nuevo hash
            String currentHash = AndroidEngine.createHash(jsonObject.toString());

            // Si los dos hash no coinciden, salta una excepción que devuelve un json vacío
            if (!currentHash.equals(originalHash)) {
                throw new SecurityException("Data has been changed");
            }

            return jsonObject;
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    /**
     * Método que guarda datos en un archivo del almacenamiento interno del móvil con un hash.
     * @param path
     * @param jsonObject
     */
    public void saveDataWithHash(String path, JSONObject jsonObject)
    {
        try{
            String data = jsonObject.toString();
            String hash = AndroidEngine.createHash(data);

            jsonObject.put("hash", hash);

            // Guarda json con hash
            FileOutputStream fos = activity.openFileOutput(path, Context.MODE_PRIVATE);
            fos.write(jsonObject.toString().getBytes());
            fos.close();
        } catch (Exception e) {

        }
    }
}
