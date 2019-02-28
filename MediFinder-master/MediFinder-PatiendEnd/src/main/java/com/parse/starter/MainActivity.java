/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

  Intent mServiceIntent;
  private MyService mSensorService;
  Context ctx;
  public Context getCtx() {
    return ctx;
  }
  ParseUser currentUser = ParseUser.getCurrentUser();
  public void goToPatient(View view)
  {
    if(currentUser==null)
      startActivity(new Intent(MainActivity.this, PatientLogin.class));
    else
      startActivity(new Intent(MainActivity.this, Emergency.class));
  }
  public void goToHospital(View view)
    {
        if(currentUser==null)
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        else
            startActivity(new Intent(MainActivity.this, HospitalScreenActivity.class));
    }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ctx = this;
    setContentView(R.layout.activity_main);
    mSensorService = new MyService(getCtx());
    mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
    if (!isMyServiceRunning(mSensorService.getClass())) {
      startService(mServiceIntent);
    }
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }
  private boolean isMyServiceRunning(Class<?> serviceClass) {
    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        Log.i ("isMyServiceRunning?", true+"");
        return true;
      }
    }
    Log.i ("isMyServiceRunning?", false+"");
    return false;
  }


  @Override
  protected void onDestroy() {
    stopService(mServiceIntent);
    Log.i("MAINACT", "onDestroy!");
    super.onDestroy();

  }
}