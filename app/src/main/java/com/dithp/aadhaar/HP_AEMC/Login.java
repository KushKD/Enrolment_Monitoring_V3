package com.dithp.aadhaar.HP_AEMC;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dithp.aadhaar.Enum.TaskType;
import com.dithp.aadhaar.HelperFunctions.AppStatus;
import com.dithp.aadhaar.Interfaces.AsyncTaskListener;
import com.dithp.aadhaar.Presentation.Custom_Dialog;
import com.dithp.aadhaar.Utils.Constants;
import com.dithp.aadhaar.Utils.Generic_Async_Get;

import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends Activity implements AsyncTaskListener {

    private int backButtonCount = 0;
    Boolean Flag_Initialize = false;
    private Button button_submit, button_getOTP;
    private EditText editText_aadhaarLogin, editText_otpLogin;
    private LinearLayout l1;
    private TextView tv_header;
    private  String aadhaar , otp = null;
    RelativeLayout Login_Header;

    URL url;
    HttpURLConnection conn;
    StringBuilder sb = new StringBuilder();
    String HeaderText;
    String HeaderColor;
    Custom_Dialog CM = new Custom_Dialog();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle bundle = getIntent().getExtras();
        HeaderColor = bundle.getString("Header");
         HeaderText = bundle.getString("Header_Text");

        Flag_Initialize = InitializeControls();
        if (Flag_Initialize){
            Login_Header.setBackgroundColor(Color.parseColor(HeaderColor));
            button_submit.setBackgroundColor(Color.parseColor(HeaderColor));
            button_getOTP.setBackgroundColor(Color.parseColor(HeaderColor));

            editText_aadhaarLogin.setHintTextColor(Color.parseColor("#000000"));
            editText_otpLogin.setHintTextColor(Color.parseColor("#000000"));
            tv_header.setText(HeaderText);

            button_getOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAadhaar();
                }
            });

            button_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOtpandAadhaa();
                }
            });

        }else{
            Toast.makeText(getApplicationContext(), Constants.Error1,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {

        EditText etAadhaar = (EditText)findViewById(R.id.et_aadhaar);
        String Save_Aadhaar = etAadhaar.getText().toString().trim();
        savedInstanceState.putString("AADHAAR", Save_Aadhaar);
        super.onSaveInstanceState(savedInstanceState);


    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        aadhaar = savedInstanceState.getString("AADHAAR");
        editText_aadhaarLogin.setText(aadhaar);
    }

    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1) {
            Login.this.finish();
        }
        else
        {
            Toast.makeText(this, Constants.EXIT, Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    private void getOtpandAadhaa() {

        otp = editText_otpLogin.getText().toString().trim();
        String aadhaar_a = editText_aadhaarLogin.getText().toString().trim();
        if(!otp.isEmpty()){
            if(otp.length()== 6){
                if(AppStatus.getInstance(Login.this).isOnline()) {
                   // OTP_Async OA = new OTP_Async();
                   // OA.execute(aadhaar_a, otp);
                    String url2 = null;
                    StringBuilder sb = new StringBuilder();
                    sb.append(Constants.url_Generic);
                    sb.append(Constants.url_Delemetre);
                    sb.append(Constants.methord_otp);
                    sb.append(Constants.url_Delemetre);
                    sb.append(aadhaar);
                    sb.append(Constants.url_Delemetre);
                    sb.append(otp);
                    url2 = sb.toString();
                    Log.e("OTP URL",url2);
                    new Generic_Async_Get(Login.this, Login.this, TaskType.OTP_USER).execute(url2);
                 } else {
                    CM.showDialog(Login.this,"Network isn't available");

            }
            }else{
                CM.showDialog(Login.this,Constants.OTPError1);
            }
        }else{
            CM.showDialog(Login.this,Constants.OTPError2);
        }

    }
    private void getAadhaar() {
        aadhaar = editText_aadhaarLogin.getText().toString().trim();
        if(!aadhaar.isEmpty() ){
            if(aadhaar.length() == 12 ){

if(AppStatus.getInstance(Login.this).isOnline()){

    String url = null;
    StringBuilder sb = new StringBuilder();
    sb.append(Constants.url_Generic);
    sb.append(Constants.url_Delemetre);
    sb.append(Constants.methord_Login);
    sb.append(Constants.url_Delemetre);
    sb.append(aadhaar);
    url = sb.toString();

    new Generic_Async_Get(Login.this, Login.this, TaskType.USER_LOGIN).execute(url);
     }
             else CM.showDialog(Login.this,"Network isn't available");
        }else CM.showDialog(Login.this,Constants.AError1);
        }else CM.showDialog(Login.this,Constants.AError2);
    }

    private Boolean InitializeControls() {
        try{
            button_submit = (Button)findViewById(R.id.bt_login);
            button_getOTP = (Button)findViewById(R.id.bt_getotp);
            editText_aadhaarLogin = (EditText)findViewById(R.id.et_aadhaar);
            editText_otpLogin = (EditText)findViewById(R.id.et_otp);
            l1 = (LinearLayout)findViewById(R.id.otp);
            Login_Header = (RelativeLayout)findViewById(R.id.header_login);
            tv_header = (TextView)findViewById(R.id.tvheader);
            return true;
        }catch (Exception e){
            System.out.println(Constants.Error1 + e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public void onTaskCompleted(String result, TaskType taskType) {

        String finalResult = null;
        if(taskType == TaskType.USER_LOGIN){
            JsonParser JP = new JsonParser();
            finalResult = JP.ParseString(result);
            if(finalResult.equalsIgnoreCase(Constants.Login_Success)){
                CM.showDialog(Login.this,finalResult);
                editText_aadhaarLogin.setEnabled(false);
                editText_otpLogin.setEnabled(true);
            }
            else{
                CM.showDialog(Login.this,finalResult);
            }

        }else if(taskType == TaskType.OTP_USER){


            JsonParser JP2 = new JsonParser();
            finalResult = JP2.ParseStringOTP(result);
            if(finalResult.equalsIgnoreCase(Constants.OTP_Successfull)){
                //Shared Prefrence
                SharedPreferences settings = getSharedPreferences(Constants.PREF_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();
                //Set "hasLoggedIn" to true
                editor.putBoolean("hasLoggedIn", true);
                editor.putString("Aadhaar_Number",aadhaar);
                editor.putString("Header_Color",HeaderColor);
                editor.putString("Header_Text",HeaderText);

                // Commit the edits!
                editor.commit();
                Intent i = new Intent(Login.this,MainActivity.class);
                i.putExtra("Color",  HeaderColor );
                startActivity(i);
                Login.this.finish();
            } else{
                CM.showDialog(Login.this,finalResult);
            }
        }else{
            CM.showDialog(Login.this,"Something went wrong");
        }
    }

}








