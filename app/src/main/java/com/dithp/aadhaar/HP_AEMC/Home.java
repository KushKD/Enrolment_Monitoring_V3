package com.dithp.aadhaar.HP_AEMC;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Home extends Activity {

    LinearLayout bt_Aww, bt_Pec , bt_Csc, bt_rh_zh, bt_phc_chc, bt_mobile;
    Boolean initialize_layout = false;

    String Header = null;
    String Color_Button = null;
    String Text_Color = null;
    String HeaderText = null;

    //Permision code that will be checked in the method onRequestPermissionsResult
    private int READ_STATE_PHONE = 23;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        initialize_layout = Layout_Initialize();
        if(initialize_layout){

            /**
             * CSC Button Click
             */
            bt_Csc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Header = "#9dbda9";
                   // Color_Button = "";
                   // Text_Color = "";
                    HeaderText = "CSC Login";

                    Intent intent = new Intent(Home.this,Login.class);
                    intent.putExtra("Header", Header);
                    intent.putExtra("Header_Text", HeaderText);
                   // intent.putExtra("Text_Color", Text_Color);
                    startActivity(intent);
                    Home.this.finish();
                }
            });


            /**
             * Aww Button Click
             */
             bt_Aww.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     Header = "#00ad96";
                     // Color_Button = "";
                     // Text_Color = "";
                     HeaderText = "Anganwadi Login";
                     Intent intent = new Intent(Home.this,Login.class);
                     intent.putExtra("Header", Header);
                     intent.putExtra("Header_Text", HeaderText);
                     // intent.putExtra("Text_Color", Text_Color);
                     startActivity(intent);
                     Home.this.finish();
                 }
             });

            /**
             * Pec Button Click
             */
            bt_Pec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Header = "#1595a6";
                    HeaderText = "PEC Login";
                    // Color_Button = "";
                    // Text_Color = "";
                    Intent intent = new Intent(Home.this,Login.class);
                    intent.putExtra("Header", Header);
                    intent.putExtra("Header_Text", HeaderText);
                    // intent.putExtra("Text_Color", Text_Color);
                    startActivity(intent);
                    Home.this.finish();
                }
            });

            /**
             * PHC/CHC Button Click
             */
            bt_phc_chc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Header = "#666666";
                    HeaderText = "PHC/CHC Login";
                    // Color_Button = "";
                    // Text_Color = "";
                    Intent intent = new Intent(Home.this,Login.class);
                    intent.putExtra("Header", Header);
                    intent.putExtra("Header_Text", HeaderText);
                    // intent.putExtra("Text_Color", Text_Color);
                    startActivity(intent);
                    Home.this.finish();
                }
            });

            /**
             * RH_ZH
             */
        bt_rh_zh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Header = "#695871";
                HeaderText = "RH/ZH Login";
                // Color_Button = "";
                // Text_Color = "";
                Intent intent = new Intent(Home.this,Login.class);
                intent.putExtra("Header", Header);
                intent.putExtra("Header_Text", HeaderText);
                // intent.putExtra("Text_Color", Text_Color);
                startActivity(intent);
                Home.this.finish();
            }
        });

            /**
             * MEC
             */
        bt_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Header = "#52715e";
                HeaderText = "Mobile Enrolment Center Login";
                // Color_Button = "";
                // Text_Color = "";
                Intent intent = new Intent(Home.this,Login.class);
                intent.putExtra("Header", Header);
                intent.putExtra("Header_Text", HeaderText);
                // intent.putExtra("Text_Color", Text_Color);
                startActivity(intent);
                Home.this.finish();
            }
        });




        }else{
            Toast.makeText(Home.this, "Problem in loading the Interface", Toast.LENGTH_SHORT).show();
        }

        //First checking if the app is already having the permission
        if(isReadStorageAllowed()){
            //If permission is already having then showing the toast
            Toast.makeText(Home.this,"You already have the STORAGE permission",Toast.LENGTH_LONG).show();
           // Toast.makeText(Home.this,"You already have the GPS permission",Toast.LENGTH_LONG).show();
            //Existing the method with return
            return;
        }

        //If the app has not the permission then asking for the permission
        requestStoragePermission();




    }



    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_PHONE_STATE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},READ_STATE_PHONE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == READ_STATE_PHONE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                Toast.makeText(this,"Storage Permission granted ",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the Storage permission",Toast.LENGTH_LONG).show();
            }
        }

    }

    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }



    /**
     * Initialize the Layout
     * @return
     */
    private Boolean Layout_Initialize() {

        try{
            bt_Csc = (LinearLayout)findViewById(R.id.csc_click);
            bt_Aww = (LinearLayout)findViewById(R.id.aww_click);
            bt_Pec = (LinearLayout)findViewById(R.id.pec_click);
            bt_rh_zh = (LinearLayout)findViewById(R.id.rh_zh_click);
            bt_phc_chc = (LinearLayout)findViewById(R.id.phc_chc_click);
            bt_mobile = (LinearLayout)findViewById(R.id.mobile_enrolment_center);
            return true;
        }catch(Exception e){
            return false;
        }

    }


}
