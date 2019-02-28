package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class nearbyHospital extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //To Receive updated of Locations from Emergency clicked button
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_hospital);
        Intent intent2=getIntent();
        final String lat=intent2.getStringExtra("Latitude");
        final String lon=intent2.getStringExtra("Longitude");
        Log.i("Latitude nearby",String.valueOf(lat));
        Log.i("Longitude nearby",String.valueOf(lon));
        Toast.makeText(getApplicationContext(),lat, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),lon, Toast.LENGTH_SHORT).show();


        //List View for showing Hospitals

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Latlng");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    if (objects.size() > 0) {
                        hospitalDetails hospital;
                        ArrayList<hospitalDetails> hospitals = new ArrayList<hospitalDetails>();
                        HashMap<String, Double> hm = new HashMap<>();
                        final ListView hospitalView=(ListView)findViewById(R.id.nearby);
                        HashMap<HashMap<Double,Double>,Double> final_map = new HashMap<>();

                        for (ParseObject object : objects) {
                            double MyLatitude = Double.parseDouble(lat);
                            double MyLongitude = Double.parseDouble(lon);
                            String latitude = object.getString("Latitude");
                            String longitude = object.getString("Longitude");
                            int ID = object.getInt("ID");
                            String name = object.getString("name");
                            name = name+" "+Integer.toString(ID);

                            double newLatitude = Double.parseDouble(latitude);
                            double newLongitude = Double.parseDouble(longitude);
                            ParseGeoPoint point = new ParseGeoPoint(MyLatitude, MyLongitude);
                            ParseGeoPoint point2 = new ParseGeoPoint(newLatitude, newLongitude);

                            Double distance = point.distanceInMilesTo(point2);
                            hm.put(name, distance * 1.63);
                            HashMap<Double, Double> latlng = new HashMap<>();
                            latlng.put(newLatitude, newLongitude);
                            final_map.put(latlng,distance*1.63);

                        }

                        List<Map.Entry<String, Double>> list = new LinkedList<>(hm.entrySet());
                        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
                            public int compare(Map.Entry<String, Double> o1,
                                               Map.Entry<String, Double> o2) {
                                return (o1.getValue()).compareTo(o2.getValue());
                            }
                        });

                        HashMap<String, Double> temp = new LinkedHashMap<>();
                        for (Map.Entry<String, Double> aa : list) {
                            temp.put(aa.getKey(), aa.getValue());
                        }

                        //This sorted HashMap provides the distance of the hospital in the sorted order
                        HashMap<String,String> sorted = new LinkedHashMap<>();
                        for (Map.Entry<String, Double> en : temp.entrySet()) {
                            sorted.put(en.getKey(),Double.toString(en.getValue()));
                            Log.i("InformationNames", "Key = " + en.getKey() + ", Value = " + en.getValue());
                        }
                        for (Map.Entry<String, String> en : sorted.entrySet()) {
                            hospital = new hospitalDetails();
                            hospital.setName(en.getKey());
                            hospital.setDistance(en.getValue());
                            hospitals.add(hospital);
                            Log.i("InformationNames", "Key = " + en.getKey() + ", Value = " + en.getValue());
                        }
                        hospitalView.setAdapter(new MyAdapter(nearbyHospital.this, hospitals));
                        List<Map.Entry<HashMap<Double,Double>, Double>> newlist = new LinkedList<>(final_map.entrySet());
                        Collections.sort(newlist, new Comparator<Map.Entry<HashMap<Double,Double>, Double>>() {
                            public int compare(Map.Entry<HashMap<Double,Double>, Double> o1,
                                               Map.Entry<HashMap<Double,Double>, Double> o2) {
                                return (o1.getValue()).compareTo(o2.getValue());
                            }
                        });

                        HashMap<HashMap<Double,Double>, Double> temp1 = new LinkedHashMap<>();
                        for (Map.Entry<HashMap<Double,Double>, Double> aa : newlist) {
                            temp1.put(aa.getKey(), aa.getValue());
                        }

                        //This sorted HashMap provides the distance of the hospital in the sorted order
                        HashMap<HashMap<Double,Double>, String> final_sorted = new LinkedHashMap<>();
                        for (Map.Entry<HashMap<Double,Double>, Double> en : temp1.entrySet()) {
                            final_sorted.put(en.getKey(),Double.toString(en.getValue()));
                            Log.i("InformationLatLong", "Key = " + en.getKey() + ", Value = " + en.getValue());
                        }
                        //On ListView Item Click Listener
                        hospitalView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                Intent intent = new Intent(nearbyHospital.this,DisplayHospitals.class);
                                hospitalDetails h1= (hospitalDetails) hospitalView.getItemAtPosition(position);
                                String name = String.valueOf(h1.getName());
                                intent.putExtra("hospital",name);
                                intent.putExtra("Lat",lat);
                                intent.putExtra("Lon",lon);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        });
    }
}
