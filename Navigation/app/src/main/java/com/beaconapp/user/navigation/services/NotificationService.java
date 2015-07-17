package com.beaconapp.user.navigation.services;

/**
 * Created by user on 22/6/15.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.classes.TimerClass;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class NotificationService extends Service {

    private BeaconManager beaconManager1, beaconManager2, beaconManager3;
    private NotificationManager notificationManager;
    private Region region_door_entry, region_desk, region_door_exit;
    public int obj = 0;
    BluetoothAdapter bt=null;
    TimerClass obj1 = new TimerClass();
    TimerClass obj2 = new TimerClass();
    TimerClass obj3 = new TimerClass();
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
    String shared_variable = "";

    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        bt= BluetoothAdapter.getDefaultAdapter();

        if(!bt.isEnabled())
            bt.enable();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefEditor = sharedPref.edit();

        region_door_entry = new Region("regionId", "b9407f30-f5f8-466e-aff9-25556b57fe6d", 29666, 63757);
        region_desk = new Region("regionId", "b9407f30-f5f8-466e-aff9-25556b57fe6d", 36798, 29499);
        region_door_exit = new Region("regionId", "b9407f30-f5f8-466e-aff9-25556b57fe6d", 64157, 33188);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        beaconManager1 = new BeaconManager(this);
        beaconManager2 = new BeaconManager(this);
        beaconManager3 = new BeaconManager(this);

        beaconManager1.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 1);
        beaconManager2.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 1);
        beaconManager3.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 1);

        beaconManager1.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {

                if ((sharedPref.getInt(getString(R.string.shared_door_exit), 0)) == 1) {
                    sharedPrefEditor.putInt(getString(R.string.shared_door_entry), 1);
                    sharedPrefEditor.commit();

                    sharedPrefEditor.putInt(getString(R.string.shared_position), 2);
                    sharedPrefEditor.commit();

                    pause(obj3);
                    obj = 2;
                    obj2.startTime = SystemClock.uptimeMillis() - sharedPref.getLong(getString(R.string.shared_timer_office), 0);
                    obj2.customHandler.postDelayed(updateTimerThread, 0);
                } else {
                    sharedPrefEditor.putInt(getString(R.string.shared_door_entry), 1);
                    sharedPrefEditor.commit();
                }
            }

            @Override
            public void onExitedRegion(Region region) {
            }
        });

        beaconManager2.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                if (sharedPref.getInt(getString(R.string.shared_door_entry), 0) == 1) {

                    sharedPrefEditor.putInt(getString(R.string.shared_door_exit), 1);
                    sharedPrefEditor.commit();

                    sharedPrefEditor.putInt(getString(R.string.shared_position), 3);
                    sharedPrefEditor.commit();
                    pause(obj2);
                    obj = 3;
                    obj3.startTime = SystemClock.uptimeMillis() - sharedPref.getLong(getString(R.string.shared_timer_outdoor), 0);
                    obj3.customHandler.postDelayed(updateTimerThread, 0);
                } else {
                    sharedPrefEditor.putInt(getString(R.string.shared_door_exit), 1);
                    sharedPrefEditor.commit();
                }
            }

            @Override
            public void onExitedRegion(Region region) {
            }
        });

        beaconManager3.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {

                sharedPrefEditor.putInt(getString(R.string.shared_position), 1);
                sharedPrefEditor.commit();

                pause(obj2);
                obj = 1;
                obj1.startTime = SystemClock.uptimeMillis() - sharedPref.getLong(getString(R.string.shared_timer_desk), 0);
                obj1.customHandler.postDelayed(updateTimerThread, 0);
            }

            @Override
            public void onExitedRegion(Region region) {

                sharedPrefEditor.putInt(getString(R.string.shared_position), 2);
                sharedPrefEditor.commit();

                pause(obj1);
                obj = 2;
                obj2.startTime = SystemClock.uptimeMillis() - sharedPref.getLong(getString(R.string.shared_timer_office), 0);
                obj2.customHandler.postDelayed(updateTimerThread, 0);
            }
        });

        beaconManager1.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager1.startMonitoring(region_door_entry);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        beaconManager2.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager2.startMonitoring(region_door_exit);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        beaconManager3.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager3.startMonitoring(region_desk);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        obj = 3;
        obj3.startTime = SystemClock.uptimeMillis() - sharedPref.getLong(getString(R.string.shared_timer_outdoor), 0);
        obj3.customHandler.postDelayed(updateTimerThread, 0);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        obj1.customHandler.removeCallbacks(updateTimerThread);
        obj2.customHandler.removeCallbacks(updateTimerThread);
        obj3.customHandler.removeCallbacks(updateTimerThread);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            if(obj ==1) {
                obj1.timeInMilliseconds = SystemClock.uptimeMillis() - obj1.startTime;
                findTime(obj1);
                obj1.customHandler.postDelayed(this, 1000);
            }
            else if(obj == 2) {
                obj2.timeInMilliseconds = SystemClock.uptimeMillis() - obj2.startTime;
                findTime(obj2);
                obj2.customHandler.postDelayed(this, 1000);
            }
            else if(obj == 3) {
                obj3.timeInMilliseconds = SystemClock.uptimeMillis() - obj3.startTime;
                findTime(obj3);
                obj3.customHandler.postDelayed(this, 1000);
            }
        }
    };

    public void findTime(TimerClass ob) {

        if(ob == obj1) {
            shared_variable = getString(R.string.shared_timer_desk);
            if ((sharedPref.getBoolean("pref_key_health",true) && ((ob.timeInMilliseconds - ob.lastPauseTime)/1000) == 40) ) {
                String msg2 = "Go Take a Break";
                postNotification(msg2, "Health Warning");
            }
        }
        else if(ob == obj2) {
            shared_variable = getString(R.string.shared_timer_office);}

        else if(ob == obj3) {
            shared_variable = getString(R.string.shared_timer_outdoor);
            if (((ob.timeInMilliseconds - ob.lastPauseTime)/1000) == 10 ) {
                String msg2 = "Get inside Office";
                postNotification(msg2, "Office Hours");
            }
        }

        sharedPrefEditor.putLong(shared_variable, ob.timeInMilliseconds);
        sharedPrefEditor.commit();

    }

    public void pause(TimerClass ob) {
        ob.lastPauseTime = ob.timeInMilliseconds;
        ob.customHandler.removeCallbacks(updateTimerThread);
    }

    private void postNotification(String msg, String title) {
        if (sharedPref.getBoolean("pref_key_notifications", true)) {
            Notification notification = new Notification.Builder(getBaseContext())
                    .setSmallIcon(R.drawable.beacon_gray)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .build();
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_LIGHTS;
            notificationManager.notify(0, notification);
        }
    }
}