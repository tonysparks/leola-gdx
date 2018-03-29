/*
 * see license.txt 
 */
package leola.gdx.assets;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import leola.gdx.game.Updatable;

/**
 * @author Tony
 *
 */
public interface AssetWatcher extends Updatable {

    /**
     * Loads the Asset and marks it for being 'watched'; loads the asset
     * immediately.
     * 
     * @param filename
     * @param loader
     * @return the {@link WatchedAsset}
     * @throws IOException
     */
    public <T> WatchedAsset<T> loadAsset(String filename, AssetLoader<T> loader);
    
    /**
     * Loads the Asset and marks it for being 'watched'
     * 
     * @param filename
     * @param loader
     * @param load
     * @return
     */
    public <T> WatchedAsset<T> loadAsset(String filename, AssetLoader<T> loader, boolean load);

    /**
     * Adds a file changed listener
     * 
     * @param fileChangedListener
     */
    public void addFileChangedListener(Consumer<File> fileChangedListener);
    
    /**
     * Removes the file changed listener
     * 
     * @param fileChangedListener
     */
    public void removeFileChangedListener(Consumer<File> fileChangedListener);
    
    /**
     * Removes the asset from being watched
     * 
     * @param filename
     */
    public void removeWatchedAsset(String filename);

    /**
     * Clear all watched assets
     */
    public void clearWatched();

    /**
     * Starts watching
     */
    public void startWatching();

    /**
     * Stops watching
     */
    public void stopWatching();
    
    

}