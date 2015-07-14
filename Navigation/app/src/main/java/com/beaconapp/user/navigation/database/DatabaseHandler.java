package com.beaconapp.user.navigation.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.beaconapp.user.navigation.classes.DailyStat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DailyStatistics";

    // Contacts table name
    private static final String TABLE_STATISTICS = "statistics";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DESK = "desk";
    private static final String KEY_OFFICE = "office";
    private static final String KEY_OUTDOOR = "outdoor";
    private static final String KEY_STAMP = "tstamp";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STATISTICS_TABLE = "CREATE TABLE " + TABLE_STATISTICS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_STAMP + " LONG," + KEY_DESK + " LONG," + KEY_OFFICE + " LONG,"
                + KEY_OUTDOOR + " LONG" + ")";
        db.execSQL(CREATE_STATISTICS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTICS);

        // Create tables again
        onCreate(db);
    }


    // Adding new Statistics
    public void addDailyStat(DailyStat dailyStat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STAMP, dailyStat.getTstamp());
        values.put(KEY_DESK, dailyStat.getDesk_time());
        values.put(KEY_OFFICE, dailyStat.getOffice_time());
        values.put(KEY_OUTDOOR, dailyStat.getOutdoor_time());

        // Inserting Row
        db.insert(TABLE_STATISTICS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single statistics
    public DailyStat getDailyStat(long tstamp) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STATISTICS, new String[] { KEY_ID, KEY_STAMP, KEY_DESK, KEY_OFFICE, KEY_OUTDOOR }, KEY_STAMP + "=?",
                new String[] { String.valueOf(tstamp) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        DailyStat dailyStat = new DailyStat(Integer.parseInt(cursor.getString(0)),Long.parseLong(cursor.getString(1)),
                Long.parseLong(cursor.getString(2)),Long.parseLong(cursor.getString(3)),Long.parseLong(cursor.getString(4)));
        // return statistics
        return dailyStat;
    }

    // Getting single statistics
    public DailyStat getSelectedDayStat(long tstamp) {
        SQLiteDatabase db = this.getReadableDatabase();

        long start_of_day = tstamp;
        long end_of_day = tstamp+86400000;

        Cursor cursor = db.query(TABLE_STATISTICS, new String[]{KEY_ID, KEY_STAMP, KEY_DESK, KEY_OFFICE, KEY_OUTDOOR}, KEY_STAMP + " >? AND " + KEY_STAMP + " <?",
                new String[]{String.valueOf(start_of_day), String.valueOf(end_of_day)}, null, null, null, null);

        if (cursor.moveToFirst()) {

            DailyStat dailyStat = new DailyStat(Integer.parseInt(cursor.getString(0)), Long.parseLong(cursor.getString(1)),
                    Long.parseLong(cursor.getString(2)), Long.parseLong(cursor.getString(3)), Long.parseLong(cursor.getString(4)));
            // return statistics
            return dailyStat;
        }
        else {
            return null;
        }
    }

    // Getting All statistics
    public List<DailyStat> getAllDailyStat() {
        List<DailyStat> dailyStatList = new ArrayList<DailyStat>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STATISTICS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToLast()) {
            do {
                DailyStat dailyStat = new DailyStat();
                dailyStat.setID(Integer.parseInt(cursor.getString(0)));
                dailyStat.setTstamp(Long.parseLong(cursor.getString(1)));
                dailyStat.setDesk_time(Long.parseLong(cursor.getString(2)));
                dailyStat.setOffice_time(Long.parseLong(cursor.getString(3)));
                dailyStat.setOutdoor_time(Long.parseLong(cursor.getString(4)));
                dailyStatList.add(dailyStat);
            } while (cursor.moveToPrevious());
        }

        return dailyStatList;
    }

    public List<DailyStat> getWeeklyStat(long tstamp) {
        List<DailyStat> dailyStatList = new ArrayList<DailyStat>();

        long start_of_week = (((tstamp/604800000L)*604800000L));
        long end_of_week = start_of_week+604800000L;

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STATISTICS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_STATISTICS, new String[]{KEY_ID, KEY_STAMP, KEY_DESK, KEY_OFFICE, KEY_OUTDOOR}, KEY_STAMP + " >? AND " + KEY_STAMP + " <?",
                new String[]{String.valueOf(start_of_week), String.valueOf(end_of_week)}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToLast()) {
            do {
                DailyStat dailyStat = new DailyStat();
                dailyStat.setID(Integer.parseInt(cursor.getString(0)));
                dailyStat.setTstamp(Long.parseLong(cursor.getString(1)));
                dailyStat.setDesk_time(Long.parseLong(cursor.getString(2)));
                dailyStat.setOffice_time(Long.parseLong(cursor.getString(3)));
                dailyStat.setOutdoor_time(Long.parseLong(cursor.getString(4)));
                dailyStatList.add(dailyStat);
            } while (cursor.moveToPrevious());
        }

        return dailyStatList;
    }

    public List<DailyStat> getMonthlyStat(long tstamp) {
        List<DailyStat> dailyStatList = new ArrayList<DailyStat>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(tstamp);
        calendar.set(Calendar.DAY_OF_MONTH, 1);  // 1st day of month.

        long start_of_month = calendar.getTimeInMillis();

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE,-1);

        long end_of_month = calendar.getTimeInMillis();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STATISTICS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_STATISTICS, new String[]{KEY_ID, KEY_STAMP, KEY_DESK, KEY_OFFICE, KEY_OUTDOOR}, KEY_STAMP + " >? AND " + KEY_STAMP + " <?",
                new String[]{String.valueOf(start_of_month), String.valueOf(end_of_month)}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToLast()) {
            do {
                DailyStat dailyStat = new DailyStat();
                dailyStat.setID(Integer.parseInt(cursor.getString(0)));
                dailyStat.setTstamp(Long.parseLong(cursor.getString(1)));
                dailyStat.setDesk_time(Long.parseLong(cursor.getString(2)));
                dailyStat.setOffice_time(Long.parseLong(cursor.getString(3)));
                dailyStat.setOutdoor_time(Long.parseLong(cursor.getString(4)));
                dailyStatList.add(dailyStat);
            } while (cursor.moveToPrevious());
        }

        return dailyStatList;
    }

}