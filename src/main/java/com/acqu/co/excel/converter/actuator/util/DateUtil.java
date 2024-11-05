package com.acqu.co.excel.converter.actuator.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getFormattedDateStr() {
        // Define the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

        // Get the current date
        Date now = new Date();

        // Format the date into the desired string format
        return dateFormat.format(now);
    }
}
