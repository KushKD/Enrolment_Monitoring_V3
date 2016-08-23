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
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dithp.aadhaar.Enum.TaskType;
import com.dithp.aadhaar.HelperFunctions.AppStatus;
import com.dithp.aadhaar.HelperFunctions.Date_Time;
import com.dithp.aadhaar.Interfaces.AsyncTaskListener;
import com.dithp.aadhaar.Modals.Aadhaar_Operator;
import com.dithp.aadhaar.Presentation.Custom_Dialog;
import com.dithp.aadhaar.Utils.Constants;
import com.dithp.aadhaar.Utils.Generic_Async_Post;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends Activity implements AsyncTaskListener {

   private Button button_submit;
   private EditText editText_phone_no, editText_total_enrollments, editText_name_operator,editText_et_enrolmentstationid,editText_et_updations;
    private TextView textView_Aanganwari , textView_IMEI, textView_Aadhaar;
    private String aanganwadi_Name,updations,enrolment_type, phoneNumber,totalEnrollments ,issuesNfeedbacks,name_operator,enrolment_Station_code, date,aadhaar_number, deviceID = null;
    private Spinner sp_issuesNfeedbacks ,editText_Aanganwadi_name, spinner_enrolment_type;
    private int backButtonCount = 0;
    LinearLayout L_Header;
    String HeaderColor;
    URL url;
    HttpURLConnection conn;
    StringBuilder sb = new StringBuilder();
    Boolean Flag_Initialize = false;
    Custom_Dialog CM = new Custom_Dialog();

    String Aadhaar = null;

    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1) {
            MainActivity.this.finish();
        }
        else
        {
            Toast.makeText(this, Constants.EXIT, Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        HeaderColor = bundle.getString("Color");

        SharedPreferences settings = getSharedPreferences(Constants.PREF_NAME, 0);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        //   boolean hasParkingSelected_ = settings.getBoolean("hasParkingSelected", false);
       // boolean has_Logged_IN = settings.getBoolean("hasLoggedIn",false);
       // String Color = settings.getString("Header_Color","#000000");
         Aadhaar = settings.getString("Aadhaar_Number","000000000000");


         Flag_Initialize = InitializeControls();

        if (Flag_Initialize){
            L_Header.setBackgroundColor(Color.parseColor(HeaderColor));
            button_submit.setBackgroundColor(Color.parseColor(HeaderColor));

            textView_Aanganwari.setText(Date_Time.Datetime());
            textView_Aadhaar.setText(Aadhaar);
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(MainActivity.TELEPHONY_SERVICE);
            String deviceIDGetting =telephonyManager.getDeviceId();
            textView_IMEI.setText(deviceIDGetting);
            button_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getdata();
                }
            });

          //  editText_Aanganwadi_name.requestFocus();

        }else{
            Toast.makeText(getApplicationContext(),Constants.Error1,Toast.LENGTH_LONG).show();
        }


    }

    private void getdata() {

        //Refresh Time
        Time_Async TA = new Time_Async();
        TA.execute();

        deviceID = textView_IMEI.getText().toString().trim();
        Log.e("deviceID",deviceID);
        date = textView_Aanganwari.getText().toString().trim();
        Log.e("date",date);
        aadhaar_number = textView_Aadhaar.getText().toString().trim();
        Log.e("aadhaar_number",aadhaar_number);
        name_operator = editText_name_operator.getText().toString().trim();
        Log.e("Name Operator",name_operator);
        phoneNumber = editText_phone_no.getText().toString().trim();
        Log.e("Phone Operator",phoneNumber);
        enrolment_Station_code = editText_et_enrolmentstationid.getText().toString();
        Log.e("Station Code",enrolment_Station_code);
        aanganwadi_Name = editText_Aanganwadi_name.getSelectedItem().toString().trim();
        Log.e("Anganwadi Name",aanganwadi_Name);
        enrolment_type = spinner_enrolment_type.getSelectedItem().toString().trim();
        Log.e("enrolment_type",enrolment_type);
        totalEnrollments =editText_total_enrollments.getText().toString().trim();
        Log.e("totalEnrollments",totalEnrollments);
        updations = editText_et_updations.getText().toString().trim();
        Log.e("updations",updations);
        issuesNfeedbacks = sp_issuesNfeedbacks.getSelectedItem().toString().trim();
        Log.e("issuesNfeedbacks",issuesNfeedbacks);

        Aadhaar_Operator aadhaar_operator = null;
       if(enrolment_Station_code.length() ==5){
           if(phoneNumber.length()!=0){
               if(phoneNumber.length()== 10) {
                   if(totalEnrollments.length()!=0) {
                       //Save Data Locally
                       try {
                           DatabaseHandler db = new DatabaseHandler(this);

                           aadhaar_operator = new Aadhaar_Operator();
                           aadhaar_operator.setTab_IMEI(deviceID);
                           aadhaar_operator.setDate(date);
                           aadhaar_operator.setAadhaar_No(aadhaar_number);
                           aadhaar_operator.setName(name_operator);
                           aadhaar_operator.setMobile_No(phoneNumber);
                           aadhaar_operator.setEnrolment_Station_ID(enrolment_Station_code);
                           aadhaar_operator.setAww_Pec_Mec_Phc_Chc_Rh_Zh_Name(aanganwadi_Name);
                           aadhaar_operator.setUpdations_Done(updations);
                           aadhaar_operator.setEnrolments_Done(totalEnrollments);
                           aadhaar_operator.setIssuesnFeedbacks(issuesNfeedbacks);
                           aadhaar_operator.setGEO_Lat("0");
                           aadhaar_operator.setGEO_Long("0");
                           aadhaar_operator.setEnrolment_Type(enrolment_type);
                           aadhaar_operator.setUpload_Status("Testing");
                           db.addContact(aadhaar_operator);
                           clearData();

                           CM.showDialog(MainActivity.this,"Data Saved Locally");
                       }catch(Exception e){
                           CM.showDialog(MainActivity.this,"Unable to Save Data. Something went wrong. Please restrat the application.");
                       }

                       if(AppStatus.getInstance(MainActivity.this).isOnline()) {
                           // PostData pd = new PostData();
                           // pd.execute(aanganwadi_Name, phoneNumber, totalEnrollments, issuesNfeedbacks, date, deviceID);
                          // String URL = Constants.URL_BASE;
                           new Generic_Async_Post(MainActivity.this, MainActivity.this, TaskType.SAVEDATA).execute(aadhaar_operator);

                           //Post Data
                       }else CM.showDialog(MainActivity.this,"Network isn't available");
                   }else CM.showDialog(MainActivity.this,Constants.Enroll);
               }else CM.showDialog(MainActivity.this,Constants.PError1);
           }else CM.showDialog(MainActivity.this,Constants.PError2);
       }else CM.showDialog(MainActivity.this,"Please enter a valid Enrolment Station ID");








    }

    private Boolean InitializeControls() {
        try{
        button_submit = (Button)findViewById(R.id.bt_submit_data);
            L_Header = (LinearLayout)findViewById(R.id.header);
        editText_Aanganwadi_name = (Spinner) findViewById(R.id.et_aanganwadi_center_name);
        editText_phone_no = (EditText)findViewById(R.id.et_phone_number);
        editText_total_enrollments = (EditText)findViewById(R.id.et_total_number_of_enrollments);
            editText_name_operator = (EditText)findViewById(R.id.et_name);
            editText_et_enrolmentstationid = (EditText)findViewById(R.id.et_enrolmentstationid);
            editText_et_updations = (EditText)findViewById(R.id.et_updations);
        sp_issuesNfeedbacks = (Spinner)findViewById(R.id.et_issuesnfeedbacks);
            spinner_enrolment_type = (Spinner)findViewById(R.id.sp_enrolment_type);
        textView_Aanganwari = (TextView)findViewById(R.id.et_aanganwadi_date);
        textView_IMEI = (TextView)findViewById(R.id.et_aanganwadi_IMEI);
       textView_Aadhaar = (TextView)findViewById(R.id.tv_Aadhaar);


        return true;
        }catch (Exception e){
            System.out.println(Constants.Error1 + e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public void onTaskCompleted(String result, TaskType taskType) {

        String finalResult = null;
        if(taskType == TaskType.SAVEDATA){
            JsonParser JP = new JsonParser();
            finalResult = JP.POST(result);
            if(finalResult.equalsIgnoreCase(Constants.Login_Success)){
                CM.showDialog(MainActivity.this,finalResult);
            }
            else{
                CM.showDialog(MainActivity.this,finalResult);
            }
        }else{
            CM.showDialog(MainActivity.this,result);
        }

    }



    private void clearData() {
        editText_total_enrollments.setText("");
        editText_phone_no.setText("");
        editText_name_operator.setText("");
        editText_et_enrolmentstationid.setText("");
        editText_et_updations.setText("");
    }




    class Time_Async extends AsyncTask<String,String,String>{

        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Updating time and Date ");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String dateGetting = df.format(Calendar.getInstance().getTime());
            return dateGetting;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            textView_Aanganwari.setText(s);
        }
    }
}
