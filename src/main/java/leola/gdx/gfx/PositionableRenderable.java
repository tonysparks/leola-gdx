/*
 * see license.txt 
 */
package leola.gdx.gfx;

/**
 * @author Tony
 *
 */
public interface PositionableRenderable {

    public float getX();
    public float getY();
    
    void render(RenderContext context);
}
