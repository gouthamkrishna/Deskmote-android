package com.beaconapp.user.navigation.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    int flag = 0;
    ReminderFragment reminderFragment;
    DailyChartFragment dailyChartFragment;
    WeeklyChartFragment weeklyChartFragment;
    MonthlyChartFragment monthlyChartFragment;

    public interface ReminderFragment {
        void onDateSelected(int year, int month, int day);
    }

    public interface DailyChartFragment {
        void onDateSelected(int year, int month, int day);
    }

    public interface WeeklyChartFragment {
        void onDateSelected(int year, int month, int day);
    }

    public interface MonthlyChartFragment {
        void onDateSelected(int year, int month, int day);
    }

    public DatePickerFragment(com.beaconapp.user.navigation.fragments.ReminderFragment remFragment) {
        reminderFragment = remFragment;
        flag = 1;
    }


    public DatePickerFragment(com.beaconapp.user.navigation.fragments.DailyChartFragment dailyFragment) {
        dailyChartFragment = dailyFragment;
        flag = 2;
    }

    public DatePickerFragment(com.beaconapp.user.navigation.fragments.WeeklyChartFragment weeklyFragment) {
        weeklyChartFragment = weeklyFragment;
        flag = 3;
    }

    public DatePickerFragment(com.beaconapp.user.navigation.fragments.MonthlyChartFragment monthlyFragment) {
        monthlyChartFragment = monthlyFragment;
        flag = 4;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        if(flag==1) {
            reminderFragment.onDateSelected(year, month, day);
        }
        if(flag==2) {
            dailyChartFragment.onDateSelected(year, month, day);
        }
        if (flag==3) {
            weeklyChartFragment.onDateSelected(year, month, day);
        }
        if (flag==4) {
            monthlyChartFragment.onDateSelected(year, month, day);
        }
    }
}
