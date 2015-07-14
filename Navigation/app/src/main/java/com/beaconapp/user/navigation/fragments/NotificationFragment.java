package com.beaconapp.user.navigation.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.activities.MainActivity;
import com.beaconapp.user.navigation.classes.ListViewAdapter;
import com.beaconapp.user.navigation.classes.Reminder;
import com.beaconapp.user.navigation.database.ReminderDatabaseHandler;
import com.beaconapp.user.navigation.receivers.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class NotificationFragment extends Fragment {

    public static final String TAG = "com.beaconapp.user.deskmote.TAG";
    public PendingIntent pendingIntent;
    private ArrayList<Reminder> arraylist;
    public int listcounter = 0;
    private long timestamp, reminderTimestamp;

    Intent alarmIntent;
    AlarmManager alarmManager;
    ReminderDatabaseHandler db_reminder;
    Reminder temporaryReminder, reminder;
    ListViewAdapter listViewAdapter;
    com.baoyz.swipemenulistview.SwipeMenuListView listView;
    List<Reminder> reminderList;
    Calendar calendar;
    TextView upcoming;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Reminders");
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_notification, null);

        db_reminder = new ReminderDatabaseHandler(getActivity());
        listView = (com.baoyz.swipemenulistview.SwipeMenuListView) root.findViewById(R.id.reminderList);
        upcoming = (TextView)root.findViewById(R.id.upcoming);
        setView();

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                deleteItem.setWidth(350);
                deleteItem.setIcon(R.drawable.delete);
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
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
                        break;
                }
                return false;
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }

    public void setView() {

        calendar = Calendar.getInstance();
        timestamp = calendar.getTimeInMillis();

        arraylist = new ArrayList<Reminder>();
        reminderList = db_reminder.getAllReminders();
        listcounter=0;

        if (listcounter<reminderList.size()){
           upcoming.setVisibility(View.VISIBLE);
        }

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
}

