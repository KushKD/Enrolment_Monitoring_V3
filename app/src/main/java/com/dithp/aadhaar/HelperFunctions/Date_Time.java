package com.dithp.aadhaar.HelperFunctions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by kuush on 8/11/2016.
 */
public class Date_Time {

    //Get Date For Format yyyy-MM-dd hh:mm:ss
    public static String Datetime()
    {
        Calendar c = Calendar .getInstance();
      //  System.out.println("Current time => "+c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mms");
        String formattedDate = df.format(c.getTime());
        return formattedDate;


    }

}
