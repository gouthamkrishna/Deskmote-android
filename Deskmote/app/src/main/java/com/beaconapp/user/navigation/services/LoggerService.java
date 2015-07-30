package com.beaconapp.user.navigation.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.classes.DailyStat;
import com.beaconapp.user.navigation.database.DatabaseHandler;

import java.util.Calendar;


public class  LoggerService extends IntentService {

    SharedPreferences sharedPref;
    DatabaseHandler db = new DatabaseHandler(this);

    public LoggerService(){
        super("LoggerService");
    }

    public void onHandleIntent(Intent intent) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Calendar calendar = Calendar.getInstance();
        long deskTime = sharedPref.getLong(getString(R.string.shared_timer_desk), 0);
        long officeTime = sharedPref.getLong(getString(R.string.shared_timer_office), 0);
        long outdoorTime = sharedPref.getLong(getString(R.string.shared_timer_outdoor), 0);
        db.addDailyStat(new DailyStat(calendar.getTimeInMillis(), deskTime, outdoorTime, officeTime));
    }
}

