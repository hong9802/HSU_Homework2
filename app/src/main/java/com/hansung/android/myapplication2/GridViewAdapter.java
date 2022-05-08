package com.hansung.android.myapplication2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private int mResource;
    private ArrayList<String> mItems = new ArrayList<String>();
    private int height;
    private int year, month;

    public GridViewAdapter(Context context, int resource, ArrayList<String> items, int Height, int Year, int Month) {
        mContext = context;
        mItems = items;
        mResource = resource;
        height = Height;
        year = Year; month = Month + 1;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    // 항목 id를 항목의 위치로 간주함
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) { // 해당 항목 뷰가 이전에 생성된 적이 없는 경우
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 항목 뷰를 정의한 xml 리소스(여기서는 mResource 값)으로부터 항목 뷰 객체를 메모리로 로드
            view = inflater.inflate(mResource, viewGroup,false);
        }

        TextView date = (TextView) view.findViewById(R.id.date);
        date.setText(mItems.get(i));

        date.setHeight(height);

        //ListView date = (ListView) view.findViewById(R.id.date);
        //date.setAdapter( new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mItems.get(i));


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView 클릭될 시 할 코드작성
                date.setBackgroundColor(Color.parseColor("#00FFFF"));
                String Date = (String)date.getText();
                if(Date != "") {
                    Toast.makeText(mContext, Integer.toString(year) + "." + Integer.toString(month) + "." + Date,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

}
