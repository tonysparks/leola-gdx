/*
 * see license.txt 
 */
package leola.gdx.game.screens;

import leola.gdx.game.TimeStep;
import leola.gdx.gfx.RenderContext;

/**
 * @author Tony
 *
 */
public interface Screen {
    public void update(TimeStep timeStep);
    public void render(RenderContext context);
    public void resize(int width, int height);
    
    default public void enter() {}
    default public void exit() {}
}
