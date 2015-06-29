package com.beaconapp.user.reminder3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by user on 29/6/15.
 */
public class AlarmResetter extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            Intent start_service = new Intent(context, ResetReminderService.class);
            context.startService(start_service);
        }
    }
}
