/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class Main2Activity extends AppCompatActivity {

    protected boolean isCurrentUserHospital(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Hospital");
        query.whereEqualTo("name", ParseUser.getCurrentUser().getUsername());
        query.setLimit(1);
        return true;
    }

    public void goLog(View view){
        Intent intent;
        if(ParseUser.getCurrentUser() != null && isCurrentUserHospital()){
            Log.i("User123",ParseUser.getCurrentUser().toString());
            Log.i("User",ParseUser.getCurrentUser().getUsername());
            intent = new Intent(Main2Activity.this, HospitalScreenActivity.class);
        }
        else{
            intent = new Intent(Main2Activity.this, LoginActivity.class);
        }
        startActivity(intent);
    }

    public void goSign(View view) {
        Intent intent = new Intent(Main2Activity.this, SignUp1Activity.class);
        startActivity(intent);
    }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_1);
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}
