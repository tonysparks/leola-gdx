/*
 * see license.txt 
 */
package leola.gdx.game;

import java.io.File;

import leola.gdx.assets.AssetWatcher;

/**
 * @author Tony
 *
 */
public abstract class AbstractLeolaLibrary {

    private AssetWatcher watcher;
    private File watchedDirectory;
    
    /**
     * 
     */
    protected AbstractLeolaLibrary(Game game) {
        this.watcher = game.getWatcher();
        this.watchedDirectory = game.getWatchedDirectory();
    }
    
    /**
     * @return the watchedDirectory
     */
    public File getWatchedDirectory() {
        return watchedDirectory;
    }
    
    /**
     * @return the watcher
     */
    public AssetWatcher getWatcher() {
        return watcher;
    }
    

    protected String getAbsoluteFilePath(String filename) {
        File file = new File(filename);
        if(file.exists()) {
            return filename;
        }
        
        file = new File(this.watchedDirectory, filename);
        if(file.exists()) {
            return file.getAbsolutePath();
        }
        
        throw new RuntimeException("The file '" + filename + "' was not found");       
    }
}
