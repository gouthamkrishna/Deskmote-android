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

    long desktime, officetime, outdoortime;

    SharedPreferences sharedPref;
    DatabaseHandler db = new DatabaseHandler(this);

    public LoggerService(){
        super("LoggerService");
    }

    public void onHandleIntent(Intent intent) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Calendar calendar = Calendar.getInstance();
        desktime = sharedPref.getLong(getString(R.string.shared_timer_desk), 0);
        officetime = sharedPref.getLong(getString(R.string.shared_timer_office), 0);
        outdoortime = sharedPref.getLong(getString(R.string.shared_timer_outdoor), 0);
        db.addDailyStat(new DailyStat(calendar.getTimeInMillis(), desktime, outdoortime, officetime));
    }
}

