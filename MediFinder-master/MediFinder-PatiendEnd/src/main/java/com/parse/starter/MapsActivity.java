package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        try {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Latlng");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (e == null) {
                        if (objects.size() > 0) {

                            HashMap<String, Double> hm = new HashMap<>();

                            HashMap<HashMap<Double,Double>,Double> final_map = new HashMap<>();

                            for (ParseObject object : objects) {
                                double MyLatitude = 13.6299103;
                                double MyLongitude = 79.4728365;
                                String latitude = object.getString("Latitude");
                                String longitude = object.getString("Longitude");
                                int ID = object.getInt("ID");
                                String name = object.getString("name");
                                name = name+"  "+Integer.toString(ID);

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
                            HashMap<String,String> sorte= new LinkedHashMap<>();
                            for (Map.Entry<String, Double> en : temp.entrySet()) {
                                sorte.put(en.getKey(),Double.toString(en.getValue()));
                                Log.i("InformationNames", "Key = " + en.getKey() + ", Value = " + en.getValue());
                            }
                            for (Map.Entry<String, String> en : sorte.entrySet()) {
                                Log.i("InformationNames", "Key = " + en.getKey() + ", Value = " + en.getValue());
                            }
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
                        }
                    }
                }
            });
        }
        catch(NumberFormatException e1)
        {
            e1.printStackTrace();
        }

        LatLng tirupati = new LatLng(13.7154094, 79.5814953);
        mMap.addMarker(new MarkerOptions().position(tirupati).title("Marker in Tirupati"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tirupati));
    }


}