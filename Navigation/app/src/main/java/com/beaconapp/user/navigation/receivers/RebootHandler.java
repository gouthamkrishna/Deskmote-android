package com.beaconapp.user.navigation.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.services.LoggerService;
import com.beaconapp.user.navigation.services.ResetReminderService;

/**
 * Created by user on 29/6/15.
 */
public class RebootHandler extends BroadcastReceiver {

    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
    Intent resetLoggerService, resetReminderService;

    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPrefEditor = sharedPref.edit();
            sharedPrefEditor.putBoolean(context.getString(R.string.logger_init), false);
            sharedPrefEditor.commit();
            resetLoggerService = new Intent(context, LoggerService.class);
            context.startService(resetLoggerService);
            resetReminderService = new Intent(context, ResetReminderService.class);
            context.startService(resetReminderService);
        }
    }
}
