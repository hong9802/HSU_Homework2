package com.hansung.android.myapplication2;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.hansung.android.myapplication2.R;
import com.hansung.android.myapplication2.PagerFragmentAdapter;

import java.util.Calendar;

public class monthFragment extends Fragment {
    private FragmentActivity fragmentActivity;
    int Year, Month;


    public monthFragment(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View main_view = (LinearLayout) inflater.inflate(R.layout.fragment_month, container, false);

        ViewPager2 vpPager = main_view.findViewById(R.id.month_calendar_view_pager);
        MonthViewFragmentStateAdapter adapter = new MonthViewFragmentStateAdapter(fragmentActivity);
        vpPager.setAdapter(adapter);

        vpPager.setCurrentItem(50);
        vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                Calendar today = Calendar.getInstance();
                int year = today.get(Calendar.YEAR);
                int month = today.get(Calendar.MONTH) + 1;

                int Month = month + position - 50;
                int Year = year;

                if(Month > 12){
                    Year += 1;
                    Month -= 12;
                }
                else if (Month < 1){
                    Year -= 1;
                    Month += 12;
                }


                String now = Year + "년" + Month + "월";

                fragmentActivity.setTitle(now);

            }
        });

        return main_view;
    }
}