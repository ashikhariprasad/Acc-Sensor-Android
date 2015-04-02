package com.sjsu.proj.accsensor.UserManager;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sjsu.proj.accsensor.HomeActivity;
import com.sjsu.proj.accsensor.Payload;
import com.sjsu.proj.accsensor.PropertyManager;
import com.sjsu.proj.accsensor.ServerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by ashik on 3/26/2015.
 */
public class AsyncCheckLogin extends AsyncTask<String,Void,Void> {
    private Context mContext;
    private PropertyManager pManager;
    private Properties properties;
    private String serverUrl;
    private String serverPath;
    private JSONObject jsonObject;
    private String result;

    public AsyncCheckLogin(Context m){
        super();
        this.mContext = m;
    }
    @Override
    protected Void doInBackground(String... params){

        jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",params[0]);
            jsonObject.put("password",params[1]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pManager = new PropertyManager(mContext);
        properties = pManager.getProperties("acc_sensor.properties");
        serverUrl = properties.getProperty("serverUrl");
        serverPath = properties.getProperty("loginPath");

        ServerManager server = new ServerManager(serverUrl,serverPath,"",jsonObject);
        try{
            result = server.doPost();
            Log.d("RESULT", result);
        }
        catch(Exception e){
            Log.e("Error trying to Login",e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void r){
        if(result.equalsIgnoreCase("success")){
            Intent i = new Intent(mContext,HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        }
        else
            Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
    }
}
