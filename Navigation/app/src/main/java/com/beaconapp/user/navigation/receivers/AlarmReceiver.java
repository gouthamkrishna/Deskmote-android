package com.beaconapp.user.navigation.receivers;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.activities.MainActivity;

import java.util.Random;
//import android.os.Vibrator;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TAG = "com.beaconapp.user.deskmote.TAG";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean notifications = sharedPreferences.getBoolean("pref_key_notifications", true);
        if(notifications) {
            boolean reminders = sharedPreferences.getBoolean("pref_key_reminders", true);
            if(reminders) {
                String reminderMessage = intent.getStringExtra(TAG);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_action_alarm4)
                        .setAutoCancel(true)
                        .build();

                Intent startMyActivity = new Intent(context, MainActivity.class);
                startMyActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent myIntent = PendingIntent.getActivity(context, 0, startMyActivity, 0);
                notification.setLatestEventInfo(context,"Reminder", reminderMessage,myIntent);

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
