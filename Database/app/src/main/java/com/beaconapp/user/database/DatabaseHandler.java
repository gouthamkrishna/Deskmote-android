package com.beaconapp.user.database;

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
    private static final String DATABASE_NAME = "remindersManager";

    // Contacts table name
    private static final String TABLE_REMINDERS = "reminders";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_TAG = "tag";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REMINDERS_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT," + KEY_TIME + " TEXT,"
                + KEY_TAG + " TEXT" + ")";
        db.execSQL(CREATE_REMINDERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);

        // Create tables again
        onCreate(db);
    }


    // Adding new Reminder
    void addReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, reminder.getDate()); // Date
        values.put(KEY_TIME, reminder.getTime()); // Time
        values.put(KEY_TAG, reminder.getTag()); // Tag

        // Inserting Row
        db.insert(TABLE_REMINDERS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single reminder
    Reminder getReminder(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REMINDERS, new String[] { KEY_ID,
                        KEY_DATE, KEY_TIME, KEY_TAG }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Reminder reminder = new Reminder(Integer.parseInt(cursor.getString(0)),
                cursor.getString(3));
        // return reminder
        return reminder;
    }

    // Getting All Reminders
    public List<Reminder> getAllReminders() {
        List<Reminder> reminderList = new ArrayList<Reminder>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REMINDERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setID(Integer.parseInt(cursor.getString(0)));
                reminder.setDate(cursor.getString(1));
                reminder.setTime(cursor.getString(2));
            	reminder.setTag(cursor.getString(3));
                // Adding contact to list
                reminderList.add(reminder);
            } while (cursor.moveToNext());
        }

        // return contact list
        return reminderList;
    }

    // Getting reminders Count
    public int getReminderCount() {
        String countQuery = "SELECT  * FROM " + TABLE_REMINDERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single reminder
    public int updateReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, reminder.getDate());
        values.put(KEY_TIME, reminder.getTime());
        values.put(KEY_TAG, reminder.getTag());

        // updating row
        return db.update(TABLE_REMINDERS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(reminder.getID())});
    }

    // Deleting single reminder
    public void deleteReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMINDERS, KEY_ID + " = ?",
                new String[] { String.valueOf(reminder.getID()) });
        db.close();
    }
}
