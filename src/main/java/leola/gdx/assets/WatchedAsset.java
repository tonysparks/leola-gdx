/*
 * see license.txt 
 */
package leola.gdx.assets;

import java.io.IOException;
import java.util.function.Consumer;


/**
 * The asset that is being watched
 * 
 * @author Tony
 *
 */
public interface WatchedAsset<T> {

    /**
     * Retrieve the Asset
     * 
     * @return the Asset in question
     */
    public T getAsset();
    
    
    /**
     * Release this asset
     */
    public void release();
   
    
    /**
     * The asset has changed on the file system
     * 
     * @throws IOException
     */
    public void assetChanged();
    
    /**
     * An asset has changed, set a listener
     * @param onChanged
     */
    public WatchedAsset<T> onAssetChanged(Consumer<T> onChanged);
    
    /**
     * Triggers the onAssetChanged callback
     * 
     * @return
     */
    public WatchedAsset<T> touch();
    
    /**
     * Triggers the onAssetChanged callback for the 
     * last asset changed listener
     * 
     * @return
     */
    public WatchedAsset<T> touchLast();
}
