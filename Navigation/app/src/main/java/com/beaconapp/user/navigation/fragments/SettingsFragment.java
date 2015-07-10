package com.beaconapp.user.navigation.fragments;


import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.TimePicker;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.activities.MainActivity;

import java.util.Calendar;


public class SettingsFragment extends PreferenceFragment implements TimePickerDialog.OnTimeSetListener {

    int timepickerID;

    Preference pref_office_time_from, pref_lunch_break_to, pref_office_time_to, pref_lunch_break_from;
    TextView officeTimeFrom, officeTimeTo, lunchTimeFrom, lunchTimeTo;
    String picked_time, officeFrom, officeTo, lunchFrom, lunchTo;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;

    public SettingsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Settings");

        addPreferencesFromResource(R.xml.preferences);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        officeFrom = sharedPref.getString("office_start_time", "08 : 30");
        officeTo = sharedPref.getString("office_end_time", "05 : 30");
        lunchFrom = sharedPref.getString("lunch_start_time", "13 : 00");
        lunchTo = sharedPref.getString("lunch_end_time", "13 : 30");

        pref_office_time_from = findPreference("pref_key_office_time_from");
        pref_lunch_break_to = findPreference("pref_key_lunch_break_to");
        pref_office_time_to = findPreference("pref_key_office_time_to");
        pref_lunch_break_from = findPreference("pref_key_lunch_break_from");

        pref_office_time_from.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {

                officeTimeFrom = (TextView) getView().findViewById(R.id.officeFromTime);
                timepickerID = 1;
                showTimeDialog();
                return false;
            }
        });

        pref_office_time_to.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {

                officeTimeTo = (TextView)getView().findViewById(R.id.officeToTime);
                timepickerID=2;
                showTimeDialog();
                return false;
            }
        });

        pref_lunch_break_from.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {

                lunchTimeFrom = (TextView)getView().findViewById(R.id.lunchFromTime);
                timepickerID=3;
                showTimeDialog();
                return false;
            }
        });

        pref_lunch_break_to.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {

                lunchTimeTo = (TextView)getView().findViewById(R.id.lunchToTime);
                timepickerID=4;
                showTimeDialog();
                return false;
            }
        });

    }

    private void showTimeDialog(){

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        new TimePickerDialog(getActivity(),this, hour, minute, true).show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String hour=""+hourOfDay, min=""+minute;
        if(hourOfDay<10){hour = "0"+hourOfDay;}
        if(minute<10){min = "0"+minute;}

        switch (timepickerID) {
            case 1:
                picked_time = ""+hour+" : "+min;
                sharedPrefEditor = sharedPref.edit();
                sharedPrefEditor.putString("office_start_time", picked_time);
                sharedPrefEditor.putInt("pref_key_office_time_from_hour", hourOfDay);
                sharedPrefEditor.putInt("pref_key_office_time_from_minute", minute);
                sharedPrefEditor.commit();
                officeTimeFrom.setText(picked_time);
                break;
            case 2:
                picked_time = ""+hour+" : "+min;
                sharedPrefEditor = sharedPref.edit();
                sharedPrefEditor.putString("office_end_time", picked_time);
                sharedPrefEditor.putInt("pref_key_office_time_to_hour", hourOfDay);
                sharedPrefEditor.putInt("pref_key_office_time_to_minute", minute);
                sharedPrefEditor.commit();
                officeTimeTo.setText(picked_time);
                break;
            case 3:
                picked_time = ""+hour+" : "+min;
                sharedPrefEditor = sharedPref.edit();
                sharedPrefEditor.putString("lunch_start_time", picked_time);
                sharedPrefEditor.putInt("pref_key_lunch_time_from_hour", hourOfDay);
                sharedPrefEditor.putInt("pref_key_lunch_time_from_minute", minute);
                sharedPrefEditor.commit();
                lunchTimeFrom.setText(picked_time);
                break;
            case 4:
                picked_time = ""+hour+" : "+min;
                sharedPrefEditor = sharedPref.edit();
                sharedPrefEditor.putString("lunch_end_time", picked_time);
                sharedPrefEditor.putInt("pref_key_lunch_time_to_hour", hourOfDay);
                sharedPrefEditor.putInt("pref_key_lunch_time_to_minute", minute);
                sharedPrefEditor.commit();
                lunchTimeTo.setText(picked_time);
                break;
        }
    }
}