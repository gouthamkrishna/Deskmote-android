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

    SetTimePreference pref_office_time_from, pref_lunch_break_to, pref_office_time_to, pref_lunch_break_from;
    String officeFrom, officeTo, lunchFrom, lunchTo;
    SharedPreferences sharedPref;
    PreferenceScreen preferenceScreen;
    PreferenceCategory categoryOffice, categoryLunch;

    public SettingsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Settings");

        addPreferencesFromResource(R.xml.preferences);

        preferenceScreen = this.getPreferenceScreen();
        categoryOffice = new PreferenceCategory(preferenceScreen.getContext());
        categoryLunch = new PreferenceCategory(preferenceScreen.getContext());

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        officeFrom = sharedPref.getString("office_from_time", "08 : 30");
        officeTo = sharedPref.getString("office_to_time", "05 : 30");
        lunchFrom = sharedPref.getString("lunch_from_time", "13 : 00");
        lunchTo = sharedPref.getString("lunch_to_time", "13 : 30");

        categoryOffice.setTitle("Office Timing");
        preferenceScreen.addPreference(categoryOffice);
        pref_office_time_from = new SetTimePreference(preferenceScreen.getContext(), "FROM", officeFrom, "office");
        pref_office_time_from.setKey("key_office_from");
        categoryOffice.addPreference(pref_office_time_from);
        pref_office_time_to = new SetTimePreference(preferenceScreen.getContext(), "TO", officeTo, "officee");
        pref_office_time_to.setKey("key_office_to");
        categoryOffice.addPreference(pref_office_time_to);

        categoryLunch.setTitle("Lunch Break");
        preferenceScreen.addPreference(categoryLunch);
        pref_lunch_break_from = new SetTimePreference(preferenceScreen.getContext(), "FROM", lunchFrom, "lunch");
        pref_lunch_break_from.setKey("key_lunch_from");
        categoryLunch.addPreference(pref_lunch_break_from);
        pref_lunch_break_to = new SetTimePreference(preferenceScreen.getContext(), "TO", lunchTo, "lunch");
        pref_lunch_break_to.setKey("key_lunch_to");
        categoryLunch.addPreference(pref_lunch_break_to);

    }
}