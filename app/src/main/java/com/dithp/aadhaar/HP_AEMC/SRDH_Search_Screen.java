package com.dithp.aadhaar.HP_AEMC;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.dithp.aadhaar.Presentation.Custom_Dialog;
import com.dithp.aadhaar.Utils.Constants;

public class SRDH_Search_Screen extends AppCompatActivity {

    ArrayAdapter<String> districts = null;
    private EditText name , fHname , pincode;
    private TextView date_of_birth;
    private EditText district;
    private Button SearchService;
    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;

    Custom_Dialog CM = new Custom_Dialog();

    private DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mSelectedDay = dayOfMonth;
            mSelectedMonth = monthOfYear;
            mSelectedYear = year;
            updateDateUI();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srdh__search__screen);

        district = (EditText)findViewById(R.id.edit_text_district);
        SearchService = (Button)findViewById(R.id.button_search);
        date_of_birth = (TextView)findViewById(R.id.edit_text_date_of_birth);
        name = (EditText)findViewById(R.id.edit_text_name);
        fHname = (EditText)findViewById(R.id.edit_text_father_husband_name);
        pincode = (EditText)findViewById(R.id.edit_text_pincode);

        date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(mSelectedYear, mSelectedMonth, mSelectedDay, mOnDateSetListener);
            }
        });

        SearchService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Get_Data();
                }catch (Exception e){
                   CM.showDialog(SRDH_Search_Screen.this,"Something Went Wrong. Please check your network connectivity");
                }
            }
        });
    }

    private void Get_Data() {
        String District_Service , DOB_Service , Name_Service , FHName_Service , PinCode_Service;
        District_Service = district.getText().toString().trim();
        DOB_Service = date_of_birth.getText().toString().trim();
        Name_Service = name.getText().toString().trim();
        FHName_Service = fHname.getText().toString().trim();
        PinCode_Service = pincode.getText().toString().trim();


        if( District_Service.length()!=0 || DOB_Service.length()!=0 || Name_Service.length()!=0|| FHName_Service.length() !=0 || PinCode_Service.length()!=0){



            //	if(PinCode_Service.length()!= 0 && PinCode_Service!= null){
            Intent i = new Intent(SRDH_Search_Screen.this , AdvancedListFive.class);
            i.putExtra(Constants.D, District_Service);
            i.putExtra(Constants.N, Name_Service);
            i.putExtra(Constants.FH, FHName_Service);
            i.putExtra(Constants.DOB, DOB_Service);
            i.putExtra(Constants.P,PinCode_Service);
            startActivity(i);

        }else{
            CM.showDialog(SRDH_Search_Screen.this,"Our Central System is as big as the universe. Please provide some parameter.");
        }
    }

    private DatePickerDialog showDatePickerDialog(int initialYear, int initialMonth, int initialDay, DatePickerDialog.OnDateSetListener listener) {
        DatePickerDialog dialog = new DatePickerDialog(SRDH_Search_Screen.this, listener, initialYear, initialMonth, initialDay);
        dialog.setTitle("Please Select Your Date Of Birth");
        dialog.show();
        return dialog;
    }

    private void updateDateUI() {
        String month = ((mSelectedMonth+1) > 9) ? ""+(mSelectedMonth+1): "0"+(mSelectedMonth+1) ;
        String day = ((mSelectedDay) < 10) ? "0"+mSelectedDay: ""+mSelectedDay ;
        date_of_birth.setText(day + "-" + month + "-" + mSelectedYear);
    }
}
