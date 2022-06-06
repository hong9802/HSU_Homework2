package com.hansung.android.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

public class ScheduleAddActivity extends AppCompatActivity {
    public static final int REQUEST_ADD_SCHEDULE = 0;
    public static final int REQUEST_REMOVE_SCHEDULE = 1;
    public static final int REQUEST_MODIFY_SCHEDULE = 2;
    public static final int RESPONSE_CANCELED = 1000;

    public static final String REQUEST_DATA_YEAR = "REQUEST_DATA_YEAR";
    public static final String REQUEST_DATA_MONTH = "REQUEST_DATA_MONTH";
    public static final String REQUEST_DATA_DAY = "REQUEST_DATA_DAY";
    public static final String REQUEST_DATA_HOUR = "REQUEST_DATA_HOUR";

    private MapView mvMap;
    private GoogleMap googleMap;

    private GeoUtility geoUtility;
    private EditText etTitle;
    private EditText etLocation;
    private EditText etMemo;
    private TimePicker tpStartTime;
    private TimePicker tpEndTime;
    private Button btnLocationSearch;
    private Button btnScheduleSave;
    private Button btnScheduleCancel;
    private Button btnScheduleDelete;

    private Calendar currentDate = Calendar.getInstance();
    private int currentHour;

    private Schedule displaySchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_add);

        Intent intent = getIntent();

        mvMap = findViewById(R.id.mv_location);
        etTitle = findViewById(R.id.et_title);
        tpStartTime = findViewById(R.id.tp_start_time);
        tpEndTime = findViewById(R.id.tp_end_time);
        etLocation = findViewById(R.id.et_location);
        etMemo = findViewById(R.id.et_memo);
        btnLocationSearch = findViewById(R.id.btn_location_search);
        btnScheduleSave = findViewById(R.id.btn_schedule_save);
        btnScheduleCancel = findViewById(R.id.btn_schedule_cancel);
        btnScheduleDelete = findViewById(R.id.btn_schedule_delete);
        geoUtility = new GeoUtility(this);

        // Delete Button
        int requestCode = intent.getExtras().getInt("REQUEST_CODE");

        currentHour = intent.getIntExtra(REQUEST_DATA_HOUR, -1);
        CalendarUtility calendarUtility = new CalendarUtility();



        switch (requestCode) {
            case REQUEST_ADD_SCHEDULE: {
                int currentYear = intent.getIntExtra(REQUEST_DATA_YEAR, -1);
                int currentMonth = intent.getIntExtra(REQUEST_DATA_MONTH, -1);
                int currentDay = intent.getIntExtra(REQUEST_DATA_DAY, -1);

                currentDate.set(Calendar.YEAR, currentYear);
                currentDate.set(Calendar.MONTH, currentMonth - 1);
                currentDate.set(Calendar.DAY_OF_MONTH, currentDay);

                if (currentHour != -1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        tpStartTime.setHour(currentHour);
                    } else {
                        tpStartTime.setCurrentHour(currentHour);
                    }
                }
                tpStartTime.setCurrentMinute(0);
                String defaultTitle = currentYear + "년 " + currentMonth + "월 " + currentDay + "일 " + currentHour + "시 ";
                etTitle.setText(defaultTitle);


                btnScheduleSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String title = etTitle.getText().toString();
                        String location = etLocation.getText().toString();
                        String memo = etMemo.getText().toString();

                        if (title.length() <= 0) {
                            Toast.makeText(ScheduleAddActivity.this, "제목을 입력하세요.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (location.length() <= 0) {
                            Toast.makeText(ScheduleAddActivity.this, "장소를 입력하세요.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Calendar startTime = Calendar.getInstance();

                        startTime.set(Calendar.YEAR, currentYear);
                        startTime.set(Calendar.MONTH, currentMonth + 1);
                        startTime.set(Calendar.DAY_OF_MONTH, currentDay);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            startTime.set(Calendar.HOUR_OF_DAY, tpStartTime.getHour());
                            startTime.set(Calendar.MINUTE, tpStartTime.getMinute());
                        } else {
                            startTime.set(Calendar.HOUR_OF_DAY, tpStartTime.getCurrentHour());
                            startTime.set(Calendar.MINUTE, tpStartTime.getCurrentMinute());
                        }

                        Calendar endTime = (Calendar) startTime.clone();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            endTime.set(Calendar.HOUR_OF_DAY, tpEndTime.getHour());
                            endTime.set(Calendar.MINUTE, tpEndTime.getMinute());
                        } else {
                            endTime.set(Calendar.HOUR_OF_DAY, tpEndTime.getCurrentHour());
                            endTime.set(Calendar.MINUTE, tpEndTime.getCurrentMinute());
                        }

                        if (endTime.before(startTime)) {
                            Toast.makeText(ScheduleAddActivity.this, "종료 시각이 시작 시각보다 빠릅니다.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Schedule schedule = new Schedule(-1, title, currentDate, startTime, endTime, location, memo);
                        ScheduleManager.getInstance().addSchedule(schedule);
                        finish();
                    }
                });
            }
            break;

            case REQUEST_MODIFY_SCHEDULE: {
                long scheduleId = intent.getLongExtra("SCHEDULE_ID", -1);
                Log.d("AddActivity", "id : " + scheduleId);
                displaySchedule = ScheduleManager.getInstance().getSchedule(scheduleId);

                currentDate = calendarUtility.fromDateString(displaySchedule.getDate());

                if (displaySchedule != null) {
                    etTitle.setText(displaySchedule.getTitle());
                    etLocation.setText(displaySchedule.getLocation());
                    etMemo.setText(displaySchedule.getMemo());

                    Calendar startCalendar = calendarUtility.fromTimeString(displaySchedule.getStartTime());
                    Calendar endCalendar = calendarUtility.fromTimeString(displaySchedule.getEndTime());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        tpStartTime.setHour(startCalendar.get(Calendar.HOUR_OF_DAY));
                        tpStartTime.setMinute(startCalendar.get(Calendar.MINUTE));

                        tpEndTime.setHour(endCalendar.get(Calendar.HOUR_OF_DAY));
                        tpEndTime.setMinute(endCalendar.get(Calendar.MINUTE));
                    } else {
                        tpStartTime.setCurrentHour(startCalendar.get(Calendar.HOUR_OF_DAY));
                        tpStartTime.setCurrentMinute(startCalendar.get(Calendar.MINUTE));

                        tpEndTime.setCurrentHour(endCalendar.get(Calendar.HOUR_OF_DAY));
                        tpEndTime.setCurrentMinute(endCalendar.get(Calendar.MINUTE));
                    }
                }


                btnScheduleDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ScheduleManager.getInstance().removeSchedule(displaySchedule.getId());
                        finish();
                    }
                });

                btnScheduleSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = etTitle.getText().toString();
                        String location = etLocation.getText().toString();
                        String memo = etMemo.getText().toString();
                        if (title.length() <= 0) {
                            Toast.makeText(ScheduleAddActivity.this, "제목을 입력하세요.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (location.length() <= 0) {
                            Toast.makeText(ScheduleAddActivity.this, "장소를 입력하세요.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Calendar startTime = Calendar.getInstance();

                        startTime.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));
                        startTime.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) + 1);
                        startTime.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            startTime.set(Calendar.HOUR_OF_DAY, tpStartTime.getHour());
                            startTime.set(Calendar.MINUTE, tpStartTime.getMinute());
                        } else {
                            startTime.set(Calendar.HOUR_OF_DAY, tpStartTime.getCurrentHour());
                            startTime.set(Calendar.MINUTE, tpStartTime.getCurrentMinute());
                        }

                        Calendar endTime = (Calendar) startTime.clone();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            endTime.set(Calendar.HOUR_OF_DAY, tpEndTime.getHour());
                            endTime.set(Calendar.MINUTE, tpEndTime.getMinute());
                        } else {
                            endTime.set(Calendar.HOUR_OF_DAY, tpEndTime.getCurrentHour());
                            endTime.set(Calendar.MINUTE, tpEndTime.getCurrentMinute());
                        }

                        if (endTime.before(startTime)) {
                            Toast.makeText(ScheduleAddActivity.this, "종료 시각이 시작 시각보다 빠릅니다.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Schedule schedule = new Schedule(scheduleId, title, currentDate, startTime, endTime, location, memo);
                        ScheduleManager.getInstance().updateSchedule(schedule);
                        finish();
                    }
                });
            }
                break;
            }

            mvMap.onCreate(savedInstanceState);
            mvMap.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    ScheduleAddActivity.this.googleMap = googleMap;
                    if (displaySchedule != null) {
                        LatLng l = geoUtility.addressToLatLng(displaySchedule.getLocation());
                        if (l != null) {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(l));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l, 15));

                        }
                   }

                }
            });

            // Add marker from address
            // https://stackoverflow.com/questions/24352192/android-google-maps-add-marker-by-address
            btnLocationSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String address = etLocation.getText().toString();
                    if (address.length() <= 0) {
                        Toast.makeText(ScheduleAddActivity.this, "주소를 입력하세요.",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LatLng l = geoUtility.addressToLatLng(address);
                    if (l == null) {
                        Toast.makeText(ScheduleAddActivity.this, "주소를 찾을 수 없습니다.",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    googleMap.addMarker(new MarkerOptions()
                            .position(l));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l, 15));
                }
            });

            btnScheduleCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setResult(RESPONSE_CANCELED);
                    finish();
                }
            });
        }


        /* Google Map works with activity life cycles */
        @Override
        protected void onResume () {
            super.onResume();
            mvMap.onResume();
        }

        @Override
        protected void onPause () {
            mvMap.onPause();
            super.onPause();
        }

        @Override
        protected void onDestroy () {
            mvMap.onDestroy();
            super.onDestroy();
        }

        @Override
        public void onLowMemory () {
            super.onLowMemory();
            mvMap.onLowMemory();
        }

        @Override
        public void onSaveInstanceState (Bundle outState){
            super.onSaveInstanceState(outState);
            mvMap.onSaveInstanceState(outState);
        }
    }