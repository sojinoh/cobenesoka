package com.example.mjami.a91won;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by mjami on 2/25/2017.
 */

public class LocationAsyncTask extends AsyncTask<String, Void, Boolean> {
    String host = "http://141.140.195.81:3002";
    String extension = "emergencyTransmission";
    @Override
    protected Boolean doInBackground(String[] params) {
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(host + "/" + extension);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
        } catch (Exception e){
            Log.d("url", e.getMessage());
        }

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "text/json");
        String body = "{\"location\":{\"latitude\":\"" + params[0] + "\",\"longitude\":\"" + params[1] + "\"},\"phone_number\":\"" + params[2] + "\"}";
        Log.d("url", body);
        try {
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(out, "UTF-8"));
            writer.write(body);
            writer.flush();
            writer.close();
            InputStream in = conn.getInputStream();
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            reader.beginObject();
            reader.nextName(); //should be "successful"
            boolean success = reader.nextBoolean();
            if(success) {
                Log.d("parsing", "success");
            }else{
                Log.d("parsing", "failure, just like mom always said");
            }
            return success;
        }catch(Exception e){
            Log.d("url", e.getMessage());
        }

        return false;
    }

}
