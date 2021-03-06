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
public class AsyncCheckLogin extends AsyncTask<String,Void,JSONObject> {
    private Context mContext;
    private PropertyManager pManager;
    private Properties properties;
    private String serverUrl;
    private String serverPath;
    private JSONObject jsonObject;
    private String result;
    private JSONObject responseObject;

    public AsyncCheckLogin(Context m){
        super();
        this.mContext = m;
    }
    @Override
    protected JSONObject doInBackground(String... params){

        jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",Long.parseLong(params[0]));
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
            //result = server.doPost();
            responseObject = server.doPost();
            if(responseObject == null){
                responseObject = new JSONObject();
                responseObject.put("message","Unable to access server");
            }
            Log.d("RESULT", responseObject.toString());
        }
        catch(Exception e){
            Log.e("Error trying to Login",e.toString());
        }
        return responseObject;
    }

    @Override
    protected void onPostExecute(JSONObject r){
        String responseMessage = null;
        String userId = null;
        try {
            responseMessage = r.getString("message");
            userId = r.getString("customer_id");
            Log.d("responseMessage",responseMessage);
            Log.d("userId",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(responseMessage.equalsIgnoreCase("success")){
            Intent i = new Intent(mContext,HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("EXTRA_USER_ID",userId);
            mContext.startActivity(i);
        }
        else
           Toast.makeText(mContext, responseMessage, Toast.LENGTH_LONG).show();
    }
}
