package com.beaconapp.user.navigation.classes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.receivers.StatisticsLogger;
import com.beaconapp.user.navigation.services.NotificationService;

import java.util.Calendar;


public class SetTimePreference extends Preference implements TimePickerDialog.OnTimeSetListener {

    Context mContext;
    String mFromTime, mToTime, mCategory, fromTo;
    TextView mFromTimeView, mToTimeView, mTitle;
    RelativeLayout setTime;

    public SetTimePreference(Context context, String fromTime, String toTime, String category) {
        super(context);

        mContext = context;
        mFromTime = fromTime;
        mToTime = toTime;
        mCategory = category;
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        return inflater.inflate(R.layout.preference_set_time, null);
    }

    @Override
    protected void onBindView (@NonNull View view) {
        super.onBindView(view);
        setTime = (RelativeLayout)view.findViewById(R.id.fromToSet);
        setTime.setMinimumHeight(400);
        mFromTimeView = (TextView)view.findViewById(R.id.fromTextView);
        mFromTimeView.setText(mFromTime);
        mFromTimeView.setTextSize(18f);
        mToTimeView = (TextView)view.findViewById(R.id.toTextView);
        mToTimeView.setText(mToTime);
        mToTimeView.setTextSize(18f);
        mTitle = (TextView)view.findViewById(R.id.titleFromTo);
        mTitle.setText(mCategory);
        mTitle.setTextSize(16f);

        mFromTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromTo = "from";
                showTimeDialog();
            }
        });

        mToTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromTo = "to";
                showTimeDialog();
            }
        });
    }
    private void showTimeDialog(){

        new TimePickerDialog(mContext,this, 10, 10, true).show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String hour=""+hourOfDay, min=""+minute, picked_time, category;
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        if(hourOfDay<10){hour = "0"+hourOfDay;}
        if(minute<10){min = "0"+minute;}
        picked_time = ""+hour+" : "+min;

        if (mCategory.equals("Office Time")) {
            category = "office";
        }
        else {
            category = "lunch";
        }

        if (fromTo.equals("from")) {
            mFromTimeView.setText(picked_time);
        }
        else {
            mToTimeView.setText(picked_time);
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putString(category + "_" + fromTo + "_time", picked_time);
        sharedPrefEditor.putInt("pref_key_" + category + "_time_" + fromTo + "_hour", hourOfDay);
        sharedPrefEditor.putInt("pref_key_" + category + "_time_" + fromTo + "_minute", minute);
        sharedPrefEditor.apply();

        Calendar calendar = Calendar.getInstance();
        int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int current_min = calendar.get(Calendar.MINUTE);

        int office_from_hour = sharedPref.getInt("pref_key_office_time_from_hour", 8);
        int office_from_minute = sharedPref.getInt("pref_key_office_time_from_minute", 30);
        int office_to_hour = sharedPref.getInt("pref_key_office_time_to_hour", 17);
        int office_to_minute = sharedPref.getInt("pref_key_office_time_to_minute", 30);

        if (category.equals("office")) {
            if (fromTo.equals("from")) {
                calendar.set(Calendar.HOUR_OF_DAY, office_from_hour);
                calendar.set(Calendar.MINUTE, office_from_minute);
                Intent startNotificationService = new Intent(getContext(), StatisticsLogger.class);
                startNotificationService.putExtra("service_name", "notification");
                PendingIntent pendingnotificationService = PendingIntent.getBroadcast(getContext(), 1728, startNotificationService, 0);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000L, pendingnotificationService);
            }
            else {
                calendar.set(Calendar.HOUR_OF_DAY, office_to_hour);
                calendar.set(Calendar.MINUTE, office_to_minute);
                Intent stopNotificationService = new Intent(getContext(), StatisticsLogger.class);
                stopNotificationService.putExtra("service_name", "notificationstop");
                PendingIntent pendingStopNotificationService = PendingIntent.getBroadcast(getContext(), 1726, stopNotificationService, 0);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000L, pendingStopNotificationService);
            }
        }

        if (category.equals("lunch")) {
            int lunch_from_hour  = sharedPref.getInt("pref_key_lunch_time_from_hour", 13);
            int lunch_from_minute = sharedPref.getInt("pref_key_lunch_time_from_minute", 0);

            calendar.set(Calendar.HOUR_OF_DAY, lunch_from_hour);
            calendar.set(Calendar.MINUTE, lunch_from_minute);
            Intent lunchNotification = new Intent(getContext(), StatisticsLogger.class);
            lunchNotification.putExtra("service_name", "lunchnotification");
            PendingIntent pendinglunchNotification = PendingIntent.getBroadcast(getContext(), 1725, lunchNotification, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000L, pendinglunchNotification);
        }

        if (current_hour < office_from_hour) {
            Intent stopNotificationService = new Intent(getContext(), NotificationService.class);
            getContext().stopService(stopNotificationService);
        }

        else if(current_hour == office_from_hour && current_min < office_from_minute) {
            Intent stopNotificationService = new Intent(getContext(), NotificationService.class);
            getContext().stopService(stopNotificationService);
        }

        else if (current_hour > office_to_hour) {
            Intent stopNotificationService = new Intent(getContext(), NotificationService.class);
            getContext().stopService(stopNotificationService);
        }
        else if (current_hour == office_to_hour && current_min >office_to_minute) {
            Intent stopNotificationService = new Intent(getContext(), NotificationService.class);
            getContext().stopService(stopNotificationService);
        }
        else {
            Intent startNotificationService = new Intent(getContext(), NotificationService.class);
            getContext().startService(startNotificationService);
        }
    }
}
