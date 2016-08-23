package com.dithp.aadhaar.HP_AEMC;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.dithp.aadhaar.HelperFunctions.AppStatus;
import com.dithp.aadhaar.HelperFunctions.Date_Time;
import com.dithp.aadhaar.Utils.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kuush on 8/22/2016.
 */

public class GPS_Service extends Service {
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 10800000;  //10800000
    private static final float LOCATION_DISTANCE = 0;



    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }



        @Override
        public void onLocationChanged(final Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
          //  Log.e("Latitude", Double.toString(location.getLatitude()));
          //  Log.e("Longitude",Double.toString(location.getLongitude()));


            //send the data to server here at specific time
           /* final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {*/
                    sendDataToServer(location);
                 /*   handler.postDelayed(this, 120000); //now is every 2 minutes
                }
            }, 120000); //Every 120000 ms (2 minutes)*/

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    private void sendDataToServer(final Location location)  {

        //Check the Time and send send data at specific time

        if(AppStatus.getInstance(GPS_Service.this).isOnline()) {




            SharedPreferences settings = getSharedPreferences(Constants.PREF_NAME, 0);
            //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
            //   boolean hasParkingSelected_ = settings.getBoolean("hasParkingSelected", false);
            // boolean has_Logged_IN = settings.getBoolean("hasLoggedIn",false);
            // String Color = settings.getString("Header_Color","#000000");
            String Aadhaar = settings.getString("Aadhaar_Number", "000000000000");
            // do your thing here, such as execute AsyncTask or send data to server
            Log.e("Latitude inside timer", Double.toString(location.getLatitude()));
            Log.e("Longitude inside Timer", Double.toString(location.getLongitude()));
            Log.e("Date Time inside Timer", Date_Time.Datetime());
            Log.e("Aadhaar Number", Aadhaar);

            try {

                File root = new File(Environment.getExternalStorageDirectory(), "Notes");
                // if external memory exists and folder with name Notes

                File filepath = new File(root, "GPS_LOG.txt");  // file path to save
                FileWriter writer = new FileWriter(filepath, true);
                writer.append("\n\n"+ Double.toString(location.getLatitude())+"\t"+Double.toString(location.getLongitude())+"\t"+Date_Time.Datetime() + "\t"+ Aadhaar);
                writer.flush();
                writer.close();
                String m = "File generated with name GPS_LOG.txt";
               // result.setText(m);
                Log.e("File Created",m);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("File Created",e.getLocalizedMessage().toString());
            }
          //   "\n\n"+ Double.toString(location.getLatitude())+"\t"+Double.toString(location.getLongitude())+"\t"+Date_Time.Datetime() + "\t"+ Aadhaar



        }else{
            Log.e("Error and More Error","Internet not available.");
        }
          }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {


        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        }
    }

}