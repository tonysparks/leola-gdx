/*
 * see license.txt 
 */
package leola.gdx.gfx.effects;

import java.util.ArrayList;
import java.util.List;

import leola.gdx.game.TimeStep;
import leola.gdx.gfx.AnimatedImage;
import leola.gdx.gfx.RenderContext;
import leola.gdx.gfx.Renderable;

/**
 * A simple effects container.  Draws {@link AnimatedImage}s at a specific location
 * 
 * @author Tony
 *
 */
public class Effects implements Renderable {

    private List<Effect> effects;
    private List<Effect> finishedEffects;
    
    /**
     * 
     */
    public Effects() {
        this.effects = new ArrayList<Effect>();
        this.finishedEffects = new ArrayList<Effect>();
    }
    
    
    public void addEffect(Effect effect) {
        this.effects.add(effect);
    }
    
    
    public boolean isEmpty() {
        return this.effects.isEmpty();
    }
    
    public int size() {
        return this.effects.size();
    }
    
    public void clearEffects() {
        for(int i = 0; i < this.effects.size(); i++) {
            this.effects.get(i).destroy();
        }
        
        this.effects.clear();
        this.finishedEffects.clear();
    }
    
    /* (non-Javadoc)
     * @see leola.live.gfx.Renderable#render(leola.live.gfx.Canvas, leola.live.gfx.Camera, long)
     */
    @Override
    public void render(RenderContext context) {        
        int size = this.effects.size();
        for(int i = 0; i < size; i++) {
            Effect e = this.effects.get(i);
            e.render(context);
        }
    }
    
    /* (non-Javadoc)
     * @see leola.live.gfx.Renderable#update(leola.live.TimeStep)
     */
    @Override
    public void update(TimeStep timeStep) {
        this.finishedEffects.clear();
        int size = this.effects.size();
        for(int i = 0; i < size; i++) {
            Effect e = this.effects.get(i);
            e.update(timeStep);
            if(e.isDone()) {                
                e.destroy();
                this.finishedEffects.add(this.effects.get(i));
            }
        }
        
        this.effects.removeAll(this.finishedEffects);    
    }

}
