/*
 * see license.txt 
 */
package leola.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import leola.gdx.game.screens.Screen;
import leola.gdx.game.screens.ScriptScreen;
import leola.vm.Leola;
import leola.vm.exceptions.LeolaRuntimeException;
import leola.vm.lib.LeolaLibrary;
import leola.vm.types.LeoNamespace;
import leola.vm.types.LeoObject;

/**
 * @author Tony
 *
 */
public class GdxLeolaLibrary implements LeolaLibrary {
    
    private Leola runtime;
    private Game game;
    private LwjglApplication app;
        
    private LeoNamespace namespace;
    
    @Override
    public void init(Leola runtime, LeoNamespace namespace) throws LeolaRuntimeException {
        this.runtime = runtime;
        this.namespace = namespace;
        
        runtime.putIntoNamespace(this, namespace);
    }

    
    /**
     * Start a game with the supplied configuration
     * 
     * @param configuration
     */
    public void start(LeoObject configuration) throws Exception {
        if(this.app == null) {
            this.app = DesktopLauncher.lauchGame(this.runtime, this.namespace, configuration);
            this.game = (Game) this.app.getApplicationListener();      
            
            while(!this.game.isCreated()) {
                Thread.sleep(50);
            }
        }
    }

    public void addScreen(String screenName, LeoObject screen) {
        Screen newScreen = new ScriptScreen(this.game, screen);
        this.game.addScreen(screenName, newScreen);
    }
    
    public void setScreen(String screenName) {
        this.game.setScreen(screenName);
    }
    
    
    public double deltaTime()  {
        return Game.step;
    }
    
    public boolean isKeyDown(int key) {
        return Gdx.input.isKeyPressed(key);
    }
    public boolean isButtonDown(int button) {
        return Gdx.input.isButtonPressed(button);
    }
    public int getMouseX() {
        return Gdx.input.getX();
    }
    public int getMouseY() {
        return Gdx.input.getY();
    }
    
    public Vector2 vec2(Float x, Float y) {
        if(x!=null && y != null)
            return new Vector2(x, y);
        return new Vector2();
    }
    public Vector3 vec3(Float x, Float y, Float z) {
        if(x!=null && y != null && z != null)
            return new Vector3(x, y, z);
        return new Vector3();
    }
    
    public Rectangle rect(Float x, Float y, Float w, Float h) {
        if(x!=null && y!=null) {
            return new Rectangle(x, y, w, h);
        }
        return new Rectangle();
    }
    
    public Color color(int rgba) {
        return new Color(rgba);
    }
    
       
    public int getWindowWidth() {
        return Gdx.graphics.getWidth();
    }
    
    public int getWindowHeight() {
        return Gdx.graphics.getHeight();
    }
}
