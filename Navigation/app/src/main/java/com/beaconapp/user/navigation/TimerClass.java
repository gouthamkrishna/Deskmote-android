package com.beaconapp.user.navigation;

import android.os.Handler;

/**
 * Created by user on 25/6/15.
 */
public class TimerClass {

    public long startTime = 0L;
    public Handler customHandler = new Handler();
    public long timeInMilliseconds = 0L,lastPauseTime = 0L;

    public TimerClass() {
        startTime = 0L;
        timeInMilliseconds = 0L;
        lastPauseTime = 0L;
    }
    /*public void initialize () {
        startTime = 0L;
        timeInMilliseconds = 0L;
    }*/


}
