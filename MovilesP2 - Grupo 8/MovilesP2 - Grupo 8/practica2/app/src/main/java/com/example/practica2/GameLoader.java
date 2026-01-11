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
    private ArrayList<ShopItemData> shopItems;
    private static String path;

    private int colorUnlocked, colorLocked;

    // Constructora de la clase
    public GameLoader(AndroidFile iFile)
    {
        this.file = iFile;

        String[] files = this.file.listFiles("levels/world1");
        for (String f : files) {
            Log.e("ASSETS", f);
        }

        levelStates = new ArrayList<>();
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
        String level = "";
        if (_level < 10){
            level = "level_" + _world + "_0" + _level;
        }
        else {
            level = "level_" + _world + "_" + _level;
        }
        String path = "levels/" + world + "/" + level + ".json";

        JSONObject jsonObject = readJSONFromAssets(path);
        if (jsonObject == null) return null;

        return levelInfo(jsonObject, _world, _level, false);
    }

    // Carga estilo de los botones de los niveles
    public void loadStyle(int world)
    {
        try{
            String path = "levels/world" + (world + 1) + "/style.json";
            JSONObject jsonObject = readJSONFromAssets(path);

            colorUnlocked = (int) Long.parseLong(jsonObject.getString("colorPassed").substring(2), 16);
            colorLocked = (int) Long.parseLong(jsonObject.getString("colorLocked").substring(2), 16);
        }
        catch (Exception e) {

        }
    }

    public int get_unlocked() { return colorUnlocked; }
    public int get_locked() { return colorLocked; }

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
                items.add(new ShopItemData( id, type, cost, description, image));
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
    public ArrayList<AdventureScene.LevelState> loadLevelStates() {

        JSONObject json = file.loadDataWithHash("levelsState.json");

        levelStates.clear();

        int totalWorlds = 2;
        int levelsPerWorld = 14;
        int totalLevels = totalWorlds * levelsPerWorld;

        if (json == null || json.length() == 0) {
            for (int i = 0; i < totalLevels; i++) {
                if (i == 0)
                    levelStates.add(AdventureScene.LevelState.UNLOCKED_INCOMPLETE);
                else
                    levelStates.add(AdventureScene.LevelState.LOCKED);
            }

            saveLevelsState(levelStates);
            return levelStates;
        }

        for (int i = 0; i < totalLevels; i++) {
            String key = "level_" + i;
            String value = json.optString(key, "LOCKED");

            switch (value) {
                case "UNLOCKED_INCOMPLETE":
                    levelStates.add(AdventureScene.LevelState.UNLOCKED_INCOMPLETE);
                    break;
                case "UNLOCKED_COMPLETED":
                    levelStates.add(AdventureScene.LevelState.UNLOCKED_COMPLETED);
                    break;
                default:
                    levelStates.add(AdventureScene.LevelState.LOCKED);
            }
        }

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

    // Guarda estado de los niveles (bloqueados, desbloqueados sin hacer, desbloqueados hechos)
    public void saveLevelsState(ArrayList<AdventureScene.LevelState> states) {
        JSONObject json = new JSONObject();

        try {
            for (int i = 0; i < states.size(); i++) {
                json.put("level_" + i, states.get(i).name());
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        file.saveDataWithHash("levelsState.json", json);
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
    public PlayerShopState getPlayerShopState() {
        path = "player_shop.json";
        PlayerShopState state = new PlayerShopState();

        try {
            JSONObject json = file.loadDataWithHash(path);
            if (json == null) {
                return state; // estado vacío por defecto
            }
            if (json.has("purchased")) {
                JSONArray purchased = json.getJSONArray("purchased");
                for (int i = 0; i < purchased.length(); i++) {
                    state.purchase(purchased.getString(i));
                }
            }
            if (json.has("selectedTower")) {
                state.selectTower(json.getString("selectedTower"));
            }
            if (json.has("selectedSkin")) {
                state.selectSkin(json.getString("selectedSkin"));
            }

        } catch (Exception e) {

        }

        return state;
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

    public int getBackgroundColor()
    {
        return 0xffffffff;
    }

    public int getButtonColor()
    {
        return 0xff808080;
    }

    public int getButtonColor2()
    {
        return 0xff9ce4f5;
    }

    public int getPanelColor()
    {
        return 0xff79c8d7;
    }

    public int getPanelButtonColor()
    {
        return 0xff01a9c9;
    }

    public ShopManager getShopManager() { return shopManager; }
}
