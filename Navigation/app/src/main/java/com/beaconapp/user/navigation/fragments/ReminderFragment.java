package com.beaconapp.user.navigation.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.activities.MainActivity;
import com.beaconapp.user.navigation.classes.Reminder;
import com.beaconapp.user.navigation.database.ReminderDatabaseHandler;
import com.beaconapp.user.navigation.receivers.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ReminderFragment extends DialogFragment implements DatePickerFragment.ReminderFragment, TimePickerFragment.ReminderFragment {

    public static final String TAG = "com.beaconapp.user.deskmote.TAG";
    public PendingIntent pendingIntent;
    public int year, month, day, hour, minute, reminderID;
    private long timestamp, temporaryTimestamp;
    boolean resminderSet = false, isUpdating = false;

    Intent alarmIntent;
    AlarmManager alarmManager;
    Reminder temporaryReminder;
    ReminderDatabaseHandler db_reminder;
    TextView datePickerIcon, timePickerIcon, saveReminderIcon, cancelReminderIcon;
    String displayTime = "", displayDate = "";
    TimePickerFragment newTimeFragment;
    DatePickerFragment newDateFragment;
    EditText reminderTagLine;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String reminderDescription = "";
    Date currentDate, temporaryDate;
    Calendar calendar;

    public ReminderFragment (){
        currentDate = new Date();
        this.timestamp = currentDate.getTime();
        displayDate = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
        displayTime = new SimpleDateFormat("HH:mm").format(currentDate);
    }

    public ReminderFragment (String reminderDescription, long timestamp, String displayTime, String displayDate, int reminderID) {
        this.timestamp = timestamp;
        this.reminderDescription = reminderDescription;
        this.displayDate = displayDate;
        this.displayTime = displayTime;
        this.isUpdating = true;
        this.reminderID = reminderID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reminder_dialog, container, false);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        db_reminder = new ReminderDatabaseHandler(getActivity());
        timePickerIcon = (TextView) rootView.findViewById(R.id.reminderTimeView);
        datePickerIcon = (TextView) rootView.findViewById(R.id.reminderDateView);
        saveReminderIcon = (TextView) rootView.findViewById(R.id.saveReminder);
        cancelReminderIcon = (TextView) rootView.findViewById(R.id.cancelReminder);
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

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        datePickerIcon.setText(displayDate);
        timePickerIcon.setText(displayTime);
        if (!reminderDescription.equals("")) {
            reminderTagLine.setText(reminderDescription);
        }
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
        datePickerIcon.setText(displayDate);
    }

    @Override
    public void onTimeSelected(int selected_hour, int selected_minute) {

        hour = selected_hour;
        minute = selected_minute;
        currentDate = new Date(year-1900,month,day,hour,minute);
        displayTime = new SimpleDateFormat("HH:mm").format(currentDate);
        timestamp = currentDate.getTime();
        timePickerIcon.setText(displayTime);
    }

    public void addListenerOnsaveReminderIcon() {

        saveReminderIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                temporaryDate = new Date();
                temporaryTimestamp = temporaryDate.getTime();

                reminderDescription = reminderTagLine.getText().toString();

                if (reminderDescription.equals("")) {
                    reminderTagLine.setError("Give a Title !!");
                    Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                    reminderTagLine.startAnimation(shake);
                } else if (timestamp <= temporaryTimestamp) {
                    Toast.makeText(getActivity(), "Change Date or Time !!", Toast.LENGTH_SHORT).show();
                } else {
                    addReminder();
                    Toast.makeText(getActivity(), "Reminder Added !!", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });

        cancelReminderIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isUpdating && !resminderSet) {
                    addReminder();
                }
                dismiss();
            }
        });
    }

    public void addReminder() {

        if (isUpdating){
            temporaryReminder = new Reminder(reminderID, timestamp, reminderDescription, displayDate, displayTime);
            db_reminder.updateReminder(temporaryReminder);
        }
        else {
            db_reminder.addReminder(new Reminder(timestamp, reminderDescription, displayDate, displayTime));
            temporaryReminder = db_reminder.getReminder(timestamp, reminderDescription);
        }

        alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        alarmIntent.putExtra(TAG, reminderDescription);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), temporaryReminder.getID(), alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
        resminderSet = true;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        if (isUpdating && !resminderSet) {
            addReminder();
        }
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        if (resminderSet && (MainActivity.position==3)) {
            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, new NotificationFragment()).commit();
        }
        super.onDismiss(dialog);
    }
}
