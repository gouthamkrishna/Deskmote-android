package com.beaconapp.user.navigation.services;

/**
 * Created by user on 30/6/15.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.classes.TimerClass;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 22/6/15.
 */
public class NotificationService extends Service {

    private BeaconManager beaconManager1, beaconManager2, beaconManager3;
    private NotificationManager notificationManager;
    public Handler cHandler = new Handler();
    private Region region_door_entry, region_desk, region_door_exit;
    private int notification_id = 0;
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

        cHandler.postDelayed(breakAlert, 0);

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
                postNotification("Entered region_door_entry", "Region Notification");
            }

            @Override
            public void onExitedRegion(Region region) {
                postNotification("Exited region_door_entry", "Region Notification");
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
                postNotification("Entered region_door_exit", "Region Notification");
            }

            @Override
            public void onExitedRegion(Region region) {
                postNotification("Exited region_door_exit", "Region Notification");
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
                postNotification("Entered region_desk", "Region Notification");
            }

            @Override
            public void onExitedRegion(Region region) {

                sharedPrefEditor.putInt(getString(R.string.shared_position), 2);
                sharedPrefEditor.commit();

                pause(obj1);
                obj = 2;
                obj2.startTime = SystemClock.uptimeMillis() - sharedPref.getLong(getString(R.string.shared_timer_office), 0);
                obj2.customHandler.postDelayed(updateTimerThread, 0);
                postNotification("Exited region_desk", "Region Notification");
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
            if (((ob.timeInMilliseconds - ob.lastPauseTime)/1000) == 10 ) {
                String msg2 = "Go Take a Break";
                postNotification(msg2, "Health Warning");
            }
        }
        else if(ob == obj2) {
            shared_variable = getString(R.string.shared_timer_office);}

        else if(ob == obj3) {
            shared_variable = getString(R.string.shared_timer_outdoor);
            /*if (((ob.timeInMilliseconds - ob.lastPauseTime)/1000) == 10 ) {
                String msg2 = "Get inside Office";
                postNotification(msg2, "Office Hours");
            }*/
        }

        sharedPrefEditor.putLong(shared_variable, ob.timeInMilliseconds);
        sharedPrefEditor.commit();

    }

    public void pause(TimerClass ob) {
        ob.lastPauseTime = ob.timeInMilliseconds;
        ob.customHandler.removeCallbacks(updateTimerThread);
    }

    public Runnable breakAlert = new Runnable() {
        public void run() {
            Calendar calendar = Calendar.getInstance();
            int hr = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            if((hr == sharedPref.getInt("pref_key_office_time_to_hour", 17)) && (min == sharedPref.getInt("pref_key_office_time_to_minute", 30))) {
                obj1.customHandler.removeCallbacks(updateTimerThread);
                obj2.customHandler.removeCallbacks(updateTimerThread);
                obj3.customHandler.removeCallbacks(updateTimerThread);
                stopSelf();
            }

            else if((hr == sharedPref.getInt("pref_key_office_time_from_hour", 8)) && (min == sharedPref.getInt("pref_key_office_time_from_minute", 30))) {
                obj1.startTime = SystemClock.uptimeMillis();
                obj2.startTime = SystemClock.uptimeMillis();
                obj3.startTime = SystemClock.uptimeMillis();
                sharedPrefEditor.putLong(getString(R.string.shared_timer_desk), 0L);
                sharedPrefEditor.putLong(getString(R.string.shared_timer_office), 0L);
                sharedPrefEditor.putLong(getString(R.string.shared_timer_outdoor),0L);
                sharedPrefEditor.commit();
            }

            else if ((hr == 11 && min == 00) || (hr == 16 && min == 00)) {
                postNotification("Tea Break", "Break Alert");
            }

            else if (hr == sharedPref.getInt("pref_key_lunch_time_from_hour", 13) && min == sharedPref.getInt("pref_key_lunch_time_from_minute", 0)) {
                postNotification("Lunch Break", "Break Alert");
            }
            cHandler.postDelayed(breakAlert, 60 * 1000);
        }
    };

    private void postNotification(String msg, String title) {
        Notification notification = new Notification.Builder(getBaseContext())
                .setSmallIcon(R.drawable.beacon_gray)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notificationManager.notify(notification_id, notification);
    }
}