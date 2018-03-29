/*
 * see license.txt 
 */
package leola.gdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;

import leola.gdx.game.Config;
import leola.gdx.game.Game;
import leola.gdx.game.Logger;
import leola.gdx.game.TimeStep;
import leola.gdx.gfx.RenderContext;
import leola.vm.types.LeoMap;
import leola.vm.types.LeoObject;

/**
 * @author Tony
 *
 */
public class ScriptScreen implements Screen {

    private LeoObject doUpdate, 
                      doRender,
                      
                      doTouchUp,
                      doTouchDown,
                      doTouchDragged,
                      doScrolled,
                      doMouseMoved,
                      doKeyUp,
                      doKeyDown,
                      doKeyTyped                      
                      ;
    
    private LeoObject canvas;
    
    private OrthographicCamera camera, hudCamera;
    private Matrix4 transform;
    
    private Config config;
    
    private LeoObject updateContext;
    
    /**
     * 
     */
    public ScriptScreen(Game game, LeoObject screen) {
        this.config = game.getConfig();
        
        this.transform = new Matrix4();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(true, config.gameWidth, config.gameHeight  * (h / w));     
        this.camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        this.camera.update();
        
        this.hudCamera = new OrthographicCamera();
        this.hudCamera.setToOrtho(true, w, h);     
        this.hudCamera.position.set(hudCamera.viewportWidth / 2f, hudCamera.viewportHeight / 2f, 0);
        this.hudCamera.update();
        
        this.canvas = LeoObject.valueOf(game.getRenderContext());
        
        this.updateContext = new LeoMap();
        this.updateContext.setObject("camera", LeoObject.valueOf(this.camera));
        
        
        this.doUpdate = screen.getObject("update");                
        this.doRender = screen.getObject("render");
        
        this.doTouchUp = screen.getObject("touchUp");
        this.doTouchDown = screen.getObject("touchDown");
        this.doTouchDragged = screen.getObject("touchDragged");
        this.doScrolled = screen.getObject("scrolled");
        this.doMouseMoved = screen.getObject("mouseMoved");
        this.doKeyUp = screen.getObject("keyUp");
        this.doKeyDown = screen.getObject("keyDown");
        this.doKeyTyped = screen.getObject("keyTyped");
        
        this.doUpdate = (LeoObject.isTrue(doUpdate) ? doUpdate : null);
        this.doRender = (LeoObject.isTrue(doRender) ? doRender : null);
        
        this.doTouchUp      = (LeoObject.isTrue(doTouchUp)      ? doTouchUp      : null);
        this.doTouchDown    = (LeoObject.isTrue(doTouchDown)    ? doTouchDown    : null);
        this.doTouchDragged = (LeoObject.isTrue(doTouchDragged) ? doTouchDragged : null);
        this.doScrolled     = (LeoObject.isTrue(doScrolled)     ? doScrolled     : null);
        this.doMouseMoved   = (LeoObject.isTrue(doMouseMoved)   ? doMouseMoved   : null);
        this.doKeyUp        = (LeoObject.isTrue(doKeyUp)        ? doKeyUp        : null);
        this.doKeyDown      = (LeoObject.isTrue(doKeyDown)      ? doKeyDown      : null);
        this.doKeyTyped     = (LeoObject.isTrue(doKeyTyped)     ? doKeyTyped     : null);
        
        Gdx.input.setInputProcessor(new InputProcessor() {

            @Override
            public boolean keyDown(int keycode) {
                if(doKeyDown != null) {
                    LeoObject result = doKeyDown.call(LeoObject.valueOf(keycode));
                    if(result.isError()) {
                        Logger.elog("*** Error keyDown() - " + result);
                    }
                    return result.isTrue();
                }                
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                if(doKeyUp != null) {
                    LeoObject result = doKeyUp.call(LeoObject.valueOf(keycode));
                    if(result.isError()) {
                        Logger.elog("*** Error keyDown() - " + result);
                    }
                    return result.isTrue();
                }                
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                if(doKeyTyped != null) {
                    LeoObject result = doKeyTyped.call(LeoObject.valueOf(character));
                    if(result.isError()) {
                        Logger.elog("*** Error keyDown() - " + result);
                    }
                    return result.isTrue();
                }                
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if(doTouchDown != null) {
                    LeoObject result = doTouchDown.call(LeoObject.valueOf(screenX), LeoObject.valueOf(screenY), LeoObject.valueOf(pointer), LeoObject.valueOf(button));
                    if(result.isError()) {
                        Logger.elog("*** Error keyDown() - " + result);
                    }
                    return result.isTrue();
                }                
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if(doTouchUp != null) {
                    LeoObject result = doTouchUp.call(LeoObject.valueOf(screenX), LeoObject.valueOf(screenY), LeoObject.valueOf(pointer), LeoObject.valueOf(button));
                    if(result.isError()) {
                        Logger.elog("*** Error keyDown() - " + result);
                    }
                    return result.isTrue();
                }                
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if(doTouchDragged != null) {
                    LeoObject result = doTouchDragged.call(LeoObject.valueOf(screenX), LeoObject.valueOf(screenY), LeoObject.valueOf(pointer));
                    if(result.isError()) {
                        Logger.elog("*** Error keyDown() - " + result);
                    }
                    return result.isTrue();
                }                
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                if(doMouseMoved != null) {
                    LeoObject result = doMouseMoved.call(LeoObject.valueOf(screenX), LeoObject.valueOf(screenY));
                    if(result.isError()) {
                        Logger.elog("*** Error keyDown() - " + result);
                    }
                    return result.isTrue();
                }                
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                if(doScrolled != null) {
                    LeoObject result = doScrolled.call(LeoObject.valueOf(amount));
                    if(result.isError()) {
                        Logger.elog("*** Error keyDown() - " + result);
                    }
                    return result.isTrue();
                }                
                return false;
            }
        });
    }

    @Override
    public void update(TimeStep timeStep) {
        if(this.doUpdate != null) {
            LeoObject result = this.doUpdate.call(this.updateContext);
            if(result.isError()) {
                Logger.elog("*** Error update() - " + result);
            }
        }
    }

    @Override
    public void render(RenderContext context) {
        
        this.camera.update();
        
        context.setProjectionTransform(this.camera.combined, this.transform);                
        if(this.doRender != null) {
            LeoObject result = this.doRender.call(this.canvas);
            if(result.isError()) {
                Logger.elog("*** Error render() - " + result);
            }
        }

        context.setProjectionTransform(this.hudCamera.combined, this.transform);        
        context.batch.begin();
        {
            // HUD
        }
        context.batch.end();
        
    }

    @Override
    public void resize(int width, int height) {
        this.camera.viewportWidth = config.gameWidth;
        this.camera.viewportHeight = config.gameHeight * height/width;
        this.camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        this.camera.update();
        
        
        this.hudCamera.viewportWidth = width;
        this.hudCamera.viewportHeight = height;
        this.hudCamera.position.set(hudCamera.viewportWidth / 2f, hudCamera.viewportHeight / 2f, 0);
        this.hudCamera.update();
    }

}
