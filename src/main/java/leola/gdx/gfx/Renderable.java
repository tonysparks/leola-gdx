/*
 * see license.txt 
 */
package leola.gdx.gfx;

import leola.gdx.game.Updatable;

/**
 * Something that can be rendered
 * 
 * @author Tony
 *
 */
public interface Renderable extends Updatable {

    void render(RenderContext context);
}
