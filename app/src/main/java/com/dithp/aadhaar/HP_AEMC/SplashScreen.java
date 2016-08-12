package com.dithp.aadhaar.HP_AEMC;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.dithp.aadhaar.Utils.Constants;

public class SplashScreen extends Activity {

    protected boolean _active = true;
    protected int _splashTime = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


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
                    Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
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


}
