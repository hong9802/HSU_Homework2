package com.hansung.android.myapplication2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements MonthViewFragment.OnGridViewSelectedListener {
    ActionBar bar;
    int year, month;
    int Year, Month;
    String now;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar today = Calendar.getInstance();
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH) + 1;
        now = year + "년" + month + "월";
        bar = getSupportActionBar();
        if(bar != null)
            bar.setTitle(now);

        /*
        화면 크기 받아오기
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        */

        //ViewPager2 객체에 FragmentStateAdapter 객체 설정
        ViewPager2 vpPager = findViewById(R.id.vpPager);
        FragmentStateAdapter adapter = new MonthViewFragmentStateAdapter(this);
        vpPager.setAdapter(adapter);

        vpPager.setCurrentItem(50);
        vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {

                Month = month + position - 50;
                Year = year;
                if(Month >= 12){
                    year += 1;
                    month -= 12;
                }
                else if (Month <= 1){
                    year -= 1;
                    month += 12;
                }

                now = Year + "년" + Month + "월";
                bar.setTitle(now);


            }
        });
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
                //getSupportFragmentManager().beginTransaction().replace(R.id.vpPager, new MonthViewFragment()).commit();
                return true;
            case R.id.week_set:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onGridViewSelected(String d) {
        Toast.makeText(getApplicationContext(), Integer.toString(Year) + "." + Integer.toString(Month) + "." + d, Toast.LENGTH_SHORT).show();
    }
}
/*
오버플로우 메뉴 처리 : https://kwanulee.github.io/AndroidProgramming/navigation/actionbar.html
AppBar Title 변경 : https://milkissboy.tistory.com/24
화면 크기 받아오는 법 : https://readystory.tistory.com/111#:~:text=%EB%B0%94%EB%A1%9C%20getRealSize()%EC%99%80%20getSize,%EB%A7%8C%20%EA%B0%80%EC%A0%B8%EC%98%A4%EB%8A%94%20%ED%95%A8%EC%88%98%EC%9E%85%EB%8B%88%EB%8B%A4.
선택한 gridView 색 바구기:https://arabiannight.tistory.com/376
*/