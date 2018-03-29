/*
 * see license.txt 
 */
package leola.gdx.gfx;

import org.hjson.JsonValue;
import org.hjson.Stringify;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;

import leola.gdx.game.Logger;
import leola.gdx.gfx.Model.ModelData;

/**
 * @author Tony
 *
 */
public class Art {

    private static Gson gson = new Gson();
    
    public static Model loadModel(String path) {
        try {
            String json = JsonValue.readHjson(Gdx.files.internal(path).reader()).toString(Stringify.PLAIN);
            ModelData data = gson.fromJson(json, ModelData.class);
            return new Model(data);
        }
        catch(Exception e) {
            throw Logger.elog("Failed to load model: " + path, e);            
        }
    }
}
