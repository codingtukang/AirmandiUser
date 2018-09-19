package com.pasyappagent.pasy.component.util;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Dhimas on 10/6/17.
 */

public class DateHelper {
    private static final String TIME_ZONE = "UTC";



    /**
     * Get unix time until milisecond
     * @return datetime in unix
     */
    public static long getUnixTime() {
        Calendar nowDate = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE));
        nowDate.setLenient(false);
        nowDate.set(Calendar.MILLISECOND, 0);

        return nowDate.getTimeInMillis() / 1000;
    }
}
