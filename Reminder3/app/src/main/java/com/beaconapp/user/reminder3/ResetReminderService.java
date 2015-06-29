package com.beaconapp.user.reminder3;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 29/6/15.
 */
public class ResetReminderService extends IntentService {

    public PendingIntent pendingIntent;
    public static final String TAG = "com.beaconapp.user.reminder3.TAG";
    Intent alarmIntent;
    AlarmManager manager;

    DatabaseHandler db = new DatabaseHandler(this);

    public ResetReminderService(){
        super("ResetReminderService");
    }

    public void onHandleIntent(Intent intent) {
        List<Reminder> reminders = db.getAllReminders();

        Calendar calendar = Calendar.getInstance();
        long ttstamp = calendar.getTimeInMillis();
        int listcounter=0;

        while (listcounter<reminders.size()) {
            Reminder rmndr = reminders.get(listcounter);
            Toast.makeText(ResetReminderService.this, "Resetting!!", Toast.LENGTH_SHORT).show();

            long tttstamp = rmndr.getTstamp();
            if(tttstamp>=ttstamp){
                alarmIntent = new Intent(ResetReminderService.this, AlarmReceiver.class);
                alarmIntent.putExtra(TAG, rmndr.getTag());
                pendingIntent = PendingIntent.getBroadcast(ResetReminderService.this, rmndr.getID(), alarmIntent, 0);
                manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                manager.setExact(AlarmManager.RTC_WAKEUP, tttstamp, pendingIntent);
            }
            listcounter++;
        }
    }
}
