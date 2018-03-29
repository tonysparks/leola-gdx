/*
 * see license.txt 
 */
package leola.gdx.sfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import leola.gdx.assets.AssetLoader;
import leola.gdx.assets.WatchedAsset;
import leola.gdx.game.AbstractLeolaLibrary;
import leola.gdx.game.Game;
import leola.vm.Leola;
import leola.vm.exceptions.LeolaRuntimeException;
import leola.vm.lib.LeolaLibrary;
import leola.vm.types.LeoNamespace;
import leola.vm.types.LeoObject;

/**
 * @author Tony
 *
 */
public class SfxLeolaLibrary extends AbstractLeolaLibrary implements LeolaLibrary {

    /**
     * 
     */
    public SfxLeolaLibrary(Game game) {
        super(game);
    }

    @Override
    public void init(Leola leola, LeoNamespace namespace) throws LeolaRuntimeException {
        LeoNamespace thisNS = leola.getOrCreateNamespace("sfx");
        leola.putIntoNamespace(this, thisNS);
        
        namespace.getScope().getNamespaceDefinitions().storeNamespace(thisNS);
    }
    
    private WatchedAsset<Sound> loadSound(String filename) {
        filename = getAbsoluteFilePath(filename);
        return this.getWatcher().loadAsset(filename, new AssetLoader<Sound>() {            
            @Override
            public Sound loadAsset(String filename) {
                return Gdx.audio.newSound(Gdx.files.internal(filename));
            }
        });
    }
    
    
    /**
     * Loads a sound
     * 
     * @param filename
     * @param onReloaded
     * @return the sound
     */
    public Sound loadSound(String filename, LeoObject onReloaded) {
        return this.loadSound(filename).onAssetChanged(sound -> {
            if(onReloaded != null) {
                LeoObject result = onReloaded.call(LeoObject.valueOf(sound));
                if(result.isError()) {
                    System.err.println("*** Error loading sound: " + result);
                }
            }
        }).touch().getAsset();
    }
}
