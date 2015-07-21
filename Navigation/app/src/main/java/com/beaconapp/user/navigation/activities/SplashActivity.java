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

            sharedPrefEditor.putBoolean(getString(R.string.shared_start), true);
            sharedPrefEditor.commit();

            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            int hour = sharedPref.getInt("pref_key_office_time_from_hour", 8);
            int minute = sharedPref.getInt("pref_key_office_time_from_minute", 30);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            Intent notificationService = new Intent(this, StatisticsLogger.class);
            notificationService.putExtra("service_name", "notification");
            PendingIntent pendingnotificationService = PendingIntent.getBroadcast(this, 1728, notificationService, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000L, pendingnotificationService);

            Intent notificationServiceIntent = new Intent(this, NotificationService.class);
            startService(notificationServiceIntent);

            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 50);
            Intent loggerService = new Intent(this, StatisticsLogger.class);
            loggerService.putExtra("service_name", "logging");
            PendingIntent pendingLoggerService = PendingIntent.getBroadcast(this, 1729, loggerService, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000L, pendingLoggerService);

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
