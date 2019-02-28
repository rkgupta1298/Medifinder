package com.parse.starter;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlacePicker;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

public class SignUp2Activity extends AppCompatActivity {

    public class SetTime implements View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener {

        private EditText editText;
        private Calendar myCalendar;
        private Context ctx;
        private CheckBox cb;

        public SetTime(EditText editText, Context ctx, CheckBox cb){
            this.editText = editText;
            this.ctx = ctx;
            this.editText.setOnFocusChangeListener(this);
            this.myCalendar = Calendar.getInstance();
            this.cb = cb;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                if(cb == null || cb.isChecked()){
                    int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                    int minute = myCalendar.get(Calendar.MINUTE);
                    new TimePickerDialog(this.ctx, this, hour, minute, false).show();
                }
            }
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            this.editText.setText(String.format("%02d:%02d", hourOfDay, minute));
        }

    }

    public class checkBoxLink implements View.OnClickListener{

        private CheckBox cb;
        private EditText ed1, ed2;

        public checkBoxLink(CheckBox cb, EditText ed1, EditText ed2){
            this.cb = cb;
            this.cb.setOnClickListener(this);
            this.ed1 = ed1;
            this.ed2 = ed2;
        }

        @Override
        public void onClick(View v){
            if(!cb.isChecked()){
                ed1.setText("");
                ed2.setText("");
            }
        }
    }

    CheckBox c1,c2,c3,c4;
    EditText t1, openTime, closeTime, co1, co2, co3, co4, cc1, cc2, cc3, cc4, phno;
    SetTime open, close, o1, o2, o3, o4, ct1, ct2, ct3, ct4;

    LocationManager locationManager;
    LocationListener locationListener;
    ParseObject object;
    String Lat, Long;
    TextView lat1, long1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,locationListener,null);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);
        c1 = (CheckBox)findViewById(R.id.checkBox1);
        c2 = (CheckBox)findViewById(R.id.checkBox2);
        c3 = (CheckBox)findViewById(R.id.checkBox3);
        c4 = (CheckBox)findViewById(R.id.checkBox4);
        phno = (EditText)findViewById(R.id.phno);
        lat1 = (TextView)findViewById(R.id.curLat);
        long1 = (TextView)findViewById(R.id.curLong);
        t1 = (EditText)findViewById(R.id.hospitalCode);
        openTime = (EditText)findViewById(R.id.openTime);
        openTime.setInputType(InputType.TYPE_NULL);
        closeTime = (EditText)findViewById(R.id.closeTime);
        closeTime.setInputType(InputType.TYPE_NULL);
        co1 = (EditText)findViewById(R.id.open1);
        co1.setInputType(InputType.TYPE_NULL);
        co2 = (EditText)findViewById(R.id.open2);
        co2.setInputType(InputType.TYPE_NULL);
        co3 = (EditText)findViewById(R.id.open3);
        co3.setInputType(InputType.TYPE_NULL);
        co4 = (EditText)findViewById(R.id.open4);
        co4.setInputType(InputType.TYPE_NULL);
        cc1 = (EditText)findViewById(R.id.close1);
        cc1.setInputType(InputType.TYPE_NULL);
        cc2 = (EditText)findViewById(R.id.close2);
        cc2.setInputType(InputType.TYPE_NULL);
        cc3 = (EditText)findViewById(R.id.close3);
        cc3.setInputType(InputType.TYPE_NULL);
        cc4 = (EditText)findViewById(R.id.close4);
        cc4.setInputType(InputType.TYPE_NULL);
        open = new SetTime(openTime,this, null);
        close = new SetTime(closeTime,this, null);
        o1 = new SetTime(co1,this, c1);
        o2 = new SetTime(co2,this, c2);
        o3 = new SetTime(co3,this, c3);
        o4 = new SetTime(co4,this, c4);
        ct1 = new SetTime(cc1,this, c1);
        ct2 = new SetTime(cc2,this, c2);
        ct3 = new SetTime(cc3,this, c3);
        ct4 = new SetTime(cc4,this, c4);
        checkBoxLink cl1 = new checkBoxLink(c1,co1,cc1), cl2 = new checkBoxLink(c2,co2,cc2), cl3 = new checkBoxLink(c3,co3,cc3), cl4 = new checkBoxLink(c4,co4,cc4);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.i("Location-Latitude", String.valueOf(location.getLatitude()));
                Log.i("Location-Longitude", String.valueOf(location.getLongitude()));
                Lat = String.valueOf(location.getLatitude());
                Long = String.valueOf(location.getLongitude());
                lat1.setText(Lat);
                long1.setText(Long);
                Toast.makeText(SignUp2Activity.this, "Location Updated", Toast.LENGTH_SHORT).show();
                locationManager.removeUpdates(this);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
            }


        };

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Hospital");
        query.whereEqualTo("name", ParseUser.getCurrentUser().getUsername());
        query.setLimit(1);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e == null){
                    Log.i("Parse stuff", Integer.toString(list.size()));
                    if(list.size() > 0){
                        Log.i("Parse stuff", "Found user");
                        object = list.get(0);
                        t1.setText(object.getString("ID"));
                        openTime.setText(object.getString("open"));
                        closeTime.setText(object.getString("close"));
                        c1.setChecked(object.getBoolean("b1"));
                        c2.setChecked(object.getBoolean("b2"));
                        c3.setChecked(object.getBoolean("b3"));
                        c4.setChecked(object.getBoolean("b4"));
                        Lat = object.getString("Latitude");
                        Long = object.getString("Longitude");
                        lat1.setText(object.getString("Latitude"));
                        long1.setText(object.getString("Longitude"));
                        co1.setText(object.getString("open1"));
                        co2.setText(object.getString("open2"));
                        co3.setText(object.getString("open3"));
                        co4.setText(object.getString("open4"));
                        cc1.setText(object.getString("close1"));
                        cc2.setText(object.getString("close2"));
                        cc3.setText(object.getString("close3"));
                        cc4.setText(object.getString("close4"));
                        phno.setText(object.getString("phno"));
                    }
                    else{
                        Log.i("Parse stuff","New User");
                        object = new ParseObject("Hospital");
                        getLoc(findViewById(R.id.outer));
                    }
                }
            }
        });
    }

    public void getLoc(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // we have permission!
            //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,locationListener,null);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    protected boolean checkUnique(String id){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Hospital");
        query.whereEqualTo("ID",id);
        query.whereNotEqualTo("name", ParseUser.getCurrentUser().getUsername());
        List<ParseObject> objs;
        try{
            objs = query.find();
        }
        catch(com.parse.ParseException e1){
            Log.i("Parse error",e1.getMessage());
            return false;
        }
        return objs.size() == 0;
    }

    protected boolean checkBoxVerify(){
        if(c1.isChecked()){
            if(co1.getText().toString().matches("") || cc1.getText().toString().matches("")){
                return true;
            }
        }
        if(c2.isChecked()){
            if(co2.getText().toString().matches("") || cc2.getText().toString().matches("")){
                return true;
            }
        }
        if(c3.isChecked()){
            if(co3.getText().toString().matches("") || cc3.getText().toString().matches("")){
                return true;
            }
        }
        if(c4.isChecked()){
            return co4.getText().toString().matches("") || cc4.getText().toString().matches("");
        }
        return false;
    }

    public void submitDetails(View view) {
        Boolean b1, b2, b3, b4;
        b1 = c1.isChecked();
        b2 = c2.isChecked();
        b3 = c3.isChecked();
        b4 = c4.isChecked();
        if(t1.getText().toString().matches("")){
            Toast.makeText(this, "Please Enter Hospital ID", Toast.LENGTH_SHORT).show();
        }
        else if(phno.getText().toString().matches("")){
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if(openTime.getText().toString().matches("")){
            Toast.makeText(this, "Please Enter Opening Time", Toast.LENGTH_SHORT).show();
        }
        else if(closeTime.getText().toString().matches("")){
            Toast.makeText(this, "Please Enter Closing Time", Toast.LENGTH_SHORT).show();
        }
        else if(checkBoxVerify()){
            Toast.makeText(this, "Please Enter the Opening and Closing Time for Relevant facilities", Toast.LENGTH_LONG).show();
        }
        else{
            if(checkUnique(t1.getText().toString())){
                object.put("name", ParseUser.getCurrentUser().getUsername());
                object.put("phno", phno.getText().toString());
                object.put("ID",t1.getText().toString());
                object.put("open",openTime.getText().toString());
                object.put("close",closeTime.getText().toString());

                object.put("b1",b1);
                object.put("b2",b2);
                object.put("b3",b3);
                object.put("b4",b4);

                object.put("open1",co1.getText().toString());
                object.put("close1",cc1.getText().toString());
                object.put("open2",co2.getText().toString());
                object.put("close2",cc2.getText().toString());
                object.put("open3",co3.getText().toString());
                object.put("close3",cc3.getText().toString());
                object.put("open4",co4.getText().toString());
                object.put("close4",cc4.getText().toString());

                object.put("Latitude",Lat);
                object.put("Longitude",Long);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Toast.makeText(SignUp2Activity.this, "Successful Update", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUp2Activity.this, HospitalScreenActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(SignUp2Activity.this, "Error! Please Submit Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else{
                Toast.makeText(SignUp2Activity.this, "Hospital ID already exists", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
