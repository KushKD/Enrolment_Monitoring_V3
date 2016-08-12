package com.dithp.aadhaar.HTTP;

import com.dithp.aadhaar.Modals.Aadhaar_Operator;
import com.dithp.aadhaar.Utils.Constants;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kuush on 8/11/2016.
 */
public class HttpManager {


    public String GetData(String url) {
        System.out.print(url);
        BufferedReader reader = null;

        try {
            URL url_ = new URL(url);
            HttpURLConnection con = (HttpURLConnection) url_.openConnection();

            if (con.getResponseCode() != 200) {
                return "Timeout";
            }


            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            con.disconnect();
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Timeout";
        } finally {
            if (reader != null) {
                try {
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error";
                }
            }
        }
    }

    public String PostData_SaveData(Object... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;
        String URL = null;
        try {

            URL = Constants.URL_BASE;
            Aadhaar_Operator AO = (Aadhaar_Operator) params[0];
            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(10000);
            conn_.setReadTimeout(10000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();

             userJson = new JSONStringer()
                    .object().key("aww_user_details")
                    .object()
                    .key("Aanganwadi_Name").value(AO.getAww_Pec_Mec_Phc_Chc_Rh_Zh_Name())
                    .key("PhoneNumber").value(AO.getMobile_No())
                    .key("TotalEnrollments").value(AO.getEnrolments_Done())
                    .key("Issues_Feedbacks").value(AO.getIssuesnFeedbacks())
                    .key("EntryDate").value(AO.getDate())
                    .key("IMEINo").value(AO.getTab_IMEI())
                    .endObject()
                    .endObject();


            //  System.out.println(userJson.toString());
            //  Log.e("Object",userJson.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
            out.write(userJson.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(sb.toString());
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
    }




}