package com.hansung.android.myapplication2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Calendar;

public class MonthViewFragmentStateAdapter extends FragmentStateAdapter {

    private static int NUM_ITEMS=100;

    public MonthViewFragmentStateAdapter(FragmentActivity fa) {
        super(fa);
    }

    // 각 페이지를 나타내는 프래그먼트 반환
    @Override
    public Fragment createFragment(int position) {
        /*
        switch (position) {
            case 0:
                FragmentMonthCalendar prevM = new FragmentMonthCalendar();
                MonthViewFragment prevmonth = new MonthViewFragment();
                prevmonth = prevM.nextMonth.newInstance(-1);

                return prevmonth;
            case 2:
                FragmentMonthCalendar nextM = new FragmentMonthCalendar();
                MonthViewFragment nextmonth = new MonthViewFragment();
                nextmonth = nextM.nextMonth.newInstance(1);

                return nextmonth;
            default:
                return null;
        }*/
        MonthViewFragment month = new MonthViewFragment();;
        if(position == 50){
            return month;
        }
        else{
            FragmentMonthCalendar nextM = new FragmentMonthCalendar();
            MonthViewFragment newmonth = new MonthViewFragment();
            newmonth = nextM.nextMonth.newInstance(position - 50);

            return newmonth;
        }
    }

    // 전체 페이지 개수 반환
    @Override
    public int getItemCount() {
        return NUM_ITEMS;
    }
    /*
    public Calendar getCurrentMonth(int position){
        MonthViewFragment Today = new MonthViewFragment();
        Today.newInstance(position - 50);
        Calendar today = (Calendar)Today;
        int month = today.get(Calendar.MONTH);
        int week = today.get(Calendar.WEEK_OF_MONTH);
        return today;
    }*/
}
