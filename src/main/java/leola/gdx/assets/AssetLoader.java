/*
 * see license.txt 
 */
package leola.gdx.assets;

/**
 * Loads an Asset
 * 
 * @author Tony
 */
public interface AssetLoader<T> {

    /**
     * Loads an Asset
     * 
     * @param filename
     * @return the Asset
     */
    public T loadAsset(String filename);
}
