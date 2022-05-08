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


public class MainActivity extends AppCompatActivity {


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
                        .replace(R.id.fragment, new WeekFragment(this)).commit();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

/*
오버플로우 메뉴 처리 : https://kwanulee.github.io/AndroidProgramming/navigation/actionbar.html
AppBar Title 변경 : https://milkissboy.tistory.com/24
화면 크기 받아오는 법 : https://readystory.tistory.com/111#:~:text=%EB%B0%94%EB%A1%9C%20getRealSize()%EC%99%80%20getSize,%EB%A7%8C%20%EA%B0%80%EC%A0%B8%EC%98%A4%EB%8A%94%20%ED%95%A8%EC%88%98%EC%9E%85%EB%8B%88%EB%8B%A4.
선택한 gridView 색 바구기:https://arabiannight.tistory.com/376
*/

}
