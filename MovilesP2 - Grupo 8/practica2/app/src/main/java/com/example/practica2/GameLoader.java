package com.example.practica2;

import com.example.androidengine.AndroidFile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameLoader {


    private AndroidFile file;

    private ArrayList<AdventureScene.LevelState> levelStates;
    private static ArrayList<Theme> themes;
    private ArrayList<Boolean> itemsState;
    private ArrayList<Boolean> fruitItems;
    private ArrayList<Boolean> selectedItems;

    private static String path;

    private int colorUnlocked, colorLocked;

    // Constructora de la clase
    public GameLoader(AndroidFile iFile)
    {
        this.file = iFile;
        levelStates = new ArrayList<>();
        itemsState = new ArrayList<>();
        fruitItems = new ArrayList<>();
        selectedItems = new ArrayList<>();
        themes = new ArrayList<>();
    }

    // Lee archivo json de las carpetas del proyecto (ej: assets)
    public JSONObject readJSONFromAssets(String path)
    {
        try{
            byte[] buffer = file.readFile(path);

            String jsonContent = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonContent);
            return jsonObject;
        }
        catch(Exception e) {
            return null;
        }
    }

    // Carga nivel de las carpetas del proyecto (ej: assets)
    public LevelData loadLevelFromAssets(int _world, int _level)
    {
        String world = "world" + _world;
        String level = "level_" + _world + "_0" + _level;
        String path = "levels/" + world + "/" + level + ".json";

        JSONObject jsonObject = readJSONFromAssets(path);
        return levelInfo(jsonObject, _world, _level, false);
    }

    // Carga estilo de los botones de los niveles
    public void loadStyle(String world)
    {
        try{
            String path = "levels/" + world + "/style.json";
            JSONObject jsonObject = readJSONFromAssets(path);

            colorUnlocked = (int) Long.parseLong(jsonObject.getString("colorUnlocked").substring(2), 16);
            colorLocked = (int) Long.parseLong(jsonObject.getString("colorLocked").substring(2), 16);
        }
        catch (Exception e) {

        }
    }

    // Carga estilo de los colores de los themes de la tienda (color de fondo y color secundario)
    public void loadThemes()
    {
        try{
            JSONObject jsonObject = readJSONFromAssets("shop/themesColor.json");

            themes.clear();

            for (int i = 0; i <= jsonObject.length(); i++) {
                JSONObject item = jsonObject.getJSONObject("item" + i);

                int buttonColor = (int) Long.parseLong(item.getString("buttonColor").substring(2), 16);
                int backgroundColor = (int) Long.parseLong(item.getString("backgroundColor").substring(2), 16);

                themes.add(new Theme(buttonColor, backgroundColor));
            }
        }
        catch (Exception e) {

        }
    }

    // Carga datos generales del almacenamiento interno (monedas y theme guardado)
    public void loadGenericData()
    {
        try {
            path = "gameData.json";
            JSONObject jsonObject = file.loadDataWithHash(path);
            int coins = jsonObject.optInt("coins", 0);
            DiamondManager.setCoins(coins);

            if(Theme.getCurrentTheme() == null)
            {
                loadThemes();
                setThemes();
            }

            int theme = jsonObject.optInt("currentTheme", 0);
            Theme.setCurrentTheme(Theme.getTheme(theme));
        } catch (Exception e) {

        }
    }

    // Carga nivel del almacenamiento interno
    public LevelData loadLevelFromFiles(int _world, int _level)
    {
        String level = "level_" + _world + "_0" + _level;
        String path = level + ".json";

        JSONObject jsonObject = file.loadDataWithHash(path);
        if(jsonObject.length() == 0)
            return null;
        return levelInfo(jsonObject, _world, _level, true);
    }

    // Carga estado de los niveles del almacenamiento interno
    public ArrayList<AdventureScene.LevelState> loadLevelStates()
    {
        JSONObject jsonObject = file.loadDataWithHash("levelsState.json");
        levelStates.clear();
        levelStates = levelStatesInfo(jsonObject);
        return levelStates;
    }

    // Carga estado de los items de la tienda del almacenamiento interno
    public ArrayList<Boolean> loadItemsState()
    {
        JSONObject jsonObject = file.loadDataWithHash("itemsState.json");
        itemsState.clear();
        itemsState = itemsStateInfo(jsonObject);
        return itemsState;
    }

    // Carga si los items de personalización de la escena de juego están seleccionados
    public ArrayList<Boolean> loadFruitItems()
    {
        JSONObject jsonObject = file.loadDataWithHash("fruitItems.json");
        fruitItems.clear();
        try{
            for (int i = 1; jsonObject.has("item" + i); i++) {
                boolean state = jsonObject.getBoolean("item" + i);
                fruitItems.add(state);
            }
            return fruitItems;
        } catch (Exception e) {
            return null;
        }
    }

    // Carga si los items de la tienda están seleccionados
    public ArrayList<Boolean> loadSelectedItems()
    {
        JSONObject jsonObject = file.loadDataWithHash("selectedItems.json");
        selectedItems.clear();
        try{
            for (int i = 1; jsonObject.has("item" + i); i++) {
                boolean state = jsonObject.getBoolean("item" + i);
                selectedItems.add(state);
            }
            return selectedItems;
        } catch (Exception e) {
            return null;
        }
    }

    // Guarda progreso del nivel (burbujas a lanzar, burbujas en tablero, número de filas y puntuación)
    public void saveLevel(List<Integer> bubblesToLaunch, int nRows, List<Integer> bubblesInBoard, int score, String levelPath) {
        try{

            path = levelPath;
            JSONObject levelJson = new JSONObject();
            JSONArray bubblesToLaunchArray = new JSONArray(bubblesToLaunch);
            JSONArray bubblesInBoardArray = new JSONArray(bubblesInBoard);

            levelJson.put("bubblesToLaunch", bubblesToLaunchArray);
            levelJson.put("numRows", nRows);
            levelJson.put("bubblesInBoard", bubblesInBoardArray);
            levelJson.put("score", score);

            file.saveDataWithHash(path, levelJson);

        }catch (Exception e) {

        }
    }

    // Guarda estado de los niveles (bloqueados, desbloqueados sin hacer, desbloqueados hechos)
    public void saveLevelsState(ArrayList<AdventureScene.LevelState> levelStates) {
        try{
            path = "levelsState.json";
            JSONObject levelStateJson = new JSONObject();
            for (int i = 1; i < levelStates.size() + 1; i++)
            {
                String state = "";
                switch (levelStates.get(i - 1))
                {
                    case LOCKED:
                        state = "Locked";
                        break;
                    case UNLOCKED_INCOMPLETE:
                        state = "UnlockedIncomplete";
                        break;
                    case UNLOCKED_COMPLETED:
                        state = "UnlockedCompleted";
                        break;
                }
                levelStateJson.put("level" + i, state);
            }

            file.saveDataWithHash(path, levelStateJson);

        }catch (Exception e) {

        }
    }

    // Guarda estado de los items de la tienda (bloqueados o desbloqueados)
    public void saveItemsState(ArrayList<Boolean> itemsState)
    {
        try{
            path = "itemsState.json";
            JSONObject itemsStateJson = new JSONObject();

            for (int i = 1; i < itemsState.size() + 1; i++)
            {
                if(itemsState.get(i - 1))
                    itemsStateJson.put("item" + i, "Locked");
                else
                    itemsStateJson.put("item" + i, "Unlocked");
            }

            file.saveDataWithHash(path, itemsStateJson);

        }catch (Exception e) {

        }
    }

    // Guarda si los items de personalización de la escena de juego están seleccionados o no
    public void saveFruitItems(ArrayList<ShopItem> fruitItems)
    {
        try{
            path = "fruitItems.json";
            JSONObject fruitItemsJson = new JSONObject();

            for (int i = 1; i < fruitItems.size() + 1; i++)
                fruitItemsJson.put("item" + i, fruitItems.get(i - 1).isSelected());

            file.saveDataWithHash(path, fruitItemsJson);

        }catch (Exception e) {

        }
    }

    // Guarda si los items de la tienda están seleccionados o no
    public void saveSelectedItems(ArrayList<Boolean> selectedItems)
    {
        try{
            path = "selectedItems.json";
            JSONObject selectedItemsJson = new JSONObject();

            for (int i = 1; i < selectedItems.size() + 1; i++)
                selectedItemsJson.put("item" + i, selectedItems.get(i - 1));

            file.saveDataWithHash(path, selectedItemsJson);

        }catch (Exception e) {

        }
    }
    // Guarda número de monedas
    public void saveCoins(int coins)
    {
        try {
            path = "gameData.json";
            JSONObject jsonObject = file.loadDataWithHash(path);
            jsonObject.put("coins", coins);
            file.saveDataWithHash(path, jsonObject);
        } catch (Exception e) {

        }
    }

    // Guarda theme actual del juego
    public void saveTheme(int theme)
    {
        try {
            path = "gameData.json";
            JSONObject jsonObject = file.loadDataWithHash(path);
            jsonObject.put("currentTheme", theme);
            file.saveDataWithHash(path, jsonObject);
        } catch (Exception e) {

        }
    }

    // Devuelve la información del nivel
    public LevelData levelInfo(JSONObject jsonObject, int _world, int _level, boolean isFromFiles)
    {
        try{
            JSONArray bubblesToLaunchArray = jsonObject.getJSONArray("bubblesToLaunch");
            List<Integer> bubblesToLaunch = new ArrayList<>();
            for (int i = 0; i < bubblesToLaunchArray.length(); i++) {
                bubblesToLaunch.add(bubblesToLaunchArray.getInt(i));
            }

            int numRows = jsonObject.getInt("numRows");

            JSONArray bubblesInBoardArray = jsonObject.getJSONArray("bubblesInBoard");
            List<Integer> bubblesInBoard = new ArrayList<>();
            for (int i = 0; i < bubblesInBoardArray.length(); i++) {
                bubblesInBoard.add(bubblesInBoardArray.getInt(i));
            }

            int score = 0;
            if(isFromFiles)
                score = jsonObject.getInt("score");

            return new LevelData(bubblesToLaunch, numRows, bubblesInBoard, score, _world, _level);
        } catch (Exception e)
        {
            return null;
        }
    }

    // Devuelve el estado de los niveles del modo aventura
    public ArrayList<AdventureScene.LevelState> levelStatesInfo(JSONObject jsonObject) {
        try{
            for (int i = 1; jsonObject.has("level" + i); i++) {
                String state = jsonObject.getString("level" + i);
                switch (state)
                {
                    case "Locked":
                        levelStates.add(AdventureScene.LevelState.LOCKED);
                        break;
                    case "UnlockedIncomplete":
                        levelStates.add(AdventureScene.LevelState.UNLOCKED_INCOMPLETE);
                        break;
                    case "UnlockedCompleted":
                        levelStates.add(AdventureScene.LevelState.UNLOCKED_COMPLETED);
                        break;
                }
            }
            return levelStates;
        }catch (Exception e)
        {
            return null;
        }
    }

    // Devuelve el estado de los items de la tienda
    public ArrayList<Boolean> itemsStateInfo(JSONObject jsonObject)
    {
        try {
            for (int i = 1; jsonObject.has("item" + i); i++) {
                String state = jsonObject.getString("item" + i);
                if(state.equals("Locked"))
                    itemsState.add(true);
                else
                    itemsState.add(false);
            }
            return itemsState;
        } catch (Exception e)
        {
            return null;
        }

    }

    public static String getPath() { return path; }
    public int getColorUnlocked() { return colorUnlocked; }
    public int getColorLocked() { return colorLocked; }

    public void setThemes() {Theme.setThemes(themes);}

}
