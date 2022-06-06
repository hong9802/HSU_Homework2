package com.hansung.android.myapplication2;

public interface ICalendarUsage {

    void daySelected(int year, int month, int day);
    void dayDeselected();

    void hourSelected(int hour);
    void hourDeselected();

    void showDetail(Schedule schedule);
}
