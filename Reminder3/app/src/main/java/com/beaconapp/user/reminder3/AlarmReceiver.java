package com.beaconapp.user.reminder3;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
//import android.os.Vibrator;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TAG = "com.beaconapp.user.reminder3.TAG";
    public NotificationManager notificationManager;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        String mssg = intent.getStringExtra(TAG);
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Reminder")
                .setContentText(mssg)
                .setAutoCancel(true)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.ledARGB = 0xff00ff00;
        notification.ledOnMS = 1000;
        notification.ledOffMS = 300;
        //  v.vibrate(400);
        notificationManager.notify(10, notification);
    }
}
