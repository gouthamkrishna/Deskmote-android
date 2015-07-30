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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.receivers.StatisticsLogger;
import com.beaconapp.user.navigation.services.NotificationService;
import com.beaconapp.user.navigation.services.TestCaseGenerator;

import java.util.Calendar;


public class SplashActivity extends Activity {

    public static final int DELAY = 4000;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StartAnimations();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefEditor = sharedPref.edit();

        if (!(sharedPref.getBoolean(getString(R.string.shared_start), false))) {

            Intent intent = new Intent(this, TestCaseGenerator.class);
            startService(intent);

            Calendar calendar = Calendar.getInstance();
            long currentTime = calendar.getTimeInMillis();
            while (calendar.get(Calendar.DAY_OF_WEEK) > 1) {
                calendar.add(Calendar.DATE, -1);
            }
            calendar.add(Calendar.DATE, 6);

            sharedPrefEditor.putLong("installed_weekend", calendar.getTimeInMillis());
            sharedPrefEditor.putBoolean(getString(R.string.shared_start), true);
            sharedPrefEditor.commit();
            calendar.setTimeInMillis(currentTime);

            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            int startHour = sharedPref.getInt("pref_key_office_time_from_hour", 8);
            int startMinute = sharedPref.getInt("pref_key_office_time_from_minute", 30);

            calendar.set(Calendar.HOUR_OF_DAY, startHour);
            calendar.set(Calendar.MINUTE, startMinute);
            if(currentTime > calendar.getTimeInMillis()) {
                calendar.add(Calendar.DATE, 1);
            }
            Intent notificationService = new Intent(this, StatisticsLogger.class);
            notificationService.putExtra("service_name", "notification");
            PendingIntent pendingnotificationService = PendingIntent.getBroadcast(this, 1728, notificationService, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000L, pendingnotificationService);
            calendar.setTimeInMillis(currentTime);

            Intent notificationServiceIntent = new Intent(this, NotificationService.class);
            startService(notificationServiceIntent);

            int stopHour = sharedPref.getInt("pref_key_office_time_to_hour", 17);
            int stopMinute = (sharedPref.getInt("pref_key_office_time_to_minute", 30));

            calendar.set(Calendar.HOUR_OF_DAY, stopHour);
            calendar.set(Calendar.MINUTE, stopMinute + 5);
            if(currentTime > calendar.getTimeInMillis()) {
                calendar.add(Calendar.DATE, 1);
            }
            Intent loggerService = new Intent(this, StatisticsLogger.class);
            loggerService.putExtra("service_name", "logging");
            PendingIntent pendingLoggerService = PendingIntent.getBroadcast(this, 1729, loggerService, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000L, pendingLoggerService);
            calendar.setTimeInMillis(currentTime);

            calendar.set(Calendar.HOUR_OF_DAY, stopHour);
            calendar.set(Calendar.MINUTE, stopMinute);
            if(currentTime > calendar.getTimeInMillis()) {
                calendar.add(Calendar.DATE, 1);
            }
            Intent notificationStop = new Intent(this, StatisticsLogger.class);
            notificationStop.putExtra("service_name", "notificationstop");
            PendingIntent pendingStopService = PendingIntent.getBroadcast(this, 1726, notificationStop, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000L, pendingStopService);
            calendar.setTimeInMillis(currentTime);

            int lunchStart = sharedPref.getInt("pref_key_lunch_time_from_hour", 13);
            int lunchStop = sharedPref.getInt("pref_key_lunch_time_from_minute", 00);

            calendar.set(Calendar.HOUR_OF_DAY, lunchStart);
            calendar.set(Calendar.MINUTE, lunchStop);
            if(currentTime > calendar.getTimeInMillis()) {
                calendar.add(Calendar.DATE, 1);
            }
            Intent lunchNotification = new Intent(this, StatisticsLogger.class);
            lunchNotification.putExtra("service_name", "lunchnotification");
            PendingIntent pendinglunchNotification = PendingIntent.getBroadcast(this, 1725, lunchNotification, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000L, pendinglunchNotification);

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                mainIntent.putExtra("LaunchType","start");
                startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, DELAY);
    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout r=(RelativeLayout) findViewById(R.id.splash_layout);
        r.clearAnimation();
        r.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash_image);
        iv.clearAnimation();
        iv.startAnimation(anim);

    }
}
