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

    public static final String ID = "com.beaconapp.user.database.ID";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        long tstamp = intent.getLongExtra(ID, 0);

        Intent alarmIntent = new Intent(context, StatisticsLogger.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 13572, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent start_logger_service = new Intent(context, LoggerService.class);
        Intent start_notification_service = new Intent(context, NotificationService.class);
        start_logger_service.putExtra(ID, tstamp);
        context.startService(start_logger_service);
        context.startService(start_notification_service);

        alarmIntent.putExtra(ID, tstamp + 86400000);
        manager.setExact(AlarmManager.RTC_WAKEUP, tstamp, pendingIntent);

    }
}
