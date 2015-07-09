package com.beaconapp.user.navigation.fragments;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.beaconapp.user.navigation.activities.MainActivity;
import com.beaconapp.user.navigation.R;

import java.util.Calendar;


public class SettingsFragment extends PreferenceFragment implements TimePickerDialog.OnTimeSetListener {

    int timepickerID;

    Preference pref_key_office_time, pref_key_lunch_break;
    TextView officeTimeFrom, officeTimeTo, lunchTimeFrom, lunchTimeTo;
    String picked_time, officeFrom, officeTo, lunchFrom, lunchTo;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;

    public SettingsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Settings");

        addPreferencesFromResource(R.xml.preferences);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        officeFrom = sharedPref.getString("office_start_time", "08 : 30");
        officeTo = sharedPref.getString("office_end_time", "05 : 30");
        lunchFrom = sharedPref.getString("lunch_start_time", "13 : 00");
        lunchTo = sharedPref.getString("lunch_end_time", "13 : 30");

        pref_key_office_time = findPreference("pref_key_office_time");
        pref_key_lunch_break = findPreference("pref_key_lunch_break");

        pref_key_office_time.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {

                officeTimeFrom = (TextView)getView().findViewById(R.id.officeTimeFrom);
                officeTimeTo = (TextView)getView().findViewById(R.id.officeTimeTo);

                officeTimeFrom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        officeTimeFrom.setText(officeFrom);
                        timepickerID=1;
                        showTimeDialog();
                    }
                });

                officeTimeTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        officeTimeTo.setText(officeTo);
                        timepickerID=2;
                        showTimeDialog();
                    }
                });

                return true;
            }
        });

        pref_key_lunch_break.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {

                lunchTimeFrom = (TextView)getView().findViewById(R.id.lunchTimeFrom);
                lunchTimeTo = (TextView)getView().findViewById(R.id.lunchTimeTo);

                lunchTimeFrom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lunchTimeFrom.setText(lunchFrom);
                        timepickerID=3;
                        showTimeDialog();
                    }
                });

                lunchTimeTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lunchTimeTo.setText(lunchTo);
                        timepickerID=4;
                        showTimeDialog();
                    }
                });
                return true;
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
                break;
            case 2:
                picked_time = ""+hour+" : "+min;
                sharedPrefEditor = sharedPref.edit();
                sharedPrefEditor.putString("office_end_time", picked_time);
                sharedPrefEditor.putInt("pref_key_office_time_to_hour", hourOfDay);
                sharedPrefEditor.putInt("pref_key_office_time_to_minute", minute);
                sharedPrefEditor.commit();
                break;
            case 3:
                picked_time = ""+hour+" : "+min;
                sharedPrefEditor = sharedPref.edit();
                sharedPrefEditor.putString("lunch_start_time", picked_time);
                sharedPrefEditor.putInt("pref_key_lunch_time_from_hour", hourOfDay);
                sharedPrefEditor.putInt("pref_key_lunch_time_from_minute", minute);
                sharedPrefEditor.commit();
                break;
            case 4:
                picked_time = ""+hour+" : "+min;
                sharedPrefEditor = sharedPref.edit();
                sharedPrefEditor.putString("lunch_end_time", picked_time);
                sharedPrefEditor.putInt("pref_key_lunch_time_to_hour", hourOfDay);
                sharedPrefEditor.putInt("pref_key_lunch_time_to_minute", minute);
                sharedPrefEditor.commit();
                break;
        }
    }
}