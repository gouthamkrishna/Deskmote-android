package com.beaconapp.user.navigation.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.beaconapp.user.navigation.classes.DailyStat;
import com.beaconapp.user.navigation.database.DatabaseHandler;

import java.util.Calendar;
import java.util.Random;


public class TestCaseGenerator extends IntentService {

    DatabaseHandler db = new DatabaseHandler(this);
    long desktime, officetime, outdoortime, timeStamp;
    int testCase, looper;
    Random random;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public TestCaseGenerator() {
        super("TestCaseGenerator");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("TestCaseGenerator", true)) {
            random = new Random();
            testCase = intent.getIntExtra("case", 2);
            switch (testCase) {
                case 1:
                    looper = 15;
                    break;
                case 2:
                    looper = 365;
                    break;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            timeStamp = calendar.getTimeInMillis();
            desktime = 60000L;
            for (int i = 0; i < looper; i++) {
                desktime = desktime + 60000L;
                officetime = desktime + 60000L;
                outdoortime = officetime + 60000L;
                db.addDailyStat(new DailyStat(timeStamp, desktime, outdoortime, officetime));
                timeStamp = timeStamp + 86400000L;
            }
            editor = sharedPreferences.edit();
            editor.putBoolean("TestCaseGenerator", false);
            editor.apply();
        }
    }
}