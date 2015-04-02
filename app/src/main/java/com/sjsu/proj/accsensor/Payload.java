package com.sjsu.proj.accsensor;

import java.util.HashMap;

/**
 * Created by ssrinidhi on 3/26/2015.
 */
public class Payload {
    private HashMap<String,String> queryParams;
    private HashMap<String,String> jsonBody;

    public void setQuery(HashMap<String,String> q){
        queryParams = (HashMap)q.clone();
    }
    public HashMap<String,String> getQueryParams(){
        return queryParams;
    }
    public void setJsonBody(HashMap<String,String> j){
        jsonBody = (HashMap)j.clone();
    }
    public HashMap<String,String> getJsonBody(){
        return jsonBody;
    }

    public Payload(){
        queryParams = new HashMap<String,String>();
        jsonBody = new HashMap<String,String>();
    }
}
