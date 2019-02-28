package com.parse.starter;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ApiSpecialist extends AsyncTask<String, Void, String> {

    public String uri;
    public String api_key;
    public String secret_key;
    public static String  token;
    public ArrayList<String> symptom=Emergency.SymList;


    public ApiSpecialist() {
        uri = "https://authservice.priaid.ch/login";
        api_key = "p9R4T_GMAIL_COM_AUT";
        secret_key = "b3YKt76Xmc2E5Gos8";
        Log.i("SkyMadarchodHai", "Sahi Mein HAi!");
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.i("SkyMadarchodHai", "Sahi Mein HAi 123!");
        byte[] secretBytes = secret_key.getBytes();
        String computedHashString = "";
        byte[] dataBytes = uri.getBytes();

        SecretKey keySpec = new SecretKeySpec(secret_key.getBytes(), "HmacMD5");
        try {
            //byte[] postData = ;

            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(keySpec);
            byte[] result = mac.doFinal(uri.getBytes());

            computedHashString = Base64.encodeToString(result, Base64.DEFAULT);

            HttpURLConnection urlConnection = null;
            URL myURL = new URL(uri);
            urlConnection = (HttpURLConnection) myURL.openConnection();

            String basicAuth = "Bearer " + api_key + ":" + computedHashString;
            urlConnection.setRequestProperty("Authorization", basicAuth);
            urlConnection.setRequestMethod("POST");

            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();
            String res = "";
            while (data != -1) {
                char current = (char) data;
                res += current;
                data = reader.read();
            }
            Log.i("Result", res);

            JSONObject full_token = new JSONObject(res);
            String final_token  = full_token.getString("Token");
            Log.i("Final_Token",final_token);
            token = final_token;
            Log.i("come",token);
            String api_url = "https://healthservice.priaid.ch/diagnosis/specialisations?symptoms=[10,188]&gender=male&year_of_birth=1998&token="+final_token+"&format=json&language=en-gb";

        }
        catch (MalformedURLException e2) {
            Log.i("MAlformed", e2.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.i("NO ALgorithm", e.toString());
        } catch (InvalidKeyException e1) {
            Log.i("InvalidKey", e1.toString());
        } catch (Exception e) {
            Log.i("Exception", e.toString());
        }
        return null;
    }

}
