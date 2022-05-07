package com.hansung.android.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            case R.id.week_set:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, new WeekFragment(this)).commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}