/*
 * see license.txt 
 */
package leola.gdx.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
public class GfxLeolaLibrary extends AbstractLeolaLibrary implements LeolaLibrary {

    /**
     * 
     */
    public GfxLeolaLibrary(Game game) {
        super(game);
    }

    @Override
    public void init(Leola leola, LeoNamespace namespace) throws LeolaRuntimeException {
        LeoNamespace thisNS = leola.getOrCreateNamespace("gfx");
        leola.putIntoNamespace(this, thisNS);

        namespace.getScope().getNamespaceDefinitions().storeNamespace(thisNS);
    }

    private WatchedAsset<TextureRegion> loadTexture(ImageData image) {
        String filename = getAbsoluteFilePath(image.filename);
        if (image.width > 0 && image.height > 0) {
            return loadTexture(filename, image.x, image.y, image.width, image.height);
        }
        return loadTexture(filename);
    }

    private WatchedAsset<TextureRegion> loadTexture(String filename) {
        filename = getAbsoluteFilePath(filename);
        return getWatcher().loadAsset(filename, TextureUtil::loadImage);
    }

    private WatchedAsset<TextureRegion> loadTexture(String filename, int x, int y, int width, int height) {
        filename = getAbsoluteFilePath(filename);
        return getWatcher().loadAsset(filename, file -> TextureUtil.subImage(TextureUtil.loadImage(file), x, y, width, height));
    }

    /**
     * Loads a texture, possible values:
     * 
     * <code>
     *  gdx:gfx:loadTexture("/path/to/image.jpg")
     *  gdx:gfx:loadTexture({
     *      filename -> "/path/to/image.jpg",
     *      x -> 0,
     *      y -> 0,
     *      width -> 32,
     *      height -> 32
     *  })
     *  gdx:gfx:loadTexture({
     *      filename -> "/path/to/image.jpg",
     *      x -> 0,
     *      y -> 0,
     *      width -> 32,
     *      height -> 32
     *  }, def(tex) {
     *      // callback of the new texture that is loaded upon file
     *      // modification detection
     *  })
     * </code>
     * 
     * @param imageData
     * @param onReloaded
     * @return
     */
    public TextureRegion loadTexture(LeoObject imageData, LeoObject onReloaded) {
        WatchedAsset<TextureRegion> asset = null;
        if (imageData.isString()) {
            asset = loadTexture(imageData.toString());
        } else {
            ImageData data = new ImageData();
            data.filename = imageData.getObject("filename").toString();

            data.x = imageData.hasObject("x") ? imageData.getObject("x").asInt() : 0;
            data.y = imageData.hasObject("y") ? imageData.getObject("y").asInt() : 0;

            data.width = imageData.hasObject("width") ? imageData.getObject("width").asInt() : 0;
            data.height = imageData.hasObject("height") ? imageData.getObject("height").asInt() : 0;

            asset = loadTexture(data);
        }

        return asset.onAssetChanged(tex -> {
            if (onReloaded != null) {
                LeoObject result = onReloaded.call(LeoObject.valueOf(tex));
                if (result.isError()) {
                    System.err.println("*** Error loading image: " + result);
                }
            }
        }).touch().getAsset();
    }

    public Sprite toSprite(TextureRegion tex) {
        return new Sprite(tex);
    }

    public TextureRegion toRegion(TextureRegion tex, int x, int y, int width, int height) {
        return new TextureRegion(tex, x, y, width, height);
    }

    public GL20 gl() {
        return Gdx.gl;
    }

    public GL20 gl20() {
        return Gdx.gl20;
    }

    public GL30 gl30() {
        return Gdx.gl30;
    }
}
