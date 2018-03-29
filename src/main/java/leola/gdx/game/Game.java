package leola.gdx.game;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

import leola.gdx.assets.AssetWatcher;
import leola.gdx.game.screens.Screen;
import leola.gdx.gfx.GfxLeolaLibrary;
import leola.gdx.gfx.RenderContext;
import leola.gdx.sfx.SfxLeolaLibrary;
import leola.gdx.sfx.Sounds;
import leola.gdx.util.EventDispatcher;
import leola.vm.Leola;
import leola.vm.exceptions.LeolaRuntimeException;
import leola.vm.types.LeoNamespace;
import leola.vm.types.LeoObject;

public class Game extends ApplicationAdapter {
    public static final String VERSION = "Alpha-0.0.1";
    
    private Leola runtime;
    
    private TimeStep timeStep;
    private long gameClock;

    private double currentTime;
    private double accumulator;
    public static final double step = 1.0 / 30.0;
    public static final long DELTA_TIME = 1000 / 30;

    private Screen currentScreen;
    private RenderContext context;
    
    private AssetWatcher watcher;
    private EventDispatcher dispatcher;
    
    private Config config;
    
    private File watchedDirectory;
    
    private Map<String, Screen> screens;
    
    private boolean isCreated;
    private LeoNamespace namespace;
    
    public Game(Leola runtime, LeoNamespace namespace, AssetWatcher watcher, Config config) {
        this.runtime = runtime;
        this.namespace = namespace;
        this.watcher = watcher;
        this.config = config;
        
        this.timeStep = new TimeStep();     
        this.dispatcher = new EventDispatcher();
        
        this.screens = new HashMap<>();        
        this.isCreated = false;
        
        this.watchedDirectory = new File(config.watchedDirectory);
        this.watcher.addFileChangedListener( changedFile -> {
            if(changedFile.getName().toLowerCase().endsWith(".leola")) {
                try {
                    Logger.log("Reevalutating: '" + changedFile + "'...");
                    runtime.eval(changedFile);
                    
                    
                    callFunction("reload");
                }
                catch(Exception e) {
                    Logger.elog("*** Error reloading script", e);
                }
                
            }
        });        
    }
    
    /**
     * @return the dispatcher
     */
    public EventDispatcher getDispatcher() {
        return dispatcher;
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
    
    /**
     * @return the config
     */
    public Config getConfig() {
        return config;
    }
    
    /**
     * @return the runtime
     */
    public Leola getRuntime() {
        return runtime;
    }
    
    /**
     * @return the isCreated
     */
    public boolean isCreated() {
        return isCreated;
    }
           
    @Override
    public void create() {             
        this.context = new RenderContext();
        this.context.batch = new SpriteBatch();
        this.context.textScaleFactor = 30 / Gdx.graphics.getHeight();
        
        //this.context.loadFont("./fonts/Consola.ttf", "Consola");
        //this.context.setDefaultFont("Consola", 12);        
        Sounds.init(this);        
                        
        this.runtime.loadLibrary(new GfxLeolaLibrary(this), this.namespace);
        this.runtime.loadLibrary(new SfxLeolaLibrary(this), this.namespace);
        
        callFunction("init");
        
        this.isCreated = true;
    }
    
    
    /**
     * @return the context
     */
    public RenderContext getRenderContext() {
        return context;
    }
    
    public void setScreen(String screenName) {
        Screen screen = this.screens.get(screenName);
        if(screen == null) {
            throw new LeolaRuntimeException("No screen by the name of '" + screenName +"' defined.");
        }
        
        setScreen(screen);
    }
    
    public void setScreen(Screen screen) {
        if(this.currentScreen != null) {
            this.currentScreen.exit();
        }
        
        this.currentScreen = screen;
        
        if(this.currentScreen != null) {
            this.currentScreen.enter();
        }
    }
    
    public void addScreen(String name, Screen screen) {
        this.screens.put(name, screen);
        
        if(this.screens.size() == 1) {
            setScreen(screen);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        double newTime = TimeUtils.millis() / 1000.0;
        double frameTime = Math.min(newTime - currentTime, 0.25);

        currentTime = newTime;
        accumulator += frameTime;

        while (accumulator >= step) {
            timeStep.setDeltaTime(DELTA_TIME);
            timeStep.setGameClock(gameClock);

            updateScreen(timeStep);

            accumulator -= step;
            gameClock += DELTA_TIME;
        }

        context.alpha = (float) (accumulator / step);
        renderScreen(context);
    }

    private void updateScreen(TimeStep timeStep) {
        this.watcher.update(timeStep);
        if(this.currentScreen != null) {
            this.currentScreen.update(timeStep);
        }
    }

    private void renderScreen(RenderContext context) {
        if(this.currentScreen != null) {
            this.currentScreen.render(context);
        }
    }

    @Override
    public void resize(int width, int height) {
        this.context.textScaleFactor = 30 * Gdx.graphics.getHeight();
        
        if(this.currentScreen != null) {
            this.currentScreen.resize(width, height);
        }
    }

    @Override
    public void dispose() {                
        callFunction("destroy");
        
        Sounds.destroy();
        this.watcher.stopWatching();
    }
    
    private void callFunction(String name) {
        LeoObject func = this.runtime.get(name);
        if(func != null) {
            LeoObject result = func.call();
            if(result.isError()) {
                Logger.elog("*** Error in " + name + "() - " + result);
            }
        }
    }
}
