package com.beaconapp.user.beaconapp1;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

/**
 * Created by user on 25/6/15.
 */
public class TimerClass extends Service {

    public long startTime = 0L;
    public Handler customHandler = new Handler();
    public long updatedTime = 0L, timeSwapBuff = 0L, timeInMilliseconds = 0L;
    public int hours, mins, secs,day_start;

    public TimerClass() {
        startTime = 0L;
        updatedTime = 0L;
        timeSwapBuff = 0L;
        timeInMilliseconds = 0L;
        hours = 0;
        mins = 0;
        secs = 0;
        day_start = 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
