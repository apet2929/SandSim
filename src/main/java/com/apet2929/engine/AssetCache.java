package com.apet2929.engine;

import com.apet2929.engine.model.ObjectLoader;
import com.apet2929.engine.model.Texture;

import java.util.HashMap;

class AssetManager {
//    Map of asset name to its resource path
    private HashMap<String, String> assetPaths;

    AssetManager() {
        assetPaths = new HashMap<>();
        assetPaths.put("NOT_FOUND", "textures/notfound.png");
        assetPaths.put("SAND", "textures/sand.png");
        assetPaths.put("TREE", "textures/tree.png");
        assetPaths.put("WATER", "textures/water.png");
    }

    String get_asset_path(String name) {
        if(this.assetPaths.get(name) == null) {
            return assetPaths.get("NOT_FOUND");
        }
        return assetPaths.get(name);
    }
}
public class AssetCache {
    private AssetManager assetManager;
    private HashMap<String, Texture> assets;
    private ObjectLoader loader;

    public AssetCache(ObjectLoader loader) throws Exception {
        this.loader = loader;
        this.assetManager = new AssetManager();
        this.assets = new HashMap<>();
        loadNotFoundTexture();
    }

    public Texture loadTexture(String name) {
        if(isTextureAlreadyLoaded(name)){
            return loadTextureFromMap(name);
        } else {
            System.out.println("name = " + name);
            return  loadTextureFromLoader(name);
        }
    }

    private boolean isTextureAlreadyLoaded(String name) {
        return this.assets.get(name) != null;
    }

    private Texture loadTextureFromMap(String name) {
        return assets.get(name);
    }

    private Texture loadTextureFromLoader(String name){
//        This should be called once I know the texture hasn't been loaded yet
        int id;
        try {
            id = loader.loadTexture(assetManager.get_asset_path(name));
            return new Texture(id);
        } catch (Exception e) {
            System.out.println("Texture: " + name + " not found!");
            return getNullTexture();
        }

    }

    private void loadNotFoundTexture() throws Exception{
        int not_found_id = loader.loadTexture(assetManager.get_asset_path("NOT_FOUND"));
        this.assets.put("NOT_FOUND", new Texture(not_found_id));
    }

    private Texture getNullTexture() {
        return assets.get("NOT_FOUND");
    }
}
