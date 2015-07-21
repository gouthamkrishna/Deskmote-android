package com.beaconapp.user.navigation.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.beaconapp.user.navigation.classes.Reminder;

import java.util.ArrayList;
import java.util.List;

public class ReminderDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RemindersManager";
    private static final String TABLE_REMINDERS = "reminder";
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_TAG = "tag";
    private static final String KEY_STAMP = "tstamp";

    public ReminderDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db_reminder) {
        String CREATE_REMINDERS_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_STAMP + " LONG," + KEY_DATE + " TEXT," + KEY_TIME + " TEXT,"
                + KEY_TAG + " TEXT" + ")";
        db_reminder.execSQL(CREATE_REMINDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db_reminder, int oldVersion, int newVersion) {

        db_reminder.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);

        onCreate(db_reminder);
    }

    public void addReminder(Reminder reminder) {
        SQLiteDatabase db_reminder = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STAMP, reminder.getTstamp());
        values.put(KEY_DATE, reminder.getDate());
        values.put(KEY_TIME, reminder.getTime());
        values.put(KEY_TAG, reminder.getTag());

        db_reminder.insert(TABLE_REMINDERS, null, values);
        db_reminder.close();
    }

    public Reminder getReminder(long timestamp, String tag) {

        SQLiteDatabase db_reminder = this.getReadableDatabase();

        Cursor cursor = db_reminder.query(TABLE_REMINDERS, new String[] { KEY_ID, KEY_STAMP, KEY_DATE, KEY_TIME, KEY_TAG }, KEY_STAMP + " =? AND " + KEY_TAG + " =?",
                new String[] { String.valueOf(timestamp) , tag}, null, null, null, null);

        if (cursor.moveToFirst()) {

            Reminder reminder = new Reminder(Integer.parseInt(cursor.getString(0)), Long.parseLong(cursor.getString(1)),
                    cursor.getString(4), cursor.getString(2), cursor.getString(3));
            cursor.close();
            return reminder;
        }
        else {
            return null;
        }
    }

    public List<Reminder> getAllReminders() {

        List<Reminder> reminderList = new ArrayList<>();
        SQLiteDatabase db_reminder = this.getWritableDatabase();

        Cursor cursor = db_reminder.query(TABLE_REMINDERS, new String[]{KEY_ID, KEY_STAMP, KEY_DATE, KEY_TIME, KEY_TAG}, null,
                null, null, null, KEY_STAMP + " DESC", null);

        if (cursor.moveToLast()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setID(Integer.parseInt(cursor.getString(0)));
                reminder.setTstamp(Long.parseLong(cursor.getString(1)));
                reminder.setDate(cursor.getString(2));
                reminder.setTime(cursor.getString(3));
                reminder.setTag(cursor.getString(4));
                reminderList.add(reminder);
            } while (cursor.moveToPrevious());
        }
        cursor.close();

        return reminderList;
    }

    public int updateReminder(Reminder reminder) {
        SQLiteDatabase db_reminder = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STAMP, reminder.getTstamp());
        values.put(KEY_DATE, reminder.getDate());
        values.put(KEY_TIME, reminder.getTime());
        values.put(KEY_TAG, reminder.getTag());

        return db_reminder.update(TABLE_REMINDERS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(reminder.getID())});
    }

    public void deleteReminder(Reminder reminder) {
        SQLiteDatabase db_reminder = this.getWritableDatabase();
        db_reminder.delete(TABLE_REMINDERS, KEY_ID + " = ?",
                new String[] { String.valueOf(reminder.getID()) });
        db_reminder.close();
    }
}


