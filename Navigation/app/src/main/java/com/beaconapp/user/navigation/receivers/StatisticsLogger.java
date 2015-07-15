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

    Intent start_logger_service;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        start_logger_service = new Intent(context, LoggerService.class);
        context.startService(start_logger_service);
    }
}
