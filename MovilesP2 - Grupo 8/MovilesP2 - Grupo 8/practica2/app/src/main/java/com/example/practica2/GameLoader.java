package com.example.practica2;

import android.util.Log;
import android.util.Pair;

import com.example.androidengine.AndroidFile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * GameLoader es la clase que gestiona el cargado y guardado de niveles.
 */
public class GameLoader {

    // AndroidFile.
    private AndroidFile file;

    // Variables de tienda.
    private ShopManager shopManager;
    private ShopCatalog shopCatalog;
    private PlayerShopState playerShopState;
    private ArrayList<ShopItemData> shopItems;

    // Estados de nivel
    private ArrayList<AdventureScene.LevelState> levelStates;

    ArrayList<Pair<String, Integer>> worlds;

    private int totalWorlds, totalLevels;

    // Ruta del archivo.
    private static String path;

    // Colores de los botones.
    private int colorUnlocked, colorLocked;
    private int backgroundColor, buttonColor, buttonColor2;

    /**
     * CONSTRUCTORA.
     * @param iFile
     */
    public GameLoader(AndroidFile iFile)
    {
        this.file = iFile;

        // String[] files = this.file.listFiles("levels/world1");
        // for (String f : files) {
           // Log.e("ASSETS", f);
        // }

        levelStates = new ArrayList<>();
        shopItems = new ArrayList<>();
        this.worlds = new ArrayList<>();

        this.totalLevels = 0;
        this.totalWorlds = 0;

        loadData();
    }

    /**
     * Método que carga variables.
     */
    private void loadData()
    {
        loadGenericData();
        loadShop();
        loadLevels();
    }

    private void loadLevels(){
        try{
            String path = "levels/world_config.json";
            JSONObject jsonObject = readJSONFromAssets(path);

            JSONArray worldArray = jsonObject.getJSONArray("worlds");

            this.totalWorlds = worldArray.length();

            for (int i = 0; i < worldArray.length(); i++) {
                JSONObject worldObj = worldArray.getJSONObject(i);

                Iterator<String> keys = worldObj.keys();
                if (keys.hasNext()) {
                    String worldName = keys.next();
                    this.worlds.add(new Pair<>(worldName, worldObj.getInt(worldName)));
                    this.totalLevels += worldObj.getInt(worldName);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Método que lee archivo .JSON de las carpetas del proyecto (ej. assets)
     * @param path
     * @return
     */
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

    /**
     * Método que carga nivel de las carpetas del proyecto (ej. assets).
     * @param _world
     * @param _level
     * @return
     */
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

    /**
     * Método que carga el estilo de los botones de los niveles.
     * @param world
     */
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

    /**
     * Método que carga las variables de la tienda.
     */
    private void loadShop()
    {
        this.shopItems = loadShopItems();
        this.shopCatalog = new ShopCatalog(shopItems);

        this.playerShopState = loadPlayerShopState();

        this.shopManager = new ShopManager(shopCatalog, playerShopState);
    }

    /**
     * Método que carga ítems de la tienda.
     * @return
     */
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
                items.add(new ShopItemData( id, type, cost, description, image, backgroundColor, buttonColor, buttonColor2));
            }
        }
        catch (Exception e) {

        }

        return items;
    }

    /**
     * Método que carga datso generales del almacenamiento interno (diamantes y theme guardado).
     */
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

    /**
     * Método que carga el nivel del almacenamiento interno.
     * @param _world
     * @param _level
     * @return
     */
    public LevelData loadLevelFromFiles(int _world, int _level)
    {
        String level = "level_" + _world + "_0" + _level;
        String path = level + ".json";

        JSONObject jsonObject = file.loadDataWithHash(path);
        if(jsonObject.length() == 0)
            return null;
        return levelInfo(jsonObject, _world, _level, true);
    }


    /**
     * Método que devuelve la información del nivel.
     * @param jsonObject
     * @param _world
     * @param _level
     * @param isFromFiles
     * @return
     */
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

    /**
     * Método que carga estado de los niveles del almacenamiento interno.
     * @return
     */
    public ArrayList<AdventureScene.LevelState> loadLevelStates() {

        JSONObject json = file.loadDataWithHash("levelsState.json");

        levelStates.clear();

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


    /**
     * Método que carga estado de los ítems de la tienda.
     */
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

    /**
     * Método que guarda el estado de los niveles.
     * @param states
     */
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

    /**
     * Método que guarda el estado de los ítems de la tienda.
     * @param state
     */
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

    /**
     * Método que carga los ítems comprados hasta el momento.
     * @return
     */
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

    /**
     * Método que guarda los diamantes.
     * @param diamonds
     */
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

    private int parseColor(JSONObject obj, String key) {
        String value = obj.optString(key, "0");

        if (value.equals("0"))
            return 0;

        return (int) Long.parseLong(value.substring(2), 16);
    }

    /*
    * GETTERS
    * */
    public int getBackgroundColor()
    {
        if(shopManager.getSelectedSkin() != null)
        {
            if(shopManager.getSelectedSkin().getBackgroundColor() != 0)
                return shopManager.getSelectedSkin().getBackgroundColor();
            else
                return 0xffffffff;
        }
        else
            return 0xffffffff;
    }

    public int getButtonColor()
    {
        if(shopManager.getSelectedSkin() != null)
        {
            if(shopManager.getSelectedSkin().getButtonColor() != 0)
                return shopManager.getSelectedSkin().getButtonColor();
            else
                return 0xff808080;
        }
        else
            return 0xff808080;
    }

    public int getButtonColor2()
    {
        if(shopManager.getSelectedSkin() != null)
        {
            if(shopManager.getSelectedSkin().getButtonColor2() != 0)
                return shopManager.getSelectedSkin().getButtonColor2();
            else
                return 0xff9ce4f5;
        }
        else
            return 0xff9ce4f5;
    }

    public int getPanelColor()
    {
        if(shopManager.getSelectedSkin() != null) {
            if(shopManager.getSelectedSkin().getButtonColor2() != 0)
                return shopManager.getSelectedSkin().getButtonColor2();
            else
                return 0xff79c8d7;
        }
        else
            return 0xff79c8d7;
    }

    public int getPanelButtonColor()
    {
        if(shopManager.getSelectedSkin() != null) {
            if(shopManager.getSelectedSkin().getButtonColor() != 0)
                return shopManager.getSelectedSkin().getButtonColor();
            else
                return 0xff01a9c9;
        }
        else
            return 0xff01a9c9;
    }

    public int get_unlocked() { return colorUnlocked; }
    public int get_locked() { return colorLocked; }
    public ShopManager getShopManager() { return shopManager; }

    public int get_totalLevels() { return this.totalLevels; }
    public int get_totalWorlds() { return this.totalWorlds; }
    public ArrayList<Pair<String, Integer>> get_levels() { return this.worlds; }

}
