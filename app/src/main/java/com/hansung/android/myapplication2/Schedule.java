package com.hansung.android.myapplication2;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class Schedule {
    private long id;
    private String title;
    private Calendar date;
    private Calendar startTime;
    private Calendar endTime;
    private String location;
    private String memo;
    private CalendarUtility calendarUtility = new CalendarUtility();

    public Schedule(long id, String title, Calendar date, Calendar startTime,
                    Calendar endTime, String location, String memo) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.memo = memo;
    }

    public Schedule(long id, String title, String date, String startTime,
                    String endTime, String location, String memo) {
        this.id = id;
        this.title = title;
        this.date = calendarUtility.fromDateString(date);
        this.startTime = calendarUtility.fromTimeString(startTime);
        this.endTime = calendarUtility.fromTimeString(endTime);
        this.location = location;
        this.memo = memo;
    }


    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    // format
    // "yyyy/mm/dd"
    public String getDate() {
        return calendarUtility.toDateString(date);
    }

    // format
    // "hh:mm"
    public String getStartTime() {
        return calendarUtility.toTimeString(startTime);
    }

    public String getEndTime() {
        return calendarUtility.toTimeString(endTime);
    }

    public String getLocation() {
        return location;
    }

    public String getMemo() {
        return memo;
    }
}
