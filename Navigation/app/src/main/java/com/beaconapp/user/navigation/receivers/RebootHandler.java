package com.beaconapp.user.navigation.receivers;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.services.LoggerService;
import com.beaconapp.user.navigation.services.NotificationService;
import com.beaconapp.user.navigation.services.ResetReminderService;

import java.util.Calendar;

/**
 * Created by user on 29/6/15.
 */
public class RebootHandler extends BroadcastReceiver {

    Intent resetReminderService, notificationService;

    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 50);
            Intent alarmIntent = new Intent(context, StatisticsLogger.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1729, alarmIntent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000L, pendingIntent);

            resetReminderService = new Intent(context, ResetReminderService.class);
            context.startService(resetReminderService);

            notificationService = new Intent(context, NotificationService.class);
            context.startService(notificationService);
        }
    }
}
