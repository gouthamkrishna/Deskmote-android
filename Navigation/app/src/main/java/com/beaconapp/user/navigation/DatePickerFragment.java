package com.beaconapp.user.navigation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    NotificationFragment notificationFragment;

    public interface NotificationFragment {
        public void onDateSelected(int year, int month, int day);
//        public String onSelectionCancelled();
    }

    public DatePickerFragment(com.beaconapp.user.navigation.NotificationFragment notFragment) {
        notificationFragment = notFragment;
    }

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
        notificationFragment.onDateSelected(year, month, day);
    }
}
