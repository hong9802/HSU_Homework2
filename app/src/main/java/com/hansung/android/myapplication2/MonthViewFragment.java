package com.hansung.android.myapplication2;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonthViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private int i;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MonthViewFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static MonthViewFragment newInstance(int i) {
        MonthViewFragment fragment = new MonthViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            i = getArguments().getInt(ARG_PARAM1);        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Calendar today = Calendar.getInstance();
        int lastDate, start_week;
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);

        if(month > 12) {year += 1; month = 1;}
        else if(month < 1) {year -= 1; month = 1;}

        Calendar cl = Calendar.getInstance();
        cl.set(Calendar.YEAR, year); //캘린더 설정 코드
        cl.set(Calendar.MONTH, month + i); //현재 月설정
        cl.set(Calendar.DAY_OF_MONTH, 1); //1일로 설정
        lastDate = cl.getActualMaximum(Calendar.DAY_OF_MONTH); //몇일까지 있는 지 확인하기 위한 변수
        start_week = cl.get(Calendar.DAY_OF_WEEK) -1; //요일 Check변수
        ArrayList<String> item_date = new ArrayList<String>();
        //String[] item_date = new String[42];

        int date = 1;
        for(int i = 0; i < 42; i++) {
            if(start_week > i)
                item_date.add("");
            else if(i < lastDate + start_week) {
                item_date.add(Integer.toString(date));
                date++;
            }
            else item_date.add("");
        }


        //화면 크기 받아오기
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        int grh = height*5/36; // gridview 행의 높이
        int gcw = width/7; // gridview 열의 폭


        View rootView = inflater.inflate(R.layout.fragment_month_view, container, false);
        GridView gridView = (GridView)rootView.findViewById(R.id.gridview);
        gridView.setAdapter(new GridViewAdapter(getActivity(),R.layout.item, item_date, grh, cl.get(Calendar.YEAR), cl.get(Calendar.MONTH)));
        //gridView.setAdapter(new ArrayAdapter(getActivity(),com.google.android.material.R.layout.support_simple_spinner_dropdown_item , item_date));


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!adapterView.getAdapter().getItem(i).equals("")){
                    String d = (String)adapterView.getAdapter().getItem(i);
                    Activity activity = getActivity();
                    if(activity instanceof OnGridViewSelectedListener)
                        ((OnGridViewSelectedListener)activity).onGridViewSelected(d);
                }
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }
    public interface OnGridViewSelectedListener{
        public void onGridViewSelected(String d);
    }
}
/*
화면 크기 받아오는 법 : https://readystory.tistory.com/111#:~:text=%EB%B0%94%EB%A1%9C%20getRealSize()%EC%99%80%20getSize,%EB%A7%8C%20%EA%B0%80%EC%A0%B8%EC%98%A4%EB%8A%94%20%ED%95%A8%EC%88%98%EC%9E%85%EB%8B%88%EB%8B%A4.
*/