package com.sjsu.proj.accsensor.UserManager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sjsu.proj.accsensor.HomeActivity;
import com.sjsu.proj.accsensor.PropertyManager;
import com.sjsu.proj.accsensor.ServerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;

/**
 * Created by Ashik on 5/4/2015.
 */
public class AsycCallHelp extends AsyncTask<String,Void,JSONObject> {

    private Context mContext;
    private PropertyManager pManager;
    private Properties properties;
    private String serverUrl;
    private String serverPath;
    private JSONObject jsonObject;
    private JSONObject responseObject;
    private String userId;
    private String query;

    public AsycCallHelp (Context m){
        super();
        this.mContext = m;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        userId = params[6];
        jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude",params[0]);
            jsonObject.put("longitude",params[1]);
            jsonObject.put("country",params[2]);
            jsonObject.put("city",params[3]);
            jsonObject.put("postalCode",params[4]);
            jsonObject.put("addressLine",params[5]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        pManager = new PropertyManager(mContext);
        properties = pManager.getProperties("acc_sensor.properties");
        serverUrl = properties.getProperty("serverUrl");
        serverPath = properties.getProperty("accidentalert");
        serverPath = serverPath.replace(":userId",userId);
        Log.d("serverUrl", serverUrl);
        Log.d("serverPath",serverPath);
        Log.d("JSONData",jsonObject.toString());

        query = "latitude="+params[0]+"&longitude="+params[1];

        ServerManager server = new ServerManager(serverUrl,serverPath,query,jsonObject);

        try{
            responseObject = server.doGet();
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
        try {
            responseMessage = r.getString("message");
            Log.d("responseMessage",responseMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(responseMessage.equalsIgnoreCase("details sent to econtacts")){
            Toast.makeText(mContext, "Emergency Contacts Have Been Notified.", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(mContext, "Failed", Toast.LENGTH_LONG).show();
        }
    }
}
