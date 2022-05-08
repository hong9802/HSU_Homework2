package com.hansung.android.myapplication2;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.hansung.android.myapplication2.R;
import com.hansung.android.myapplication2.PagerFragmentAdapter;

import java.util.Calendar;

public class WeekFragment extends Fragment {
    private FragmentActivity fragmentActivity;

    public WeekFragment(FragmentActivity fragmentActivity){
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View main_view = (LinearLayout)inflater.inflate(R.layout.fragment_week,container,false);
        ViewPager2 viewPager = main_view.findViewById(R.id.week_calendar_view_pager);
        PagerFragmentAdapter fragmentAdapter = new PagerFragmentAdapter(fragmentActivity);

        viewPager.setAdapter(fragmentAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Calendar calendar = fragmentAdapter.getCurrentWeek(position);
                calendar.set(Calendar.DAY_OF_WEEK, 0);
                String now = calendar.get(Calendar.YEAR) + "년" + (calendar.get(Calendar.MONTH) + 1) + "월";

                fragmentActivity.setTitle(now);
            }
        });

        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(fragmentAdapter.MIDDLE_POSITION);
        return main_view;
    }
}