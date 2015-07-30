package com.beaconapp.user.navigation.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.services.LoggerService;
import com.beaconapp.user.navigation.services.NotificationService;


public class StatisticsLogger extends BroadcastReceiver {

    SharedPreferences sharedPref;

    @Override
    public void onReceive(Context context, Intent intent) {

        String service = intent.getStringExtra("service_name");

        switch (service) {
            case "notification":
                sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
                sharedPrefEditor.putLong("Shared Timer Desk", 0L);
                sharedPrefEditor.putLong("Shared Timer Office", 0L);
                sharedPrefEditor.putLong("Shared Timer Outdoor", 0L);
                sharedPrefEditor.commit();
                Intent start_notification_service = new Intent(context, NotificationService.class);
                context.startService(start_notification_service);
                break;
            case "logging":
                Intent start_logger_service = new Intent(context, LoggerService.class);
                context.startService(start_logger_service);
                break;
            case "notificationstop":
                Intent stopNotificationService = new Intent(context, NotificationService.class);
                context.stopService(stopNotificationService);
                break;
            case "lunchnotification":
                sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                boolean notifications = sharedPref.getBoolean("pref_key_notifications", false);
                if (notifications) {
                    boolean breakAlert = sharedPref.getBoolean("pref_key_break", false);
                    if (breakAlert) {
                        Bitmap background = BitmapFactory.decodeResource(context.getResources(), R.drawable.wearbg);

                        NotificationCompat.WearableExtender wearableExtender =
                                new NotificationCompat.WearableExtender()
                                        .setBackground(background);
                        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.beacon_gray)
                                .setContentTitle("Break Alert")
                                .setContentText("Lunch Break")
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .extend(wearableExtender)
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager =  NotificationManagerCompat.from(context);
                        notificationManager.notify(153, notification.build());
                    }
                }
                break;
        }
    }
}