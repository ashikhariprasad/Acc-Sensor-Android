package com.sjsu.proj.accsensor;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by ashik on 3/26/2015.
 */
public class ServerManager {
    private String url;
    private String path;
    private String query;
    private JSONObject json;
    private String finalUrl;
    private HttpResponse response;
    JSONObject resultJson;

    public ServerManager(String sUrl, String sPath, String sQuery, JSONObject jObj){
        this.url = sUrl;
        this.path = sPath;
        this.query = sQuery;
        this.json = jObj;
        if(query != ""){
            this.finalUrl = url+path+"?"+query;
        }
        else{
            this.finalUrl = url+path;
        }
    }

    public JSONObject doPost(){
        String jsonResult = "";
        String result = "";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(finalUrl);
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(json.toString());
            Log.d("JSONOBJECT",json.toString());
            stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(stringEntity);
            response = client.execute(post);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()),"UTF-8"),8);
            String line = "";

            while((line = br.readLine()) != null){
                jsonResult += line;
            }
            resultJson = new JSONObject(jsonResult);
            //result = resultJson.getString("message");
            client.getConnectionManager().shutdown();

        }
        catch(Exception e){
            Log.e("Error in doPost",e.toString());
        }

        return resultJson;
    }

    public JSONObject doPut(){
        String jsonResult = "";
        String result = "";
        HttpClient client = new DefaultHttpClient();
        HttpPut put = new HttpPut(finalUrl);
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(json.toString());
            Log.d("JSONOBJECT",json.toString());
            stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            put.setEntity(stringEntity);
            response = client.execute(put);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()),"UTF-8"),8);
            String line = "";

            while((line = br.readLine()) != null){
                jsonResult += line;
            }
            Log.d("jsonResult",jsonResult);
            resultJson = new JSONObject(jsonResult);
            Log.d("resultJson",resultJson.toString());
            //result = resultJson.getString("message");
            client.getConnectionManager().shutdown();

        }
        catch(Exception e){
            Log.e("Error in doPut",e.toString());
        }

        return resultJson;
    }

    public JSONObject doGet(){
        String jsonResult = "";
        String result = "";
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(finalUrl);

        try{
            response = client.execute(get);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()),"UTF-8"),8);
            String line = "";

            while((line = br.readLine()) != null){
                jsonResult += line;
            }
            Log.d("jsonResult",jsonResult);
            resultJson = new JSONObject(jsonResult);
            Log.d("resultJson",resultJson.toString());
            //result = resultJson.getString("message");
            client.getConnectionManager().shutdown();
        }
        catch(Exception e){
            Log.e("Error in doGet",e.toString());
        }

        return resultJson;
    }

}
