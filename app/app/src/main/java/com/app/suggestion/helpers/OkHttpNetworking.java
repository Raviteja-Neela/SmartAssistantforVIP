package com.app.suggestion.helpers;


import android.os.AsyncTask;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpNetworking extends AsyncTask<String, Void, String> {

    private String textToProcess;

    public OkHttpNetworking(String textToProcess) {
        this.textToProcess = textToProcess;
    }

    private static final String api_key = "051f3d43-d0f2-44cd-9305-c28774e55ee1";

    public String callNamedERAPI(String extractedText){
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(mediaType, extractedText );
        Request request = new Request.Builder()
                .url("http://api.intellexer.com/recognizeNeText?apikey=" + api_key + "&loadNamedEntities=true")
                .post(body)
                .addHeader("cache-control", "no-cache")
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject Jobject = new JSONObject(response.body().string());
            JSONArray ErArray = Jobject.getJSONArray("entities");
            for(int i = 0; i < ErArray.length(); i++){
                JSONObject temp = ErArray.getJSONObject(i);
                if(temp.getInt("type") == 1){
                    return temp.getString("text");
                }
            }
            try {
                String[] arr = extractedText.split("\\s+");
                return arr[0] + " " + arr[1];
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        return callNamedERAPI(textToProcess);
    }
}