package com.example.practica2;

import com.example.androidengine.AndroidFile;

import org.json.JSONObject;

import java.util.ArrayList;

public class GameLoader {

    private AndroidFile iFile;

    // Constructora de la clase
    public GameLoader(AndroidFile iFile)
    {
        this.iFile = iFile;
    }

    // Lee archivo json de las carpetas del proyecto (ej: assets)
    public JSONObject readJSONFromAssets(String path)
    {
        try{
            byte[] buffer = iFile.readFile(path);

            String jsonContent = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonContent);
            return jsonObject;
        }
        catch(Exception e) {
            return null;
        }
    }
}
