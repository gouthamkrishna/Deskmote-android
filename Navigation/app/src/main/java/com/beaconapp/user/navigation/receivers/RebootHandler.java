package com.beaconapp.user.navigation.receivers;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.beaconapp.user.navigation.services.ResetReminderService;

import java.util.Calendar;

/**
 * Created by user on 29/6/15.
 */
public class RebootHandler extends BroadcastReceiver {

    Intent resetReminderService;
    SharedPreferences sharedPref;

    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

            int office_from_hour = sharedPref.getInt("pref_key_office_time_from_hour", 8);
            int office_from_minute = sharedPref.getInt("pref_key_office_time_from_minute", 30);
            int office_to_hour = sharedPref.getInt("pref_key_office_time_to_hour", 17);
            int office_to_minute = sharedPref.getInt("pref_key_office_time_to_minute", 30);
            int lunch_from_hour  = sharedPref.getInt("pref_key_lunch_time_from_hour", 13);
            int lunch_from_minute = sharedPref.getInt("pref_key_lunch_time_from_minute", 0);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 50);
            Intent alarmIntent = new Intent(context, StatisticsLogger.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1729, alarmIntent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000L, pendingIntent);

            resetReminderService = new Intent(context, ResetReminderService.class);
            context.startService(resetReminderService);

            calendar.set(Calendar.HOUR_OF_DAY, office_from_hour);
            calendar.set(Calendar.MINUTE, office_from_minute);
            Intent startNotificationService = new Intent(context, StatisticsLogger.class);
            startNotificationService.putExtra("service_name", "notification");
            PendingIntent pendingnotificationService = PendingIntent.getBroadcast(context, 1727, startNotificationService, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000L, pendingnotificationService);

            calendar.set(Calendar.HOUR_OF_DAY, office_to_hour);
            calendar.set(Calendar.MINUTE, office_to_minute);
            Intent stopNotificationService = new Intent(context, StatisticsLogger.class);
            stopNotificationService.putExtra("service_name", "notificationstop");
            PendingIntent pendingStopNotificationService = PendingIntent.getBroadcast(context, 1726, stopNotificationService, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000L, pendingStopNotificationService);

            calendar.set(Calendar.HOUR_OF_DAY, lunch_from_hour);
            calendar.set(Calendar.MINUTE, lunch_from_minute);
            Intent lunchNotification = new Intent(context, StatisticsLogger.class);
            lunchNotification.putExtra("service_name", "lunchnotification");
            PendingIntent pendinglunchNotification = PendingIntent.getBroadcast(context, 1725, lunchNotification, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000L, pendinglunchNotification);

        }
    }
}
