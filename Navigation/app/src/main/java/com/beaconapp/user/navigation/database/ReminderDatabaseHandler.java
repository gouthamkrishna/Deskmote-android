package com.beaconapp.user.navigation.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.beaconapp.user.navigation.classes.Reminder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 23/6/15.
 */
public class ReminderDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "RemindersManager";

    // Contacts table name
    private static final String TABLE_REMINDERS = "reminder";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_TAG = "tag";
    private static final String KEY_STAMP = "tstamp";

    public ReminderDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db_reminder) {
        String CREATE_REMINDERS_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_STAMP + " LONG," + KEY_DATE + " TEXT," + KEY_TIME + " TEXT,"
                + KEY_TAG + " TEXT" + ")";
        db_reminder.execSQL(CREATE_REMINDERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db_reminder, int oldVersion, int newVersion) {
        // Drop older table if existed
        db_reminder.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);

        // Create tables again
        onCreate(db_reminder);
    }


    // Adding new Reminder
    public void addReminder(Reminder reminder) {
        SQLiteDatabase db_reminder = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STAMP, reminder.getTstamp()); //Tstamp
        values.put(KEY_DATE, reminder.getDate()); // Date
        values.put(KEY_TIME, reminder.getTime()); // Time
        values.put(KEY_TAG, reminder.getTag()); // Tag

        // Inserting Row
        db_reminder.insert(TABLE_REMINDERS, null, values);
        db_reminder.close(); // Closing database connection
    }

    // Getting single reminder
    public Reminder getReminder(long timestamp, String tag) {
        SQLiteDatabase db_reminder = this.getReadableDatabase();

        Cursor cursor = db_reminder.query(TABLE_REMINDERS, new String[] { KEY_ID, KEY_STAMP, KEY_DATE, KEY_TIME, KEY_TAG }, KEY_STAMP + " =? AND " + KEY_TAG + " =?",
                new String[] { String.valueOf(timestamp) , tag}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Reminder reminder = new Reminder(Integer.parseInt(cursor.getString(0)),Long.parseLong(cursor.getString(1)),
                cursor.getString(4),cursor.getString(2),cursor.getString(3));
        // return reminder
        return reminder;
    }

    // Getting All Reminders
    public List<Reminder> getAllReminders() {
        List<Reminder> reminderList = new ArrayList<Reminder>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REMINDERS;

        SQLiteDatabase db_reminder = this.getWritableDatabase();

        Cursor cursor = db_reminder.query(TABLE_REMINDERS, new String[] { KEY_ID, KEY_STAMP, KEY_DATE, KEY_TIME, KEY_TAG }, null,
                null, null, null, KEY_STAMP+" DESC", null);

        // looping through all rows and adding to list
        if (cursor.moveToLast()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setID(Integer.parseInt(cursor.getString(0)));
                reminder.setTstamp(Long.parseLong(cursor.getString(1)));
                reminder.setDate(cursor.getString(2));
                reminder.setTime(cursor.getString(3));
                reminder.setTag(cursor.getString(4));
                // Adding contact to list
                reminderList.add(reminder);
            } while (cursor.moveToPrevious());
        }

        // return contact list
        return reminderList;
    }

    // Getting reminders Count
    public int getReminderCount() {
        String countQuery = "SELECT  * FROM " + TABLE_REMINDERS;
        SQLiteDatabase db_reminder = this.getReadableDatabase();
        Cursor cursor = db_reminder.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single reminder
    public int updateReminder(Reminder reminder) {
        SQLiteDatabase db_reminder = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STAMP, reminder.getTstamp());
        values.put(KEY_DATE, reminder.getDate());
        values.put(KEY_TIME, reminder.getTime());
        values.put(KEY_TAG, reminder.getTag());

        // updating row
        return db_reminder.update(TABLE_REMINDERS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(reminder.getID())});
    }

    // Deleting single reminder
    public void deleteReminder(Reminder reminder) {
        SQLiteDatabase db_reminder = this.getWritableDatabase();
        db_reminder.delete(TABLE_REMINDERS, KEY_ID + " = ?",
                new String[] { String.valueOf(reminder.getID()) });
        db_reminder.close();
    }
}


