package com.beaconapp.user.navigation.receivers;


/**
 * Created by user on 30/6/15.
 */
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.beaconapp.user.navigation.services.LoggerService;
import com.beaconapp.user.navigation.services.NotificationService;


public class StatisticsLogger extends BroadcastReceiver {


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        String service = intent.getStringExtra("service_name");

        if (service.equals("notification")){
            Intent start_notification_service = new Intent(context, NotificationService.class);
            context.startService(start_notification_service);
        }
        else if (service.equals("logging")){
            Intent start_logger_service = new Intent(context, LoggerService.class);
            context.startService(start_logger_service);
        }
        else if (service.equals("notificationstop")) {
            Intent stopNotificationService = new Intent(context, NotificationService.class);
            context.stopService(stopNotificationService);
        }
    }
}
