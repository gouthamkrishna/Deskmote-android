package com.beaconapp.user.navigation.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.beaconapp.user.navigation.receivers.AlarmReceiver;
import com.beaconapp.user.navigation.classes.Reminder;
import com.beaconapp.user.navigation.database.ReminderDatabaseHandler;

import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 29/6/15.
 */
public class ResetReminderService extends IntentService {

    public PendingIntent pendingIntent;
    public static final String TAG = "com.beaconapp.user.deskmote.TAG";
    long currentTimestamp, reminderTimestamp;
    int listcounter=0;

    Intent alarmIntent;
    AlarmManager alarmManager;
    ReminderDatabaseHandler db = new ReminderDatabaseHandler(this);
    List<Reminder> reminderList;
    Calendar calendar;
    Reminder reminder;

    public ResetReminderService(){
        super("ResetReminderService");
    }

    public void onHandleIntent(Intent intent) {
        reminderList = db.getAllReminders();

        calendar = Calendar.getInstance();
        currentTimestamp = calendar.getTimeInMillis();
        listcounter=0;

        while (listcounter<reminderList.size()) {
            reminder = reminderList.get(listcounter);
            Toast.makeText(ResetReminderService.this, "Resetting Reminders!!", Toast.LENGTH_LONG).show();

            reminderTimestamp = reminder.getTstamp();
            if(reminderTimestamp>=currentTimestamp){
                alarmIntent = new Intent(ResetReminderService.this, AlarmReceiver.class);
                alarmIntent.putExtra(TAG, reminder.getTag());
                pendingIntent = PendingIntent.getBroadcast(ResetReminderService.this, reminder.getID(), alarmIntent, 0);
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimestamp, pendingIntent);
            }
            listcounter++;
        }
    }
}
