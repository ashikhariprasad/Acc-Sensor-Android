package com.sjsu.proj.accsensor.UserManager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sjsu.proj.accsensor.AsyncResponse;
import com.sjsu.proj.accsensor.PropertyManager;
import com.sjsu.proj.accsensor.ServerManager;

import org.json.JSONObject;

import java.util.Properties;

/**
 * Created by Ashik on 4/16/2015.
 */
public class AsyncUpdateProfile extends AsyncTask<String,Void,JSONObject> {

    private Context mContext;
    private PropertyManager pManager;
    private Properties properties;
    private String serverUrl;
    private String serverPath;
    private JSONObject jsonObject;
    private String[] keys = {"name","age","gender","address",
    "econtact1Name","econtact1Phone","econtact1Email",
    "econtact2Name","econtact2Phone","econtact2Email",
    "econtact3Name","econtact3Phone","econtact3Email"};
    private String result;
    public AsyncResponse responseHandler = null;
    private JSONObject responseObject;

    public AsyncUpdateProfile(Context m, AsyncResponse r){
        this.mContext = m;
        this.responseHandler = r;
    }


    @Override
    protected JSONObject doInBackground(String... params) {

        jsonObject = new JSONObject();
        try {
            for (int i = 0; i < keys.length; i++) {
                if(i==1)
                    jsonObject.put(keys[i],Integer.parseInt(params[i]));
                else if(i == 5 || i == 8 || i == 11){
                    jsonObject.put(keys[i],Long.parseLong(params[i]));
                }
                else
                    jsonObject.put(keys[i], params[i]);
            }
        }
        catch(Exception e){
            Log.e("Exception AsyncRegister", e.toString());
        }
        pManager = new PropertyManager(mContext);
        properties = pManager.getProperties("acc_sensor.properties");
        serverUrl = properties.getProperty("serverUrl");
        serverPath = properties.getProperty("updateProfile");
        serverPath = serverPath.replace(":userId",params[params.length-1]);
        Log.d("serverUrl",serverUrl);
        Log.d("serverPath",serverPath);
        Log.d("JSONUpdate",jsonObject.toString());

        ServerManager server = new ServerManager(serverUrl,serverPath,"",jsonObject);
        try{
            //result = server.doPost();
            responseObject = server.doPut();
            Log.d("responseObject", responseObject.toString());
        }
        catch(Exception e){
            Log.e("Error trying to Update",e.toString());
        }

        return responseObject;
    }
    @Override
    protected void onPostExecute(JSONObject r){

        responseHandler.postRegistrationHandler(r);
    }
}
