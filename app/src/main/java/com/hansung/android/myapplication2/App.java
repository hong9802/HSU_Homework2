package com.hansung.android.myapplication2;

import android.app.Application;
import android.content.Context;

// http://susemi99.kr/5149/
public class App extends Application {
    private static App instance;
    public static App instance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public Context getContext() {
        return getApplicationContext();
    }
}
