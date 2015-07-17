package com.beaconapp.user.navigation.receivers;


/**
 * Created by user on 30/6/15.
 */
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.services.LoggerService;
import com.beaconapp.user.navigation.services.NotificationService;


public class StatisticsLogger extends BroadcastReceiver {

    SharedPreferences sharedPref;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        String service = intent.getStringExtra("service_name");

        if (service.equals("notification")) {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
            sharedPrefEditor.putLong("Shared Timer Desk", 0L);
            sharedPrefEditor.putLong("Shared Timer Office", 0L);
            sharedPrefEditor.putLong("Shared Timer Outdoor", 0L);
            sharedPrefEditor.apply();
            Intent start_notification_service = new Intent(context, NotificationService.class);
            context.startService(start_notification_service);
        }
        else if (service.equals("logging")) {
            Intent start_logger_service = new Intent(context, LoggerService.class);
            context.startService(start_logger_service);
        }
        else if (service.equals("notificationstop")) {
            Intent stopNotificationService = new Intent(context, NotificationService.class);
            context.stopService(stopNotificationService);
        }
        else if (service.equals("lunchnotification")) {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            boolean notifications = sharedPref.getBoolean("pref_key_notifications", false);
            if (notifications) {
                boolean breakAlert = sharedPref.getBoolean("pref_key_break", false);
                if (breakAlert) {
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification = new Notification.Builder(context)
                            .setSmallIcon(R.drawable.beacon_gray)
                            .setContentTitle("Break Alert")
                            .setContentText("Lunch Break")
                            .setAutoCancel(true)
                            .build();
                    notification.defaults |= Notification.DEFAULT_SOUND;
                    notification.defaults |= Notification.DEFAULT_LIGHTS;
                    notificationManager.notify(1, notification);
                }
            }
        }
    }
}