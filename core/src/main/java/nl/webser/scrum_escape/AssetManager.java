package nl.webser.scrum_escape;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.audio.Sound;

public class AssetManager {
    private static AssetManager instance;
    private final com.badlogic.gdx.assets.AssetManager manager;
    private TextureAtlas atlas;

    private AssetManager() {
        manager = new com.badlogic.gdx.assets.AssetManager();
        loadAssets();
    }

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    public void loadAssets() {
        // Load player animation frames
        manager.load("player1.png", Texture.class);
        manager.load("player2.png", Texture.class);
        manager.load("player3.png", Texture.class);
        manager.load("player4.png", Texture.class);
        
        // Load door textures
        manager.load("door_closed.png", Texture.class);
        manager.load("door_open.png", Texture.class);
        
        // Load monster texture
        manager.load("monster.png", Texture.class);
        
        // Load TIA challenge textures
        manager.load("transparency.png", Texture.class);  // Vergrootglas
        manager.load("inspection.png", Texture.class);    // Checklist
        manager.load("adaptation.png", Texture.class);    // Tandwiel
        
        // Load tilemap
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load("scrum.tmx", TiledMap.class);
        
        // Load sounds
        manager.load("correct.wav", Sound.class);
        manager.load("wrong.wav", Sound.class);
        manager.load("welcome.wav", Sound.class);
        manager.load("winner.wav", Sound.class);
        
        // Wait for all assets to load
        manager.finishLoading();
    }

    public Texture getTexture(String name) {
        return manager.get(name, Texture.class);
    }

    public TextureRegion getTextureRegion(String name) {
        if (atlas != null) {
            return atlas.findRegion(name);
        }
        return new TextureRegion(getTexture(name));
    }

    public TiledMap getTiledMap(String name) {
        return manager.get(name, TiledMap.class);
    }

    public Sound getSound(String name) {
        return manager.get(name, Sound.class);
    }

    public void dispose() {
        manager.dispose();
    }
} 