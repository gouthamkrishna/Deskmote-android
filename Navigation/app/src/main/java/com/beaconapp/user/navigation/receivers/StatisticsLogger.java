package com.beaconapp.user.navigation.receivers;


/**
 * Created by user on 30/6/15.
 */
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.beaconapp.user.navigation.services.LoggerService;
import com.beaconapp.user.navigation.services.NotificationService;


public class StatisticsLogger extends BroadcastReceiver {

    public static final String TIMESTAMP_ID = "com.beaconapp.user.deskmote.TIMESTAMP_ID";
    public static final long SINGLEDAY_TIMESTAMP = 86400000L;
    public static final int ALARM_ID = 1729;

    long timeStamp = 0L;
    Intent alarmIntent, start_logger_service, start_notification_service;
    PendingIntent pendingIntent;
    AlarmManager manager;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        timeStamp = intent.getLongExtra(TIMESTAMP_ID, timeStamp);

        alarmIntent = new Intent(context, StatisticsLogger.class);
        alarmIntent.putExtra(TIMESTAMP_ID, timeStamp + SINGLEDAY_TIMESTAMP);
        pendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, alarmIntent, 0);
        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        start_logger_service = new Intent(context, LoggerService.class);
        start_notification_service = new Intent(context, NotificationService.class);
        start_logger_service.putExtra(TIMESTAMP_ID, timeStamp);
        context.startService(start_logger_service);
        context.startService(start_notification_service);

        manager.setExact(AlarmManager.RTC_WAKEUP, timeStamp, pendingIntent);

    }
}
