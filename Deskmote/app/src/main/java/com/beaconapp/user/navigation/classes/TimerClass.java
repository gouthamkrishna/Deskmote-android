package com.beaconapp.user.navigation.classes;

import android.os.Handler;

public class TimerClass {

    public long startTime = 0L;
    public Handler customHandler = new Handler();
    public long timeInMilliseconds = 0L, lastPauseTime = 0L;

    public TimerClass() {
        startTime = 0L;
        timeInMilliseconds = 0L;
        lastPauseTime = 0L;
    }
}
