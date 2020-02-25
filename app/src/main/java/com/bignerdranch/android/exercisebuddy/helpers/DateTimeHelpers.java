package com.bignerdranch.android.exercisebuddy.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeHelpers {
    public static String getTimeFromMilliseconds(long timeInMilliseconds){
        Date date = new Date(timeInMilliseconds);

        DateFormat formatter = new SimpleDateFormat("MM/dd/yy hh:mm a");

        return formatter.format(date);
    }
}
