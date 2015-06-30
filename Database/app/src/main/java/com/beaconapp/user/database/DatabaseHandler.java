package com.beaconapp.user.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 23/6/15.
 */
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
    void addDailyStat(DailyStat dailyStat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STAMP, dailyStat.getTstamp()); //Tstamp
        values.put(KEY_DESK, dailyStat.getDesk_time()); // Date
        values.put(KEY_OFFICE, dailyStat.getOffice_time()); // Time
        values.put(KEY_OUTDOOR, dailyStat.getOutdoor_time()); // Tag

        // Inserting Row
        db.insert(TABLE_STATISTICS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single statistics
    DailyStat getDailyStat(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STATISTICS, new String[] { KEY_ID, KEY_STAMP, KEY_DESK, KEY_OFFICE, KEY_OUTDOOR }, KEY_STAMP + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        DailyStat dailyStat = new DailyStat(Integer.parseInt(cursor.getString(0)),Long.parseLong(cursor.getString(1)),
                Long.parseLong(cursor.getString(2)),Long.parseLong(cursor.getString(3)),Long.parseLong(cursor.getString(4)));
        // return statistics
        return dailyStat;
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

}
