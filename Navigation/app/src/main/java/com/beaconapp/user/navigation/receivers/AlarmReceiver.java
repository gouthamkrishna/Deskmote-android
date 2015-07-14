package com.beaconapp.user.navigation.receivers;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.beaconapp.user.navigation.R;

import java.util.Random;
//import android.os.Vibrator;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TAG = "com.beaconapp.user.deskmote.TAG";
    public static final int NOTIFICATION_ID = 113;
    public NotificationManager notificationManager;
    String reminderMessage;
    Notification notification;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        reminderMessage = intent.getStringExtra(TAG);
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
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
