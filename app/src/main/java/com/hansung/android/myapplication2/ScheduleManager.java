package com.hansung.android.myapplication2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleManager {

    private static class TableEntries implements BaseColumns {
        public static final String TABLE_NAME = "schedules";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_END_TIME = "end_time";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_MEMO = "memo";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TableEntries.TABLE_NAME + " (" +
                    TableEntries._ID + " INTEGER PRIMARY KEY," +
                    TableEntries.COLUMN_NAME_DATE + " TEXT," +
                    TableEntries.COLUMN_NAME_TITLE + " TEXT," +
                    TableEntries.COLUMN_NAME_START_TIME + " TEXT," +
                    TableEntries.COLUMN_NAME_END_TIME + " TEXT, " +
                    TableEntries.COLUMN_NAME_LOCATION + " TEXT, " +
                    TableEntries.COLUMN_NAME_MEMO + " TEXT " +
            ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TableEntries.TABLE_NAME;

    public class ScheduleDbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Schedule.db";

        public ScheduleDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    private static ScheduleManager instance;
    private ScheduleDbHelper dbHelper;
    private SQLiteDatabase wDb;
    private SQLiteDatabase rDb;

    private ScheduleManager() {

        // Get Context from App
        App app = App.instance();

        dbHelper = new ScheduleDbHelper(app.getContext());

        wDb = dbHelper.getWritableDatabase();
        rDb = dbHelper.getReadableDatabase();
    }

    public static ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    public long addSchedule(Schedule schedule) {
        ContentValues values = new ContentValues();
        values.put(TableEntries.COLUMN_NAME_DATE, schedule.getDate());
        values.put(TableEntries.COLUMN_NAME_TITLE, schedule.getTitle());
        values.put(TableEntries.COLUMN_NAME_START_TIME, schedule.getStartTime());
        values.put(TableEntries.COLUMN_NAME_END_TIME, schedule.getEndTime());
        values.put(TableEntries.COLUMN_NAME_LOCATION, schedule.getLocation());
        values.put(TableEntries.COLUMN_NAME_MEMO, schedule.getMemo());

        long newRowId = wDb.insert(
                TableEntries.TABLE_NAME, null, values);
        return newRowId;
    }

    public void updateSchedule(Schedule schedule) {
        ContentValues values = new ContentValues();
        values.put(TableEntries.COLUMN_NAME_DATE, schedule.getDate());
        values.put(TableEntries.COLUMN_NAME_TITLE, schedule.getTitle());
        values.put(TableEntries.COLUMN_NAME_START_TIME, schedule.getStartTime());
        values.put(TableEntries.COLUMN_NAME_END_TIME, schedule.getEndTime());
        values.put(TableEntries.COLUMN_NAME_LOCATION, schedule.getLocation());
        values.put(TableEntries.COLUMN_NAME_MEMO, schedule.getMemo());

        // Which row to update, based on the title
        String selection = TableEntries._ID + " = ?";
        String[] selectionArgs = { schedule.getId() + "" };

        wDb.update(
                TableEntries.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public ArrayList<Schedule> getScheduleOfDay(Calendar calendar) {
        CalendarUtility calendarUtility = new CalendarUtility();

        String selection = TableEntries.COLUMN_NAME_DATE + " = ? ";
        String[] selectionArgs = { calendarUtility.toDateString(calendar) };

        Cursor cursor = rDb.query(
                TableEntries.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Schedule> items = new ArrayList<>();
        while(cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(TableEntries._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TableEntries.COLUMN_NAME_TITLE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(TableEntries.COLUMN_NAME_DATE));
            String startTime = cursor.getString(cursor.getColumnIndexOrThrow(TableEntries.COLUMN_NAME_START_TIME));
            String endTime = cursor.getString(cursor.getColumnIndexOrThrow(TableEntries.COLUMN_NAME_END_TIME));
            String memo = cursor.getString(cursor.getColumnIndexOrThrow(TableEntries.COLUMN_NAME_MEMO));
            String location =  cursor.getString(cursor.getColumnIndexOrThrow(TableEntries.COLUMN_NAME_LOCATION));
            Schedule schedule = new Schedule(id, title, date, startTime, endTime, location, memo);
            items.add(schedule);
        }
        cursor.close();

        Log.d("ScheduleManager", "Search size : " + items.size());
        return items;
    }

    public Schedule getSchedule(long id) {
        Schedule schedule = null;
        String selection = TableEntries._ID + " = ? ";
        String[] selectionArgs = { id + "" };
        Log.d("getSchedule", "id : " + id);
        Cursor cursor = rDb.query(
                TableEntries.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        while(cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TableEntries.COLUMN_NAME_TITLE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(TableEntries.COLUMN_NAME_DATE));
            String startTime = cursor.getString(cursor.getColumnIndexOrThrow(TableEntries.COLUMN_NAME_START_TIME));
            String endTime = cursor.getString(cursor.getColumnIndexOrThrow(TableEntries.COLUMN_NAME_END_TIME));
            String memo = cursor.getString(cursor.getColumnIndexOrThrow(TableEntries.COLUMN_NAME_MEMO));
            String location =  cursor.getString(cursor.getColumnIndexOrThrow(TableEntries.COLUMN_NAME_LOCATION));
            schedule = new Schedule(id, title, date, startTime, endTime, location, memo);
            break;
        }
        cursor.close();

        return schedule;
    }

    public boolean removeSchedule(long id) {

        String selection = TableEntries._ID + "  = ?";
        String[] selectionArgs = { id + "" };

        int deletedRows = wDb.delete(TableEntries.TABLE_NAME, selection, selectionArgs);
        return (deletedRows > 0);
    }




}
