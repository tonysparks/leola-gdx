/*
 * The Seventh
 * see license.txt 
 */
package leola.gdx.gfx.effects;

import leola.gdx.gfx.Renderable;

/**
 * A visual effect
 * 
 * @author Tony
 *
 */
public interface Effect extends Renderable {

    /**
     * @return true if this effect is done
     */
    public boolean isDone();
    public default void onDone() {};    
    public void destroy();
}
