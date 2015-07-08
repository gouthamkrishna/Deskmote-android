package com.beaconapp.user.navigation;

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

import java.util.Calendar;


public class  LoggerService extends IntentService {

    public static final String ID = "com.beaconapp.user.database.ID";
    SharedPreferences sharedPref;
    Boolean defaultValue = false;
    Boolean test_val;
    DatabaseHandler db = new DatabaseHandler(this);
    long tstamp;
    long desktime;
    long officetime;
    long outdoortime;

    public LoggerService(){
        super("LoggerService");
    }

    public void onHandleIntent(Intent intent) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        test_val = sharedPref.getBoolean(getString(R.string.logger_init), defaultValue);

        if(!test_val) {

            tstamp = calendar.getTimeInMillis();
            Intent alarmIntent = new Intent(this, StatisticsLogger.class);
            alarmIntent.putExtra(ID, tstamp);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(LoggerService.this, 13572, alarmIntent, 0);
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            manager.setExact(AlarmManager.RTC_WAKEUP, tstamp, pendingIntent);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.logger_init), true);
            editor.commit();
        }

        else {

            tstamp = intent.getLongExtra(ID, 0);
            desktime = sharedPref.getLong(getString(R.string.shared_timer_desk), 0);
            officetime = sharedPref.getLong(getString(R.string.shared_timer_office), 0);
            outdoortime = sharedPref.getLong(getString(R.string.shared_timer_outdoor), 0);
            db.addDailyStat(new DailyStat(tstamp, desktime, outdoortime, officetime));
        }
    }
}

