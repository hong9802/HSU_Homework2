package com.hansung.android.myapplication2;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.hansung.android.myapplication2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CalendarFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener{
    private Context context;
    private final float MIN_TEXT_HEIGHT = 50f;
    private final Calendar offset;
    private LinearLayout main_view;
    private TableLayout fixed_rows;
    private TextView fixed_margin_view;
    private boolean day_isClicked;
    private boolean hour_isClicked;
    private View already_click_day;
    private View already_click_hour;
    private ArrayList<View> day_views;

    public CalendarFragment(){
        this(null, Calendar.getInstance());
    }

    public CalendarFragment(Context context){
        this(context, Calendar.getInstance());
    }

    public CalendarFragment(Context context, Calendar calendar){
        this.context = context;
        this.offset = (Calendar) calendar.clone();
        already_click_day = null;
        already_click_hour = null;
        day_isClicked = false;
        hour_isClicked = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        day_views = new ArrayList<>();
        this.main_view =(LinearLayout)inflater.inflate(R.layout.fragment_week_calendar,container,false);
        this.fixed_rows = this.main_view.findViewById(R.id.fixed_rows);
        TableLayout scrollable_rows = this.main_view.findViewById(R.id.scrollable_rows);

        TableRow row;
        for(int i = 0; i < 24; i++) {
            row = new TableRow(this.context);
            fixed_margin_view = new TextView(this.context);
            fixed_margin_view.setText(String.valueOf(i));
            fixed_margin_view.setHeight((int)dpToPx(context, MIN_TEXT_HEIGHT));
            fixed_margin_view.setTextColor(Color.BLACK);
            fixed_margin_view.setBackgroundColor(Color.WHITE);
            row.addView(fixed_margin_view);
            row.setGravity(Gravity.CENTER);
            row.setBackgroundColor(Color.WHITE);
            for(int j =0;j<7;j++){
                row.addView(makeTableRowWithText(j));
            }
            scrollable_rows.addView(row);
        }

        //header (fixed vertically)
        row = new TableRow(this.context);
        row.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        row.setGravity(Gravity.CENTER);
        row.setBackgroundColor(Color.WHITE);
        for(int i = 0; i < 7; i++) {
            Calendar calendar = (Calendar) this.offset.clone();

            calendar.set(Calendar.DAY_OF_WEEK, i + 1);
            SimpleDateFormat myDateFormat = new SimpleDateFormat("E");
            String formattedDate = myDateFormat.format(calendar.getTime());
            TextView view = new TextView(this.context);
            view.setText(formattedDate);
            view.setGravity(Gravity.CENTER);
            view.setTextColor(Color.BLACK);
            view.setBackgroundColor(Color.GREEN);
            row.addView(view);
        }
        fixed_rows.addView(row);

        row = new TableRow(this.context);
        row.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        row.setGravity(Gravity.CENTER);
        row.setBackgroundColor(Color.WHITE);
        for(int i = 0; i < 7; i++) {
            Calendar calendar = (Calendar) this.offset.clone();
            calendar.set(Calendar.DAY_OF_WEEK, i + 1);
            TextView view = makeTableTitle(String.valueOf(calendar.get(Calendar.DATE)));
            row.addView(view);
            day_views.add(view);
        }
        fixed_rows.addView(row);
        main_view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        return this.main_view;
    }


    public TextView makeTableTitle(String text) {
        TextView recyclableTextView = new TextView(this.context);
        recyclableTextView.setText(text);
        recyclableTextView.setTextColor(Color.BLACK);
        recyclableTextView.setGravity(Gravity.CENTER);

        recyclableTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        float height = dpToPx(this.context, MIN_TEXT_HEIGHT);
        recyclableTextView.setMinHeight((int)height);
        recyclableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //아무것도 안눌린 상태
                if(!day_isClicked) {
                    view.setBackgroundColor(Color.parseColor("#FF00DBC4"));
                    day_isClicked = !day_isClicked;
                    already_click_day = view;
                }else{
                    if(already_click_day != view){
                        if(already_click_day != null) {
                            already_click_day.setBackgroundColor(Color.WHITE);
                        }
                        view.setBackgroundColor(Color.parseColor("#FF00DBC4"));
                        already_click_day = view;
                    }else{
                        view.setBackgroundColor(Color.WHITE);
                        day_isClicked = !day_isClicked;
                        already_click_day = null;
                    }
                }
            }
        });

        return recyclableTextView;
    }

    public TextView makeTableRowWithText(final int position) {
        TextView recyclableTextView = new TextView(this.context);
        recyclableTextView.setText(" ");
        recyclableTextView.setTextColor(Color.BLACK);
        recyclableTextView.setGravity(Gravity.CENTER);

        recyclableTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        float height = dpToPx(this.context, MIN_TEXT_HEIGHT);
        recyclableTextView.setMinHeight((int)height);
        recyclableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hour_isClicked) {
                    view.setBackgroundResource(R.drawable.clicked_edge_view);
                    hour_isClicked = !hour_isClicked;
                    already_click_hour = view;
                    day_views.get(position).performClick();
                    Toast.makeText(context, "position=" + position, Toast.LENGTH_SHORT).show();
                }else{
                    if(already_click_hour != view){
                        if(already_click_hour != null) {
                            already_click_hour.setBackgroundResource(R.drawable.edge_view);
                            day_views.get(position).performClick();
                            Toast.makeText(context, "position=" + position, Toast.LENGTH_SHORT).show();
                        }
                        view.setBackgroundResource(R.drawable.clicked_edge_view);
                        already_click_hour = view;
                    }else{
                        view.setBackgroundResource(R.drawable.edge_view);
                        day_isClicked = !day_isClicked;
                        already_click_hour = null;
                    }
                }
            }
        });
        recyclableTextView.setBackgroundResource(R.drawable.edge_view);

        return recyclableTextView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        main_view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    public float dpToPx(Context context, float dp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
    }

    @Override
    public void onGlobalLayout() {
        try {
            fixed_rows.setPadding(fixed_margin_view.getWidth(), 0, 0, 0);
        }catch(Exception e){
            Log.e("Fragment", e.getMessage());
        }
    }
}
