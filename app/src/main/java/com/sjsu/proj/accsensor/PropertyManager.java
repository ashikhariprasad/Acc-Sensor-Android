package com.sjsu.proj.accsensor;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ashik on 3/26/2015.
 */
public class PropertyManager {
    private Context context;
    private Properties properties;

    public PropertyManager(Context c) {
        this.context = c;
        properties = new Properties();
    }

    public Properties getProperties(String propertyFileName){
        try{
            AssetManager assetManager = context.getAssets();
            InputStream iStream = assetManager.open(propertyFileName);
            properties.load(iStream);
        }
        catch(IOException io){
            Log.e("AssetProReaderException",io.toString());
        }
        return properties;
    }

}
