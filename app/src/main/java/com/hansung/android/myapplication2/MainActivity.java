package com.hansung.android.myapplication2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ActionBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH) + 1;
        String now = year + "년" + month + "월";
        bar = getSupportActionBar();
        if(bar != null)
            bar.setTitle(now);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
/*
오버플로우 메뉴 처리 : https://kwanulee.github.io/AndroidProgramming/navigation/actionbar.html
AppBar Title 변경 : https://milkissboy.tistory.com/24
 */