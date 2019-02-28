package com.parse.starter;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fabio on 30/01/2016.
 */
public class MyService extends Service {
    public int counter=0;
    public MyService(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public MyService() {
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);

        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  "+ (counter++));
                if(ParseUser.getCurrentUser()!=null){
                final String use = ParseUser.getCurrentUser().getUsername();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Emergency");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null){
                            if(objects.size()>0){
                                Log.i("Hello","ff");
                                for(ParseObject object:objects){
                                    String id = object.getString("HospitalID");
                                    if(id.equals(use)){
                                        NotificationCompat.Builder nb = new NotificationCompat.Builder(MyService.this);
                                        nb.setContentText("Timer done");
                                        nb.setContentTitle("Hi!");
                                        nb.setSmallIcon(R.mipmap.ic_launcher);

                                        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        nm.notify(1,nb.build());
                                    }
                                }
                            }
                        }
                    }
                });
            }}
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
