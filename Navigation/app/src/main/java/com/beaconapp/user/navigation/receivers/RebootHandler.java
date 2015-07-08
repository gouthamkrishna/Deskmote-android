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

    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(context.getString(R.string.logger_init), false);
            editor.commit();
            Intent resetter = new Intent(context, LoggerService.class);
            context.startService(resetter);
            Intent start_service = new Intent(context, ResetReminderService.class);
            context.startService(start_service);
        }
    }
}