package com.beaconapp.user.navigation.receivers;


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

import java.util.Random;
//import android.os.Vibrator;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TAG = "com.beaconapp.user.deskmote.TAG";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean notifications = sharedPreferences.getBoolean("pref_key_notifications", false);
        if(notifications) {
            boolean reminders = sharedPreferences.getBoolean("pref_key_reminders", false);
            if(reminders) {
                String reminderMessage = intent.getStringExtra(TAG);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new Notification.Builder(context)
                        .setSmallIcon(R.drawable.alarm4)
                        .setContentTitle("Reminder")
                        .setContentText(reminderMessage)
                        .setAutoCancel(true)
                        .build();
                notification.defaults |= Notification.DEFAULT_SOUND;
                notification.defaults |= Notification.DEFAULT_LIGHTS;
                notification.defaults |= Notification.DEFAULT_VIBRATE;
                notification.flags |= Notification.FLAG_SHOW_LIGHTS;
                notification.ledARGB = 0xff00ff00;
                notification.ledOnMS = 1000;
                notification.ledOffMS = 300;

                notificationManager.notify(new Random().nextInt(100), notification);
            }
        }
    }
}
