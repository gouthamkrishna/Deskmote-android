package com.beaconapp.user.navigation.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.activities.MainActivity;
import com.beaconapp.user.navigation.classes.SetTimePreference;


public class SettingsFragment extends PreferenceFragment {

    SetTimePreference pref_office_time, pref_lunch_break;
    String officeFrom, officeTo, lunchFrom, lunchTo;
    SharedPreferences sharedPref;
    PreferenceScreen preferenceScreen;
    PreferenceCategory setTimings;

    public SettingsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Settings");

        addPreferencesFromResource(R.xml.preferences);

        preferenceScreen = this.getPreferenceScreen();
        setTimings = new PreferenceCategory(preferenceScreen.getContext());

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        officeFrom = sharedPref.getString("office_from_time", "08 : 30");
        officeTo = sharedPref.getString("office_to_time", "05 : 30");
        lunchFrom = sharedPref.getString("lunch_from_time", "13 : 00");
        lunchTo = sharedPref.getString("lunch_to_time", "13 : 30");

        setTimings.setTitle("SET TIMINGS");
        preferenceScreen.addPreference(setTimings);
        pref_office_time = new SetTimePreference(preferenceScreen.getContext(), officeFrom, officeTo, "Office Time");
        pref_office_time.setKey("key_office_time");
        setTimings.addPreference(pref_office_time);
        pref_lunch_break = new SetTimePreference(preferenceScreen.getContext(), lunchFrom, lunchTo, "Lunch Break");
        pref_lunch_break.setKey("key_lunch_break");
        setTimings.addPreference(pref_lunch_break);
    }
}
