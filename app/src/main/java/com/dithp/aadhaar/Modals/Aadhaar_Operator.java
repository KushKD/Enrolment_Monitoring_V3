package com.dithp.aadhaar.Modals;

import java.io.Serializable;

/**
 * Created by kuush on 8/10/2016.
 */
public class Aadhaar_Operator implements Serializable {




    public String getEnrolment_Type() {
        return Enrolment_Type;
    }

    public void setEnrolment_Type(String enrolment_Type) {
        Enrolment_Type = enrolment_Type;
    }





    public String getIssuesnFeedbacks() {
        return IssuesnFeedbacks;
    }

    public void setIssuesnFeedbacks(String issuesnFeedbacks) {
        IssuesnFeedbacks = issuesnFeedbacks;
    }



    public String getMobile_No() {
        return Mobile_No;
    }

    public void setMobile_No(String mobile_No) {
        Mobile_No = mobile_No;
    }

    public String getTab_IMEI() {
        return Tab_IMEI;
    }

    public void setTab_IMEI(String tab_IMEI) {
        Tab_IMEI = tab_IMEI;
    }

    public String getGEO_Long() {
        return GEO_Long;
    }

    public void setGEO_Long(String GEO_Long) {
        this.GEO_Long = GEO_Long;
    }

    public String getGEO_Lat() {
        return GEO_Lat;
    }

    public void setGEO_Lat(String GEO_Lat) {
        this.GEO_Lat = GEO_Lat;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUpload_Status() {
        return Upload_Status;
    }

    public void setUpload_Status(String upload_Status) {
        Upload_Status = upload_Status;
    }

    public String getUpdations_Done() {
        return Updations_Done;
    }

    public void setUpdations_Done(String updations_Done) {
        Updations_Done = updations_Done;
    }

    public String getEnrolments_Done() {
        return Enrolments_Done;
    }

    public void setEnrolments_Done(String enrolments_Done) {
        Enrolments_Done = enrolments_Done;
    }

    public String getAww_Pec_Mec_Phc_Chc_Rh_Zh_Name() {
        return Aww_Pec_Mec_Phc_Chc_Rh_Zh_Name;
    }

    public void setAww_Pec_Mec_Phc_Chc_Rh_Zh_Name(String aww_Pec_Mec_Phc_Chc_Rh_Zh_Name) {
        Aww_Pec_Mec_Phc_Chc_Rh_Zh_Name = aww_Pec_Mec_Phc_Chc_Rh_Zh_Name;
    }

    public String getEnrolment_Station_ID() {
        return Enrolment_Station_ID;
    }

    public void setEnrolment_Station_ID(String enrolment_Station_ID) {
        Enrolment_Station_ID = enrolment_Station_ID;
    }

    public String getAadhaar_No() {
        return Aadhaar_No;
    }

    public void setAadhaar_No(String aadhaar_No) {
        Aadhaar_No = aadhaar_No;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String Enrolment_Station_ID;
    public String Aww_Pec_Mec_Phc_Chc_Rh_Zh_Name;
    public String Enrolments_Done;
    public String Updations_Done;
    public String Upload_Status;
    public String Date;
    public String GEO_Lat;
    public String GEO_Long;
    public String Tab_IMEI;
    public String Name;
    public String Aadhaar_No;
    public String IssuesnFeedbacks;
    public String Mobile_No;
    public String Enrolment_Type;
}
