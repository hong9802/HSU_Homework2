package com.hansung.android.myapplication2;

import java.util.Calendar;

public class CalendarUtility {

    public String toDateString(Calendar calendar) {
        return calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
    }

    public String toTimeString(Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    public Calendar fromTimeString(String timeString) {
        String[] times = timeString.split(":");
        if (times.length >= 2) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR, Integer.parseInt(times[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
            return calendar;
        }
        return null;
    }

    public Calendar fromDateString(String timeString) {
        String[] dates = timeString.split("/");

        if (dates.length >= 3) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(dates[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(dates[1]) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[2]));
            return calendar;
        }

        return null;
    }
}