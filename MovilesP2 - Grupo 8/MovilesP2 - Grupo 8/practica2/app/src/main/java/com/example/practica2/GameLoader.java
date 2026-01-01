package com.example.practica2;

import android.util.Log;

import com.example.androidengine.AndroidFile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class GameLoader {


    private AndroidFile file;

    private ShopManager shopManager;
    private ShopCatalog shopCatalog;
    private PlayerShopState playerShopState;

    private ArrayList<AdventureScene.LevelState> levelStates;
    private static ArrayList<Theme> themes;
    private ArrayList<Boolean> itemsState;
    private ArrayList<Boolean> fruitItems;
    private ArrayList<Boolean> selectedItems;
    private ArrayList<ShopItemData> shopItems;

    private ArrayList<String> waveTypes;

    private ArrayList<Integer> waveAmounts;

    private static String path;

    private int colorUnlocked, colorLocked, colorPassed;
    private int backgroundColor, buttonColor, buttonColor2;

    // Constructora de la clase
    public GameLoader(AndroidFile iFile)
    {
        this.file = iFile;

        String[] files = this.file.listFiles("levels/world1");
        for (String f : files) {
            Log.e("ASSETS", f);
        }

        levelStates = new ArrayList<>();
        waveAmounts = new ArrayList<>();
        waveTypes = new ArrayList<>();
        selectedItems = new ArrayList<>();
        themes = new ArrayList<>();
        shopItems = new ArrayList<>();

        loadData();
    }

    private void loadData()
    {
        loadGenericData();
        loadShop();
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
            e.printStackTrace();
            throw new RuntimeException("No se encuentra el asset: " + path);
        }
    }

    // Carga nivel de las carpetas del proyecto (ej: assets)
    public LevelData loadLevelFromAssets(int _world, int _level)
    {
        String world = "world" + _world;
        String level = "level_" + _world + "_0" + _level;
        String path = "levels/" + world + "/" + level + ".json";

        JSONObject jsonObject = readJSONFromAssets(path);
        if (jsonObject == null) return null;

        return levelInfo(jsonObject, _world, _level, false);
    }

    // Carga estilo de los botones de los niveles
    public void loadStyle(String world)
    {
        try{
            String path = "levels/" + world + "/style.json";
            JSONObject jsonObject = readJSONFromAssets(path);

            colorUnlocked = (int) Long.parseLong(jsonObject.getString("colorPassed").substring(2), 16);
            colorUnlocked = (int) Long.parseLong(jsonObject.getString("colorUnlocked").substring(2), 16);
            colorLocked = (int) Long.parseLong(jsonObject.getString("colorLocked").substring(2), 16);
        }
        catch (Exception e) {

        }
    }

    private void loadShop()
    {
        this.shopItems = loadShopItems();
        this.shopCatalog = new ShopCatalog(shopItems);

        this.playerShopState = loadPlayerShopState();

        this.shopManager = new ShopManager(shopCatalog, playerShopState);
    }

    // Carga items de la tienda
    public ArrayList<ShopItemData> loadShopItems()
    {
        ArrayList<ShopItemData> items = new ArrayList<>();

        try{
            JSONObject root = readJSONFromAssets("shop/shopItems.json");
            JSONArray jsonItems = root.getJSONArray("items");

            for (int i = 0; i < jsonItems.length(); i++) {
                JSONObject obj = jsonItems.getJSONObject(i);

                String id = obj.getString("id");
                ShopItemData.ShopItemType type = ShopItemData.ShopItemType.valueOf(obj.getString("type"));
                int cost = obj.getInt("cost");
                String description = obj.getString("description");
                String image = obj.getString("image");
                int backgroundColor = parseColor(obj, "backgroundColor");
                int buttonColor = parseColor(obj, "buttonColor");
                int buttonColor2 = parseColor(obj, "buttonColor2");

                items.add(new ShopItemData(
                        id, type, cost, description, image, backgroundColor, buttonColor, buttonColor2
                ));
            }
        }
        catch (Exception e) {

        }

        return items;
    }

    // !!!!!!
    // Carga datos generales del almacenamiento interno (diamantes y theme guardado)
    public void loadGenericData()
    {
        try {
            path = "gameData.json";
            JSONObject jsonObject = file.loadDataWithHash(path);
            int diamonds = jsonObject.optInt("diamonds", 0);
            DiamondManager.setDiamonds(diamonds);
            this.backgroundColor = jsonObject.optInt("backgroundColor", 0);
            this.buttonColor = jsonObject.optInt("buttonColor", 0);
            this.buttonColor2 = jsonObject.optInt("buttonColor2", 0);

            /*if(backgroundColor == 0)
                backgroundColor = 0xffffffff;
            if(buttonColor == 0)
                buttonColor = 0xff808080;*/

            /*if(Theme.getCurrentTheme() == null)
            {
                loadThemes();
                setThemes();
            }*/

            //int theme = jsonObject.optInt("currentTheme", 0);
            //Theme.setCurrentTheme(Theme.getTheme(theme));
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

    // !!!!!
    // Carga estado de los niveles del almacenamiento interno
    public ArrayList<AdventureScene.LevelState> loadLevelStates()
    {
        JSONObject jsonObject = file.loadDataWithHash("levelsState.json");
        levelStates.clear();
        levelStates = levelStatesInfo(jsonObject);
        return levelStates;
    }

    // Carga estado de los items de la tienda
    public PlayerShopState loadPlayerShopState() {
        PlayerShopState state = new PlayerShopState();

        try {
            JSONObject json = file.loadDataWithHash("player_shop.json");


            JSONArray purchased = json.getJSONArray("purchased");
            for (int i = 0; i < purchased.length(); i++) {
                state.purchase(purchased.getString(i));
            }

            state.selectTower(json.optString("selectedTower", null));
            state.selectSkin(json.optString("selectedSkin", null));

        } catch (Exception e) {

        }

        return state;
    }


    // !!!!!
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


    // !!!!!
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


    // !!!!!
    // Guarda progreso del nivel
    public void saveLevel(){

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
            e.printStackTrace();
        }
    }


    // Guarda estado de los items de la tienda
    public void savePlayerShopState(PlayerShopState state) {
        try {
            path = "player_shop.json";
            JSONObject json = new JSONObject();

            JSONArray purchased = new JSONArray(state.getPurchasedItems());
            json.put("purchased", purchased);

            json.put("selectedTower", state.getSelectedTowerId());
            json.put("selectedSkin", state.getSelectedSkinId());

            file.saveDataWithHash(path, json);

        } catch (Exception e) {

        }
    }

    // !!!!!
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

    // !!!!!
    // Guarda número de diamantes
    public void saveDiamonds(int diamonds)
    {
        try {
            path = "gameData.json";
            JSONObject jsonObject = file.loadDataWithHash(path);
            jsonObject.put("diamonds", diamonds);
            file.saveDataWithHash(path, jsonObject);
        } catch (Exception e) {

        }
    }

    // !!!!!
    // Guarda theme actual del juego
    public void saveTheme(int backgroundColor, int buttonColor, int buttonColor2)
    {
        try {
            path = "gameData.json";
            JSONObject jsonObject = file.loadDataWithHash(path);
            jsonObject.put("backgroundColor", backgroundColor);
            jsonObject.put("buttonColor", buttonColor);
            jsonObject.put("buttonColor2", buttonColor2);
            file.saveDataWithHash(path, jsonObject);
        } catch (Exception e) {

        }
    }

    // Devuelve la información del nivel
    public LevelData levelInfo(JSONObject jsonObject, int _world, int _level, boolean isFromFiles)
    {
        try{
            JSONArray wavesJson = jsonObject.getJSONArray("waves");

            ArrayList<String> waveTypes = new ArrayList<>();
            ArrayList<Integer> waveAmounts = new ArrayList<>();

            for (int i = 0; i < wavesJson.length(); i++) {
                JSONObject waveObj = wavesJson.getJSONObject(i);

                if (waveObj.has("goblin")) {
                    waveTypes.add("goblin");
                    waveAmounts.add(waveObj.getInt("goblin"));
                } else if (waveObj.has("orc")) {
                    waveTypes.add("orc");
                    waveAmounts.add(waveObj.getInt("orc"));
                }
            }

            int reward = jsonObject.getInt("reward");

            JSONArray roadJson = jsonObject.getJSONArray("road");
            ArrayList<String> road = new ArrayList<>();
            for (int i = 0; i < roadJson.length(); i++) {
                road.add(roadJson.getString(i));
            }

            String levelBackground = jsonObject.getString("levelBackground");

            int score = 0;
            if (isFromFiles && jsonObject.has("score"))
                score = jsonObject.getInt("score");

            return new LevelData(
                    waveTypes,
                    waveAmounts,
                    reward,
                    road,
                    levelBackground,
                    score,
                    _world,
                    _level
            );
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

    // !!!!!
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

    private int parseColor(JSONObject obj, String key) {
        String value = obj.optString(key, "0");

        if (value.equals("0"))
            return 0;

        return (int) Long.parseLong(value.substring(2), 16);
    }

    public int getBackgroundColor()
    {
        if(shopManager.getSelectedSkin() != null)
            return shopManager.getSelectedSkin().getBackgroundColor();
        else
            return 0xffffffff;
    }

    public int getButtonColor()
    {
        if(shopManager.getSelectedSkin() != null)
            return shopManager.getSelectedSkin().getButtonColor();
        else
            return 0xff808080;
    }

    public int getButtonColor2()
    {
        if(shopManager.getSelectedSkin() != null)
            return shopManager.getSelectedSkin().getButtonColor2();
        else
            return 0xff9ce4f5;
    }


    public ShopManager getShopManager() { return shopManager; }

    public static String getPath() { return path; }


    //public void setThemes() {Theme.setThemes(themes);}

}
