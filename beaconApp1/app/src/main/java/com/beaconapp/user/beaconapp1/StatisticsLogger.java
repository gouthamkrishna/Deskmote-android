package com.beaconapp.user.beaconapp1;

/**
 * Created by user on 30/6/15.
 */
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


public class StatisticsLogger extends BroadcastReceiver {

    public static final String ID = "com.beaconapp.user.database.ID";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        long tstamp = intent.getLongExtra(ID, 0);

        Intent alarmIntent = new Intent(context, StatisticsLogger.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 13572, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent start_service = new Intent(context, LoggerService.class);
        start_service.putExtra(ID, tstamp);
        context.startService(start_service);

        alarmIntent.putExtra(ID, tstamp+86400000);
        manager.setExact(AlarmManager.RTC_WAKEUP, tstamp, pendingIntent);

    }
}
