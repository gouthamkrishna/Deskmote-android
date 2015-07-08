package com.beaconapp.user.navigation;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.TimePicker;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements TimePickerDialog.OnTimeSetListener {

    int timepickerID;
    Preference pref_key_office_time_from,pref_key_office_time_to, pref_key_lunch_time_from, pref_key_lunch_time_to;
    String picked_time;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public SettingsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Settings");

        addPreferencesFromResource(R.xml.preferences);

        pref_key_office_time_from = (Preference) findPreference("pref_key_office_time_from");
        pref_key_office_time_from.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                timepickerID=1;
                showTimeDialog();
                return false;
            }
        });
        pref_key_office_time_to = (Preference) findPreference("pref_key_office_time_to");
        pref_key_office_time_to.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                timepickerID=2;
                showTimeDialog();
                return false;
            }
        });
        pref_key_lunch_time_from = (Preference) findPreference("pref_key_lunch_time_from");
        pref_key_lunch_time_from.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                timepickerID=3;
                showTimeDialog();
                return false;
            }
        });
        pref_key_lunch_time_to = (Preference) findPreference("pref_key_lunch_time_to");
        pref_key_lunch_time_to.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onPreferenceClick(Preference preference) {
                timepickerID = 4;
                showTimeDialog();
                return false;
            }
        });

    }

    private void showTimeDialog(){
        // Use the current date as the default date in the picker
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
                picked_time = "FROM          "+hour+":"+min;
                pref_key_office_time_from.setTitle(picked_time);
                sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                editor = sharedPref.edit();
                editor.putInt("pref_key_office_time_from_hour", hourOfDay);
                editor.putInt("pref_key_office_time_from_minute", minute);
                editor.commit();
                break;
            case 2:
                picked_time = "TO               "+hour+":"+min;
                pref_key_office_time_to.setTitle(picked_time);
                sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                editor = sharedPref.edit();
                editor.putInt("pref_key_office_time_to_hour", hourOfDay);
                editor.putInt("pref_key_office_time_to_minute", minute);
                editor.commit();
                break;
            case 3:
                picked_time = "FROM          "+hour+":"+min;
                pref_key_lunch_time_from.setTitle(picked_time);
                sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                editor = sharedPref.edit();
                editor.putInt("pref_key_lunch_time_from_hour", hourOfDay);
                editor.putInt("pref_key_lunch_time_from_minute", minute);
                editor.commit();
                break;
            case 4:
                picked_time = "TO               "+hour+":"+min;
                pref_key_lunch_time_to.setTitle(picked_time);
                sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                editor = sharedPref.edit();
                editor.putInt("pref_key_lunch_time_to_hour", hourOfDay);
                editor.putInt("pref_key_lunch_time_to_minute", minute);
                editor.commit();
                break;
        }
    }
}
