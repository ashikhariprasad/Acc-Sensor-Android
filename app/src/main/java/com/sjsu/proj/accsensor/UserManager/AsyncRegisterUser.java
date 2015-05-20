package com.sjsu.proj.accsensor.UserManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sjsu.proj.accsensor.AsyncResponse;
import com.sjsu.proj.accsensor.LoginActivity;
import com.sjsu.proj.accsensor.PropertyManager;
import com.sjsu.proj.accsensor.RegisterActivity;
import com.sjsu.proj.accsensor.ServerManager;

import org.json.JSONObject;

import java.util.Properties;

/**
 * Created by ashik on 3/28/2015.
 */
public class AsyncRegisterUser extends AsyncTask<String,Void,Void>{

    private Context mContext;
    private PropertyManager pManager;
    private Properties properties;
    private String serverUrl;
    private String serverPath;
    private JSONObject jsonObject;
    private String[] keys = {"firstname","lastname","phone","password"};
    private JSONObject result;

    public AsyncResponse responseHandler = null;

    ProgressDialog proBar;

    public AsyncRegisterUser(Context c, AsyncResponse aResp){
        this.mContext = c;
        this.responseHandler = aResp;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        proBar = ProgressDialog.show(mContext,"Please wait...","Registering",true);
    }

    @Override
    protected Void doInBackground(String... params) {
        jsonObject = new JSONObject();
        if(params.length == keys.length){
            try {
                for (int i = 0; i < keys.length; i++) {
                    if(i==2)
                        jsonObject.put(keys[i],Long.parseLong(params[i]));
                    else
                        jsonObject.put(keys[i], params[i]);
                }
            }
            catch(Exception e){
                Log.e("Exception AsyncRegister",e.toString());
            }
            pManager = new PropertyManager(mContext);
            properties = pManager.getProperties("acc_sensor.properties");
            serverUrl = properties.getProperty("serverUrl");
            serverPath = properties.getProperty("registerPath");

            ServerManager server = new ServerManager(serverUrl,serverPath,"",jsonObject);
            try{
                result = server.doPost();
                Log.d("RESULT", result.toString());
            }
            catch(Exception e){
                Log.e("Error trying to Login",e.toString());
            }
        }
        else{
            Log.d("Exception AsyncRegister","Length of key != Length of params");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void r){
        responseHandler.postRegistrationHandler(result);
        proBar.dismiss();
    }

}
