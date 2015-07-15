package com.beaconapp.user.navigation.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.receivers.StatisticsLogger;
import com.beaconapp.user.navigation.services.NotificationService;

import java.util.Calendar;


public class SplashActivity extends Activity {

    public static final int DELAY = 2000;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
    boolean defaultValue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefEditor = sharedPref.edit();


        boolean start = sharedPref.getBoolean(getString(R.string.shared_start), defaultValue);
        if (!start) {
            sharedPrefEditor.putBoolean(getString(R.string.shared_start), true);
            sharedPrefEditor.commit();

            Intent notificationServiceIntent = new Intent(this, NotificationService.class);
            startService(notificationServiceIntent);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 50);
            Intent alarmIntent = new Intent(this, StatisticsLogger.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1729, alarmIntent, 0);
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000L, pendingIntent);

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, DELAY);
    }

}
