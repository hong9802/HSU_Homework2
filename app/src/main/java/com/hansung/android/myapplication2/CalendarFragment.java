package com.hansung.android.myapplication2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
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

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class CalendarFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener {
    private Context context;
    private final float MIN_TEXT_HEIGHT = 50f;
    private final Calendar offset;
    private LinearLayout main_view;
    private TableLayout fixed_rows;
    private TextView fixed_margin_view;

    private View clickedDayView = null;
    private View clickedHourView = null;

    private ArrayList<View> day_views;

    private ArrayList<TextView> titles = new ArrayList<TextView>();
    private ArrayList<TableRow> dayRows = new ArrayList<>();
    private ArrayList<TextView> contents = new ArrayList<TextView>();

    private ICalendarUsage calendarUsage;

    private HashMap<TextView, Schedule> data = new HashMap<>();

    public CalendarFragment(Context context, Calendar calendar, ICalendarUsage usage) {
        this.context = context;
        this.offset = (Calendar) calendar.clone();
        calendarUsage = usage;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
    }

    @Override
    public void onResume() {
        super.onResume();
        CalendarUtility calendarUtility = new CalendarUtility();
        if (data != null) {
            Iterator<TextView> iter = data.keySet().iterator();
            while (iter.hasNext()) {
                TextView tv = iter.next();
                tv.setText("");
            }
            data.clear();
        }
        for (int i = 0; i < 7; i++) {
            Calendar calendar = (Calendar) this.offset.clone();
            calendar.set(Calendar.DAY_OF_WEEK, i + 1);
            ArrayList<Schedule> schedules = ScheduleManager.getInstance().getScheduleOfDay(calendar);
            for(int j = 0; j < schedules.size(); j++) {
                Schedule schedule = schedules.get(j);
                Calendar t = calendarUtility.fromTimeString(schedule.getStartTime());
                int hour = t.get(Calendar.HOUR_OF_DAY);
                TableRow row = dayRows.get(hour);
                TextView tv = (TextView) row.getVirtualChildAt(i + 1);
                tv.setText(schedule.getTitle());
                data.put(tv, schedule);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        day_views = new ArrayList<>();
        this.main_view = (LinearLayout) inflater.inflate(R.layout.fragment_week_calendar, container, false);
        this.fixed_rows = this.main_view.findViewById(R.id.fixed_rows);
        TableLayout scrollable_rows = this.main_view.findViewById(R.id.scrollable_rows);

        for (int i = 0; i < 24; i++) {
            TableRow row = new TableRow(this.context);
            fixed_margin_view = new TextView(this.context);
            fixed_margin_view.setText(String.valueOf(i));
            fixed_margin_view.setHeight((int) dpToPx(context, MIN_TEXT_HEIGHT));
            fixed_margin_view.setTextColor(Color.BLACK);
            fixed_margin_view.setBackgroundColor(Color.WHITE);
            row.addView(fixed_margin_view);
            row.setGravity(Gravity.CENTER);
            row.setBackgroundColor(Color.WHITE);

            for (int j = 0; j < 7; j++) {
                Calendar calendar = (Calendar) this.offset.clone();
                calendar.set(Calendar.DAY_OF_WEEK, j + 1);

                row.addView(makeTableRowWithText(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE), j, i));
            }
            scrollable_rows.addView(row);
            dayRows.add(row);
        }

        //header (fixed vertically)
        TableRow row = new TableRow(this.context);
        row.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        row.setGravity(Gravity.CENTER);
        row.setBackgroundColor(Color.WHITE);
        for (int i = 0; i < 7; i++) {
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
        for (int i = 0; i < 7; i++) {
            Calendar calendar = (Calendar) this.offset.clone();
            calendar.set(Calendar.DAY_OF_WEEK, i + 1);
            TextView view = makeTableTitle(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
            row.addView(view);
            day_views.add(view);
        }
        fixed_rows.addView(row);
        main_view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        return this.main_view;
    }

    public TextView makeTableTitle(int year, int month, int day) {
        TextView recyclableTextView = new TextView(this.context);
        recyclableTextView.setText(day + "");
        recyclableTextView.setTextColor(Color.BLACK);
        recyclableTextView.setGravity(Gravity.CENTER);
        recyclableTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        float height = dpToPx(this.context, MIN_TEXT_HEIGHT);

        recyclableTextView.setMinHeight((int) height);
        recyclableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //아무것도 안눌린 상태

                if (clickedDayView == null) {
                    view.setBackgroundColor(Color.parseColor("#FF00DBC4"));
                    clickedDayView = view;
                    calendarUsage.daySelected(year, month, day);
                } else {
                    if (clickedDayView != view) {
                        view.setBackgroundColor(Color.parseColor("#FF00DBC4"));
                        clickedDayView.setBackgroundColor(Color.WHITE);
                        clickedDayView = view;
                        calendarUsage.daySelected(year, month, day);
                    } else {
                        clickedDayView = null;
                        view.setBackgroundColor(Color.WHITE);
                        calendarUsage.dayDeselected();
                    }

                }
            }
        });

        titles.add(recyclableTextView);

        return recyclableTextView;
    }

    public TextView makeTableRowWithText(final int year, final int month, final int day,
                                         final int x, final int y) {
        TextView recyclableTextView = new TextView(this.context);
        recyclableTextView.setText(" ");
        recyclableTextView.setTextColor(Color.BLACK);
        recyclableTextView.setSingleLine(true);
        recyclableTextView.setTextSize(12);
        recyclableTextView.setEllipsize(TextUtils.TruncateAt.END);

        recyclableTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        float height = dpToPx(this.context, MIN_TEXT_HEIGHT);

        recyclableTextView.setMinHeight((int) height);
        recyclableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv = (TextView) view;
                Schedule schedule = data.get(tv);
                if (schedule != null) {
                    calendarUsage.showDetail(schedule);
                    return;
                }
                if (clickedHourView == null) {
                    view.setBackgroundResource(R.drawable.clicked_edge_view);
                    clickedHourView = view;
                    if (clickedDayView == null || clickedDayView != day_views.get(x)) {
                        day_views.get(x).performClick();
                    }
                    calendarUsage.hourSelected(y);
                } else {
                    if (clickedHourView != view) {
                        clickedHourView.setBackgroundResource(R.drawable.edge_view);
                        view.setBackgroundResource(R.drawable.clicked_edge_view);
                        clickedHourView = view;

                        if (clickedDayView == null || clickedDayView != day_views.get(x)) {
                            day_views.get(x).performClick();
                        }
                        calendarUsage.hourSelected(y);
                    } else {
                        clickedHourView = null;
                        view.setBackgroundResource(R.drawable.edge_view);
                        calendarUsage.hourDeselected();
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
        } catch (Exception e) {
            Log.e("Fragment", e.getMessage());
        }
    }

    public void clearSelect() {
        if (clickedDayView != null) {
            clickedDayView.setBackgroundColor(Color.WHITE);
            clickedDayView = null;
            calendarUsage.dayDeselected();
        }

        if (clickedHourView != null) {
            clickedHourView.setBackgroundResource(R.drawable.edge_view);
            clickedHourView = null;
            calendarUsage.hourDeselected();
        }

    }

}
