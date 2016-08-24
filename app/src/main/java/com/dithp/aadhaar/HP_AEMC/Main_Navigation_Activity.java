package com.dithp.aadhaar.HP_AEMC;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main_Navigation_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AsyncTaskListener{

    private Button button_submit, logout;
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
    private static final int REQUEST_WRITE_STORAGE = 112;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__navigation_);



        Bundle bundle = getIntent().getExtras();
        HeaderColor = bundle.getString("Color");

        SharedPreferences settings = getSharedPreferences(Constants.PREF_NAME, 0);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        //   boolean hasParkingSelected_ = settings.getBoolean("hasParkingSelected", false);
        // boolean has_Logged_IN = settings.getBoolean("hasLoggedIn",false);
        // String Color = settings.getString("Header_Color","#000000");
        Aadhaar = settings.getString("Aadhaar_Number","000000000000");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(Main_Navigation_Activity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(Main_Navigation_Activity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Main_Navigation_Activity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            } else {
                //do something
            }
        } else {
            //do something
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);

        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/

        TextView Name_Manager_Tv = (TextView)header .findViewById(R.id.name);
        TextView Aadhaar_Manager_Tv = (TextView)header .findViewById(R.id.aadhaar);
        // Name_Manager_Tv.setText(name);
        Aadhaar_Manager_Tv.setText(Aadhaar);



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
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Clear the shared prefrences tab
                    //Shared Prefrence
                    SharedPreferences settings = getSharedPreferences(Constants.PREF_NAME, 0); // 0 - for private mode
                    SharedPreferences.Editor editor = settings.edit();
                    //Set "hasLoggedIn" to true
                    editor.putBoolean("hasLoggedIn", false);
                    editor.putString("Aadhaar_Number","000000000000");
                    editor.putString("Header_Color",HeaderColor);
                    // Commit the edits!
                    editor.commit();
                    //finish the activity
                    Main_Navigation_Activity.this.finish();
                }
            });

            //  editText_Aanganwadi_name.requestFocus();

        }else{
            Toast.makeText(getApplicationContext(),Constants.Error1,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main__navigation_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       /* if (id == R.id.nav_camera) {
            Toast.makeText(getApplicationContext(),"User profile will be available soon.",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(),"Reports Coming soon.",Toast.LENGTH_LONG).show();
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                            //Add Aadhaar , name,et_enrolmentstationid,updations,
                            db.addContact(aadhaar_operator);
                            clearData();

                            CM.showDialog(Main_Navigation_Activity.this,"Data Saved Locally");
                        }catch(Exception e){
                            CM.showDialog(Main_Navigation_Activity.this,"Unable to Save Data. Something went wrong. Please restrat the application.");
                        }

                        if(AppStatus.getInstance(Main_Navigation_Activity.this).isOnline()) {
                            // PostData pd = new PostData();
                            // pd.execute(aanganwadi_Name, phoneNumber, totalEnrollments, issuesNfeedbacks, date, deviceID);
                            // String URL = Constants.URL_BASE;
                            new Generic_Async_Post(Main_Navigation_Activity.this, Main_Navigation_Activity.this, TaskType.SAVEDATA).execute(aadhaar_operator);

                            //Post Data
                        }else CM.showDialog(Main_Navigation_Activity.this,"Network isn't available");
                    }else CM.showDialog(Main_Navigation_Activity.this,Constants.Enroll);
                }else CM.showDialog(Main_Navigation_Activity.this,Constants.PError1);
            }else CM.showDialog(Main_Navigation_Activity.this,Constants.PError2);
        }else CM.showDialog(Main_Navigation_Activity.this,"Please enter a valid Enrolment Station ID");








    }

    private Boolean InitializeControls() {
        try{
            logout = (Button)findViewById(R.id.logout);
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
            if(finalResult.equalsIgnoreCase(Constants.DATASENT)){
                CM.showDialog(Main_Navigation_Activity.this,finalResult);
            }
            else{
                CM.showDialog(Main_Navigation_Activity.this,finalResult);
            }
        }else{
            CM.showDialog(Main_Navigation_Activity.this,result);
        }

    }



    private void clearData() {
        editText_total_enrollments.setText("");
        editText_phone_no.setText("");
        editText_name_operator.setText("");
        editText_et_enrolmentstationid.setText("");
        editText_et_updations.setText("");
    }




    class Time_Async extends AsyncTask<String,String,String> {

        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Main_Navigation_Activity.this);
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
