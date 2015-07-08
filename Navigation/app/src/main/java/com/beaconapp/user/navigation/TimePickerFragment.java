package com.beaconapp.user.navigation;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    NotificationFragment notificationTimeFragment;

    public interface NotificationFragment {
        public void onTimeSelected(int hour, int minute);
//        public String onSelectionCancelled();
    }

    public TimePickerFragment(com.beaconapp.user.navigation.NotificationFragment notTimeFragment) {
        notificationTimeFragment = notTimeFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        notificationTimeFragment.onTimeSelected(hourOfDay, minute);
    }
}
