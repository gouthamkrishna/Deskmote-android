package com.beaconapp.user.navigation.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beaconapp.user.navigation.receivers.AlarmReceiver;
import com.beaconapp.user.navigation.activities.MainActivity;
import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.database.ReminderDatabaseHandler;
import com.beaconapp.user.navigation.classes.ListViewAdapter;
import com.beaconapp.user.navigation.classes.Reminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class NotificationFragment extends Fragment implements AdapterView.OnItemClickListener, DatePickerFragment.NotificationFragment, TimePickerFragment.NotificationFragment {

    public static final String TAG = "com.beaconapp.user.deskmote.TAG";
    public PendingIntent pendingIntent;
    private ArrayList<Reminder> arraylist;
    public int year, month, day, hour, minute, listcounter = 0;
    private long timestamp, reminderTimestamp, temporaryTimestamp;

    Intent alarmIntent;
    AlarmManager alarmManager;
    ReminderDatabaseHandler db_reminder;
    Reminder temporaryReminder, reminder;
    String displayTime = "", displayDate = "";
    TimePickerFragment newTimeFragment;
    DatePickerFragment newDateFragment;
    ListViewAdapter listViewAdapter;
    TextView tvDisplayDate, tvDisplayTime;
    ImageView datePickerIcon, timePickerIcon, saveReminderIcon;
    EditText reminderTagLine;
    ListView listView;
    Date currentDate, temporaryDate;
    Calendar calendar;
    List<Reminder> reminderList;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String reminderDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Reminders");
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_notification, null);

        db_reminder = new ReminderDatabaseHandler(getActivity());
        tvDisplayDate = (TextView)root.findViewById(R.id.reminderDateView);
        tvDisplayTime = (TextView)root.findViewById(R.id.reminderTimeView);
        listView = (ListView) root.findViewById(R.id.reminderList);
        timePickerIcon = (ImageView) root.findViewById(R.id.timePicker);
        datePickerIcon = (ImageView) root.findViewById(R.id.datePicker);
        saveReminderIcon = (ImageView) root.findViewById(R.id.saveReminder);
        reminderTagLine = (EditText)root.findViewById(R.id.tagLine);
        newDateFragment = new DatePickerFragment(this);
        newTimeFragment = new TimePickerFragment(this);

        setCurrentDateOnView();
        addListenerOntimePickerIcon();
        addListenerOndatePickerIcon();
        addListenerOnsaveReminderIcon();
        listView.setOnItemClickListener(this);

        return root;
    }

    public void setCurrentDateOnView() {

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

        arraylist = new ArrayList<Reminder>();
        reminderList = db_reminder.getAllReminders();
        listcounter=0;

        while (listcounter<reminderList.size()) {

            reminder = reminderList.get(listcounter);
            reminderTimestamp = reminder.getTstamp();

            if(reminderTimestamp>=timestamp){

                arraylist.add(reminder);
            }
            else {

                db_reminder.deleteReminder(reminder);
            }

            listcounter++;
        }

        listViewAdapter = new ListViewAdapter(getActivity(), arraylist);
        listView.setAdapter(listViewAdapter);
    }

    public void resetCurrentDateTime(){

        currentDate = new Date();
        displayDate = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
        displayTime = new SimpleDateFormat("HH:mm").format(currentDate);
        timestamp = currentDate.getTime();

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

    public void onTimeSelected (int selected_hour, int selected_minute) {

        hour = selected_hour;
        minute = selected_minute;
        currentDate = new Date(year-1900,month,day,hour,minute);
        displayTime = new SimpleDateFormat("HH:mm").format(currentDate);
        timestamp = currentDate.getTime();
        tvDisplayTime.setText(displayTime);
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

    public void onDateSelected (int selected_year, int selected_month, int selected_day) {
        year = selected_year;
        month = selected_month;
        day = selected_day;

        currentDate = new Date(year-1900,month,day,hour,minute);
        displayDate = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
        timestamp = currentDate.getTime();
        tvDisplayDate.setText(displayDate);
    }

    public void addListenerOnsaveReminderIcon() {

        saveReminderIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                temporaryDate = new Date();
                temporaryTimestamp = temporaryDate.getTime();

                reminderDescription = reminderTagLine.getText().toString();
                reminderTagLine.setText("");

                if(reminderDescription.equals("")) {

                    Toast.makeText(getActivity(), "Title Empty !!", Toast.LENGTH_SHORT).show();
                }
                else if(timestamp <= temporaryTimestamp) {

                    Toast.makeText(getActivity(), "Invalid Date or Time !!", Toast.LENGTH_SHORT).show();
                }
                else {

                    db_reminder.addReminder(new Reminder(timestamp, reminderDescription, displayDate, displayTime));
                    temporaryReminder = db_reminder.getReminder(timestamp);
                    Toast.makeText(getActivity(), "Alarm Set !!", Toast.LENGTH_SHORT).show();

                    alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
                    alarmIntent.putExtra(TAG, reminderDescription);
                    alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    pendingIntent = PendingIntent.getBroadcast(getActivity(), temporaryReminder.getID(), alarmIntent, 0);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
                }

                arraylist.add(temporaryReminder);
                listViewAdapter.notifyDataSetChanged();
                listView.setAdapter(listViewAdapter);
                resetCurrentDateTime();
            }
        });
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        temporaryReminder = arraylist.get(position);
        db_reminder.deleteReminder(temporaryReminder);
        alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        alarmIntent.putExtra(TAG, temporaryReminder.getTag());
        pendingIntent = PendingIntent.getBroadcast(getActivity(), temporaryReminder.getID(), alarmIntent, 0);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(getActivity(), "Alarm Canceled", Toast.LENGTH_SHORT).show();
        arraylist.remove(position);
        listViewAdapter.notifyDataSetChanged();
        listView.setAdapter(listViewAdapter);
        resetCurrentDateTime();
    }
}

