package com.beaconapp.user.navigation.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.beaconapp.user.navigation.receivers.AlarmReceiver;
import com.beaconapp.user.navigation.classes.Reminder;
import com.beaconapp.user.navigation.database.ReminderDatabaseHandler;

import java.util.Calendar;
import java.util.List;

public class ResetReminderService extends IntentService {

    public PendingIntent pendingIntent;
    public static final String TAG = "com.beaconapp.user.deskmote.TAG";
    long currentTimestamp, reminderTimestamp;
    int listCounter=0;

    Intent alarmIntent;
    AlarmManager alarmManager;
    ReminderDatabaseHandler db = new ReminderDatabaseHandler(this);
    Reminder reminder;

    public ResetReminderService(){
        super("ResetReminderService");
    }

    public void onHandleIntent(Intent intent) {

        List<Reminder> reminderList = db.getAllReminders();
        Calendar calendar = Calendar.getInstance();
        currentTimestamp = calendar.getTimeInMillis();
        listCounter=0;

        while (listCounter<reminderList.size()) {
            reminder = reminderList.get(listCounter);

            reminderTimestamp = reminder.getTstamp();
            if(reminderTimestamp>=currentTimestamp){
                alarmIntent = new Intent(ResetReminderService.this, AlarmReceiver.class);
                alarmIntent.putExtra(TAG, reminder.getTag());
                pendingIntent = PendingIntent.getBroadcast(ResetReminderService.this, reminder.getID(), alarmIntent, 0);
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimestamp, pendingIntent);
            }
            listCounter++;
        }
    }
}
