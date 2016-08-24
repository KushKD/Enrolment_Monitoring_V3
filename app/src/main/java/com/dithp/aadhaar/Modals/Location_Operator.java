package com.dithp.aadhaar.Modals;

import java.io.Serializable;

/**
 * Created by kuush on 8/24/2016.
 */
public class Location_Operator implements Serializable{

    public String Aadhaar_Operator;

    public String getAadhaar_Operator() {
        return Aadhaar_Operator;
    }

    public void setAadhaar_Operator(String aadhaar_Operator) {
        Aadhaar_Operator = aadhaar_Operator;
    }

    public String getDate_Time() {
        return Date_Time;
    }

    public void setDate_Time(String date_Time) {
        Date_Time = date_Time;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String Date_Time;
    public String Latitude;
    public String Longitude;

}
