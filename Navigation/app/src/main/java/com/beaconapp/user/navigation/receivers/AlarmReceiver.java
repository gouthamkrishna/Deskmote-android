package com.beaconapp.user.navigation.receivers;


import android.app.PendingIntent;
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
import com.beaconapp.user.navigation.activities.MainActivity;

import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TAG = "com.beaconapp.user.deskmote.TAG";

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean notifications = sharedPreferences.getBoolean("pref_key_notifications", false);
        if(notifications) {
            boolean reminders = sharedPreferences.getBoolean("pref_key_reminders", false);
            if(reminders) {
                String reminderMessage = intent.getStringExtra(TAG);
                Intent startMyActivity = new Intent(context, MainActivity.class);
                startMyActivity.putExtra("LaunchType","reminder");
                startMyActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent myIntent = PendingIntent.getActivity(context, 0, startMyActivity, 0);

                Bitmap background = BitmapFactory.decodeResource(context.getResources(), R.drawable.wearbg);
                NotificationCompat.WearableExtender wearableExtender =
                        new NotificationCompat.WearableExtender()
                                .setBackground(background);

                NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_action_alarm4)
                        .setContentTitle("Reminder")
                        .setContentIntent(myIntent)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setContentText(reminderMessage)
                        .extend(wearableExtender)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(new Random().nextInt(100), notification.build());
            }
        }
    }
}
