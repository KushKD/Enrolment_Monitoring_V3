package com.dithp.aadhaar.HP_AEMC;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.dithp.aadhaar.Utils.Constants;

public class SplashScreen extends Activity {

    protected boolean _active = true;
    protected int _splashTime = 3000;
    private int GPS_PERMISSION = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //First checking if the app is already having the permission
        if(!isGPSAllowed()){
            requestGPSPermission();
        }

        try {
            startService(new Intent(this, GPS_Service.class));
        }catch(Exception e){
            Log.e("GPS SERVICE NOT STARTED"," ERROR IN INITIALIZING THE LOCATION SERVICE");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



                SharedPreferences settings = getSharedPreferences(Constants.PREF_NAME, 0);
                //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
                //   boolean hasParkingSelected_ = settings.getBoolean("hasParkingSelected", false);
                boolean has_Logged_IN = settings.getBoolean("hasLoggedIn",false);
                String Color = settings.getString("Header_Color","#000000");
                String Aadhaar = settings.getString("Aadhaar_Number","000000000000");




              /*  if(hasParkingSelected_)
                {
                    //Parking_Details_Activity
                    Intent mainIntent = new Intent(Splash_Screen_Activity.this, Navigation_Drawer_Main_Activity.class);
                    Splash_Screen_Activity.this.startActivity(mainIntent);
                    Splash_Screen_Activity.this.finish();
                }else{
                    Intent loginIntent = new Intent(Splash_Screen_Activity.this, Main_Activity.class);
                    Splash_Screen_Activity.this.startActivity(loginIntent);
                    Splash_Screen_Activity.this.finish();
                }*/

                if(has_Logged_IN)
                {

                    //Parking_Details_Activity
                    Intent mainIntent = new Intent(SplashScreen.this, Main_Navigation_Activity.class);
                    mainIntent.putExtra("Color",  Color );
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();



                }else{
                    Intent login_Intent = new Intent(SplashScreen.this, Home.class); //Login_Activity
                    SplashScreen.this.startActivity(login_Intent);
                    SplashScreen.this.finish();

                }




            }
        }, 2000);


    }

    private void requestGPSPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},GPS_PERMISSION);
        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},GPS_PERMISSION);
    }

    //We are calling this method to check the permission status
    private boolean isGPSAllowed() {
       // String permission = "android.permission.ACCESS_FINE_LOCATION";
       // int res = this.checkCallingOrSelfPermission(permission);
       // return (res == PackageManager.PERMISSION_GRANTED);
        //Getting the permission status
         int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION );

        //If permission is granted returning true
          if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
         return false;
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if(requestCode == GPS_PERMISSION){
            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                Toast.makeText(this,"GPS Permission granted ",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the GPS permission",Toast.LENGTH_LONG).show();
            }
        }
    }


}
