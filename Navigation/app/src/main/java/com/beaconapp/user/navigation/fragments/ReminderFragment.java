package com.beaconapp.user.navigation.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.classes.Reminder;
import com.beaconapp.user.navigation.database.ReminderDatabaseHandler;
import com.beaconapp.user.navigation.receivers.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 13/7/15.
 */
public class ReminderFragment extends DialogFragment implements DatePickerFragment.ReminderFragment, TimePickerFragment.ReminderFragment {

    public static final String TAG = "com.beaconapp.user.deskmote.TAG";
    public PendingIntent pendingIntent;
    public int year, month, day, hour, minute;
    private long timestamp, temporaryTimestamp;
    boolean flag = false;

    Intent alarmIntent;
    AlarmManager alarmManager;
    Reminder temporaryReminder;
    ReminderDatabaseHandler db_reminder;
    TextView tvDisplayDate, tvDisplayTime;
    ImageView datePickerIcon, timePickerIcon, saveReminderIcon;
    String displayTime = "", displayDate = "";
    TimePickerFragment newTimeFragment;
    DatePickerFragment newDateFragment;
    EditText reminderTagLine;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String reminderDescription;
    Date currentDate, temporaryDate;
    Calendar calendar;

    public  ReminderFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reminder_dialog, container, false);
        getDialog().setTitle("Add Reminder");

        db_reminder = new ReminderDatabaseHandler(getActivity());
        tvDisplayDate = (TextView)rootView.findViewById(R.id.reminderDateView);
        tvDisplayTime = (TextView)rootView.findViewById(R.id.reminderTimeView);
        timePickerIcon = (ImageView) rootView.findViewById(R.id.timePicker);
        datePickerIcon = (ImageView) rootView.findViewById(R.id.datePicker);
        saveReminderIcon = (ImageView) rootView.findViewById(R.id.saveReminder);
        reminderTagLine = (EditText)rootView.findViewById(R.id.tagLine);
        newDateFragment = new DatePickerFragment(this);
        newTimeFragment = new TimePickerFragment(this);

        setCurrentDateOnView();
        addListenerOntimePickerIcon();
        addListenerOndatePickerIcon();
        addListenerOnsaveReminderIcon();
        return rootView;
    }

    private void setCurrentDateOnView() {

        currentDate = new Date();
        displayDate = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
        displayTime = new SimpleDateFormat("HH:mm").format(currentDate);
        timestamp = currentDate.getTime();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        tvDisplayDate.setText(displayDate);
        tvDisplayTime.setText(displayTime);
    }

    public void addListenerOntimePickerIcon() {

        timePickerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                newTimeFragment.show(fragmentTransaction, "Time picker");
            }
        });
    }

    public void addListenerOndatePickerIcon() {

        datePickerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                newDateFragment.show(fragmentTransaction, "Date picker");
            }
        });
    }

    @Override
    public void onDateSelected(int selected_year, int selected_month, int selected_day) {

        year = selected_year;
        month = selected_month;
        day = selected_day;

        currentDate = new Date(year-1900,month,day,hour,minute);
        displayDate = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
        timestamp = currentDate.getTime();
        tvDisplayDate.setText(displayDate);
    }

    @Override
    public void onTimeSelected(int selected_hour, int selected_minute) {

        hour = selected_hour;
        minute = selected_minute;
        currentDate = new Date(year-1900,month,day,hour,minute);
        displayTime = new SimpleDateFormat("HH:mm").format(currentDate);
        timestamp = currentDate.getTime();
        tvDisplayTime.setText(displayTime);
    }

    public void addListenerOnsaveReminderIcon() {

        saveReminderIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                temporaryDate = new Date();
                temporaryTimestamp = temporaryDate.getTime();

                reminderDescription = reminderTagLine.getText().toString();
                reminderTagLine.setText("");

                if (reminderDescription.equals("")) {

                    Toast.makeText(getActivity(), "Title Empty !!", Toast.LENGTH_SHORT).show();
                } else if (timestamp <= temporaryTimestamp) {

                    Toast.makeText(getActivity(), "Invalid Date or Time !!", Toast.LENGTH_SHORT).show();
                } else {

                    db_reminder.addReminder(new Reminder(timestamp, reminderDescription, displayDate, displayTime));
                    temporaryReminder = db_reminder.getReminder(timestamp);
                    Toast.makeText(getActivity(), "Alarm Set !!", Toast.LENGTH_SHORT).show();

                    alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
                    alarmIntent.putExtra(TAG, reminderDescription);
                    alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    pendingIntent = PendingIntent.getBroadcast(getActivity(), temporaryReminder.getID(), alarmIntent, 0);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
                    flag = true;
                    dismiss();
                }
            }
        });
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        if (flag) {
            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new NotificationFragment());
            fragmentTransaction.commit();
        }
        super.onDismiss(dialog);
    }
}
