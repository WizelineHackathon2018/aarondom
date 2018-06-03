package com.aarondomo.wizeline.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getFormattedDate(Date date){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("ddMMMyyyy");
        String formattedDate = DATE_FORMAT.format(date);
        return formattedDate;
    }

    public static String getFormattedHour(Date date){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");
        String formattedDate = DATE_FORMAT.format(date);
        return formattedDate;
    }
}
