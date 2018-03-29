/*
 * see license.txt 
 */
package leola.gdx.game;

import leola.vm.Leola;
import leola.vm.types.LeoMap;
import leola.vm.types.LeoNull;
import leola.vm.types.LeoObject;
import leola.vm.types.LeoString;

/**
 * Configuration 
 * 
 * @author Tony
 *
 */
public class Config {

    public static class VideoConfig {
        public boolean fullscreen = false;
        public boolean vSync = true;
        public int width = 800;
        public int height = 600;
    }
    
    public static class SoundConfig {
        public float musicVolume = 0.6f;
        public float gameVolume = 0.8f;
    }
            
    public String watchedDirectory = "./";
    
    /**
     * If this is in development mode
     */
    public boolean developmentMode = false;
    public String title = "Leola Gdx";
    public float gameWidth = 30f;
    public float gameHeight = 30f;
    
    public VideoConfig video = new VideoConfig();
    public SoundConfig sound = new SoundConfig();
    
    
    private LeoObject config;
    
    public Config(LeoObject config) {
        this.config = config;
        
        this.title = getStr(this.title, "title");
        this.watchedDirectory = getStr(this.watchedDirectory, "watchedDirectory");
        this.developmentMode = getBool(this.developmentMode, "developmentMode");
        this.gameWidth  = getFloat(this.gameWidth, "gameWidth");
        this.gameHeight = getFloat(this.gameHeight, "gameHeight");
        
        this.video.fullscreen = getBool(this.video.fullscreen, "video", "fullscreen");
        this.video.vSync = getBool(this.video.vSync, "video", "vSync");
        this.video.width = getInt(this.video.width, "video", "width");
        this.video.height = getInt(this.video.height, "video", "height");
        
        this.sound.musicVolume = getFloat(this.sound.musicVolume, "sound", "musicVolume");
        this.sound.gameVolume = getFloat(this.sound.gameVolume, "sound", "gameVolume");
    }
    
    /**
     * @param keys
     * @return the object defined by the set of keys
     */
    public LeoObject get(String ... keys) {
        int len = keys.length;
        
        LeoObject obj = config;
        for(int i = 0; i < len; i++) {
            LeoObject nextObj = obj.getObject(LeoString.valueOf(keys[i]));            
            if(!LeoObject.isNull(nextObj)) {
                obj = nextObj;
            }
            else return null;
        }
        
        return obj;
    }
    
    public float getFloat(String ...keys ) {
        return getFloat(0, keys);
    }
    
    public float getFloat(float defaultValue, String ...keys) {
        LeoObject obj = get(keys);
        if(obj != null && obj.isNumber()) {
            return obj.asFloat();
        }
        
        return defaultValue;
    }
    
    public int getInt(String ...keys ) {
        LeoObject obj = get(keys);
        return obj.asInt();
    }
    
    public int getInt(int defaultValue, String ...keys ) {
        LeoObject obj = get(keys);
        if(obj != null && obj.isNumber() ) {
            return obj.asInt();
        }
        
        return defaultValue;
    }
    
    public String getStr(String defaultValue, String ...keys ) {
        LeoObject obj = get(keys);
        if(obj != null) {
            return obj.toString();
        }
        
        return defaultValue;
    }
    
    public String getString(String ...keys ) {
        LeoObject obj = get(keys);
        return obj.toString();
    }
    
    public boolean getBool(boolean defaultValue, String ...keys) {
        LeoObject obj = get(keys);
        if(obj==null) {
            return defaultValue;
        }
        return LeoObject.isTrue(obj);
    }
    
    public boolean getBool(String ...keys ) {
        LeoObject obj = get(keys);
        return LeoObject.isTrue(obj);
    }
    
    /**
     * @param value
     * @param keys
     * @return sets a value defined by the set of keys
     */
    public boolean set(Object value, String ... keys) {
        int len = keys.length - 1;
        
        LeoObject obj = config;
        for(int i = 0; i < len; i++) {
            LeoObject nextObj = obj.getObject(keys[i]); 
            if(!LeoObject.isNull(nextObj)) {
                obj = nextObj;
            }
            else {
                LeoObject newEntry = new LeoMap();                
                obj.$sindex(LeoString.valueOf(keys[i]), newEntry);
                obj = newEntry;
            }
        }
        obj.$sindex(LeoString.valueOf(keys[keys.length-1]), LeoObject.valueOf(value));
        //obj.setObject(keys[keys.length-1], Leola.toLeoObject(value));
        return true;
    }
    
    public LeoObject setIfNull(String key, Object value) {
        LeoObject v = config.getObject(key); 
        if(v == null || v == LeoNull.LEONULL) {
            v = Leola.toLeoObject(value);
            config.setObject(key, v);
        }
        
        return v;
    }
    
    public boolean has(String key) {
        LeoObject v = config.getObject(key); 
        return v != null && v != LeoNull.LEONULL;
    }
    
}
