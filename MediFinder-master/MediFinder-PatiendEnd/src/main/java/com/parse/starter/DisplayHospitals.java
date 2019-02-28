package com.parse.starter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class DisplayHospitals extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_hospitals);
        Intent intent = getIntent();

        String id = intent.getStringExtra("hospital");
        Log.i("String ",id);
        String[] nameID = id.split(" ");
        Log.i("String ",String.valueOf(nameID[1]));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Hospital");
        query.whereEqualTo("ID",String.valueOf(nameID[1]).trim());
        final TextView textView = (TextView)findViewById(R.id.details);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                    for(ParseObject object:objects){
                        String open = String.valueOf(object.get("open"));
                        Log.i("Open",open);
                        textView.setText(open);
                    }
                }
            }
        }});
    }
    public void mapLauncher(View view){
        Intent intent = getIntent();

        String id = intent.getStringExtra("hospital");
        Log.i("String ",id);
        String[] nameID = id.split(" ");
        Log.i("String ",String.valueOf(nameID[1]));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Hospital");
        query.whereEqualTo("ID",String.valueOf(nameID[1]).trim());
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        for(ParseObject object:objects){
                            Intent intent1=getIntent();
                            String currentLatitude =intent1.getStringExtra("Lat");
                            String currentLongitude = intent1.getStringExtra("Lon");
                             String hospitalLatitude = String.valueOf(object.get("Latitude"));
                            String hospitalLongitude =String.valueOf(object.get("Longitude"));
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+currentLatitude+","+currentLongitude+"&daddr="+ hospitalLatitude +","+ hospitalLongitude));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                            startActivity(intent);

                        }
                    }
                }
            }});

    }
}
