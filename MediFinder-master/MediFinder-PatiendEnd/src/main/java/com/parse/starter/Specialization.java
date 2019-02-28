package com.parse.starter;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class Specialization extends AsyncTask<String, Void, String> {


    public String doInBackground(String... strings) {
        String result = "";

        URL url;

        HttpURLConnection urlConnection = null;
        String token=ApiSpecialist.token;
        Log.i("Hiiii12",String.valueOf(token));
        String s = "[";
        for (Map.Entry<String, String> entry : Emergency.Id.entrySet()){
            if(entry.getKey() != null)
            {
                s = s+entry.getValue();
                s=s+",";
            }
        }
        s=s.substring(0,s.length()-1);
        s=s+"]";
        try {


            url  = new URL("https://healthservice.priaid.ch/diagnosis/specialisations?symptoms="+s+"&gender=male&year_of_birth=1998&token="+token+"&format=json&language=en-gb");

            Log.i("URL",url.toString());

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.connect();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while (data != -1) {

                char current = (char) data;

                result += current;

                data = reader.read();
            }

            Log.i("URLContent", result);


            JSONArray jsonArray = new JSONArray(result);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}