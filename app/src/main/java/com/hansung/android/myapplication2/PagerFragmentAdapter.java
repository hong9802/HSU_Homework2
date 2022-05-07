package com.hansung.android.myapplication2;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Calendar;

public class PagerFragmentAdapter extends FragmentStateAdapter {
    private final FragmentActivity activity;
    public final int MIDDLE_POSITION = Integer.MAX_VALUE / 2;
    public PagerFragmentAdapter(FragmentActivity fragmentActivity){
        super(fragmentActivity);
        activity = fragmentActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Calendar current = getCurrentWeek(position);
        return new CalendarFragment(activity.getApplicationContext(), current);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public Calendar getCurrentWeek(int position){
        Calendar today = Calendar.getInstance();
        position -= MIDDLE_POSITION; //이전으로 넘기기 위한 코드
        today.set(Calendar.HOUR_OF_DAY, 0);
        long time = today.getTime().getTime();
        time += ((long)position * (3600 * 24) * 7 * 1000); //과거 시간을 계산하기 위한 코드
        today.setTimeInMillis(time);
        int month = today.get(Calendar.MONTH);
        int week = today.get(Calendar.WEEK_OF_MONTH);
        return today;
    }
}
