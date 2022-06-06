package com.hansung.android.myapplication2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements ICalendarUsage {
    private FloatingActionButton fab;

    private Calendar timeSelected = Calendar.getInstance();
    private boolean daySelected = false;
    private boolean hourSelected = false;

    private ScheduleManager scheduleManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        ActionBar bar;
        int year, month;
        String now;

        Calendar today = Calendar.getInstance();
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH) + 1;
        now = year + "년" + month + "월";
        bar = getSupportActionBar();
        if (bar != null)
            bar.setTitle(now);
        */
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, new monthFragment(this)).commit();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* spawn 'ScheduleAddActivity' */
                if (daySelected && hourSelected)
                {
                    Intent intent = new Intent(MainActivity.this, ScheduleAddActivity.class);
                    intent.putExtra("REQUEST_CODE", ScheduleAddActivity.REQUEST_ADD_SCHEDULE);
                    intent.putExtra(ScheduleAddActivity.REQUEST_DATA_YEAR, timeSelected.get(Calendar.YEAR));
                    intent.putExtra(ScheduleAddActivity.REQUEST_DATA_MONTH, timeSelected.get(Calendar.MONTH) + 1);
                    intent.putExtra(ScheduleAddActivity.REQUEST_DATA_DAY, timeSelected.get(Calendar.DAY_OF_MONTH));
                    intent.putExtra(ScheduleAddActivity.REQUEST_DATA_HOUR, timeSelected.get(Calendar.HOUR_OF_DAY));
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Appbar에 Overflow Menu 추가
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Item 선택 처리

        switch (item.getItemId()) {

            case R.id.month_set:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, new monthFragment(this)).commit();
                return true;
            case R.id.week_set:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, new WeekFragment(this, this)).commit();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void daySelected(int year, int month, int day) {
        timeSelected.set(Calendar.YEAR, year);
        timeSelected.set(Calendar.MONTH, month - 1);
        timeSelected.set(Calendar.DAY_OF_MONTH, day);
        ScheduleManager.getInstance().getScheduleOfDay(timeSelected);

        daySelected = true;
    }

    @Override
    public void dayDeselected() {
        Log.d("MainActivity", "dayDeselected");
        daySelected = false;
    }

    @Override
    public void hourSelected(int hour) {
        timeSelected.set(Calendar.HOUR_OF_DAY, hour);
        hourSelected = true;
        Log.d("MainActivity", "hourSelected : " + hour);
    }

    @Override
    public void hourDeselected() {
        Log.d("MainActivity", "hourDeselected");
        hourSelected = false;
    }

    @Override
    public void showDetail(Schedule schedule) {
        Log.d("MainActivity", "showDetail : " + schedule.getId());
        CalendarUtility calendarUtility = new CalendarUtility();

        Intent intent = new Intent(MainActivity.this, ScheduleAddActivity.class);
        intent.putExtra("REQUEST_CODE", ScheduleAddActivity.REQUEST_MODIFY_SCHEDULE);
        intent.putExtra("SCHEDULE_ID", schedule.getId());
        startActivity(intent);
    }

/*
오버플로우 메뉴 처리 : https://kwanulee.github.io/AndroidProgramming/navigation/actionbar.html
AppBar Title 변경 : https://milkissboy.tistory.com/24
화면 크기 받아오는 법 : https://readystory.tistory.com/111#:~:text=%EB%B0%94%EB%A1%9C%20getRealSize()%EC%99%80%20getSize,%EB%A7%8C%20%EA%B0%80%EC%A0%B8%EC%98%A4%EB%8A%94%20%ED%95%A8%EC%88%98%EC%9E%85%EB%8B%88%EB%8B%A4.
선택한 gridView 색 바구기:https://arabiannight.tistory.com/376
*/

}
