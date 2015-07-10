package com.beaconapp.user.navigation.services;

/**
 * Created by user on 30/6/15.
 */
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.receivers.StatisticsLogger;
import com.beaconapp.user.navigation.classes.DailyStat;
import com.beaconapp.user.navigation.database.DatabaseHandler;

import java.util.Calendar;


public class  LoggerService extends IntentService {

    public static final String TIMESTAMP_ID = "com.beaconapp.user.deskmote.TIMESTAMP_ID";
    public static final int ALARM_ID = 1729;
    long timestamp, desktime, officetime, outdoortime;

    SharedPreferences sharedPref;
    boolean defaultValue = false, test_val;
    DatabaseHandler db = new DatabaseHandler(this);
    Calendar calendar;
    Intent alarmIntent;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    SharedPreferences.Editor sharedPrefEditor;

    public LoggerService(){
        super("LoggerService");
    }

    public void onHandleIntent(Intent intent) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        test_val = sharedPref.getBoolean(getString(R.string.logger_init), defaultValue);

        if(!test_val) {

            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            timestamp = calendar.getTimeInMillis();
            alarmIntent = new Intent(this, StatisticsLogger.class);
            alarmIntent.putExtra(TIMESTAMP_ID, timestamp);
            pendingIntent = PendingIntent.getBroadcast(LoggerService.this, ALARM_ID, alarmIntent, 0);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);

            sharedPrefEditor = sharedPref.edit();
            sharedPrefEditor.putBoolean(getString(R.string.logger_init), true);
            sharedPrefEditor.commit();
        }

        else {

            timestamp = intent.getLongExtra(TIMESTAMP_ID, 0);
            desktime = sharedPref.getLong(getString(R.string.shared_timer_desk), 0);
            officetime = sharedPref.getLong(getString(R.string.shared_timer_office), 0);
            outdoortime = sharedPref.getLong(getString(R.string.shared_timer_outdoor), 0);
            db.addDailyStat(new DailyStat(timestamp, desktime, outdoortime, officetime));
        }
    }
}

