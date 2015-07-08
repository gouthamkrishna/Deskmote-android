package com.beaconapp.user.navigation;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class NotificationFragment extends Fragment implements AdapterView.OnItemClickListener, DatePickerFragment.NotificationFragment, TimePickerFragment.NotificationFragment {

    public PendingIntent pendingIntent;
    public static final String TAG = "com.beaconapp.user.reminder3.TAG";
    Intent alarmIntent;
    AlarmManager manager;

    private ArrayList<Reminder> arraylist;
    ReminderDatabaseHandler db_reminder;
    Reminder temp;

    public int year, month, day, hour, min;
    private  long tstamp;
    String time = "", date = "";
    TimePickerFragment newTimeFragment;
    DatePickerFragment newDateFragment;

    ListViewAdapter adapter;
    TextView tvDisplayDate, tvDisplayTime;
    ImageView btnChangeDate, btnChangeDate1, btnChangeDate2;
    EditText text;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	
	((MainActivity) getActivity())
                .setActionBarTitle("Reminders");
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_notification, null);

        db_reminder = new ReminderDatabaseHandler(getActivity());
        tvDisplayDate = (TextView)root.findViewById(R.id.textView3);
        tvDisplayTime = (TextView)root.findViewById(R.id.textView2);
        listView = (ListView) root.findViewById(R.id.list);
        btnChangeDate1 = (ImageView) root.findViewById(R.id.button2);
        btnChangeDate = (ImageView) root.findViewById(R.id.button1);
        btnChangeDate2 = (ImageView) root.findViewById(R.id.button3);
        text = (EditText)root.findViewById(R.id.editText);
        listView = (ListView) root.findViewById(R.id.list);
        newDateFragment = new DatePickerFragment(this);
        newTimeFragment = new TimePickerFragment(this);

        setCurrentDateOnView();

        addListenerOnButton();
        addListenerOnButton1();
        addListenerOnButton2();
        return root;
    }

    public void setCurrentDateOnView() {

        Date cDate = new Date();
        Calendar calendar = Calendar.getInstance();
        date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        time = new SimpleDateFormat("HH:mm").format(cDate);
        tstamp = cDate.getTime();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

        tvDisplayDate.setText(date);
        tvDisplayTime.setText(time);
        //Reading all Reminders

        arraylist = new ArrayList<Reminder>();
        List<Reminder> reminders = db_reminder.getAllReminders();
        int listcounter=0;
        listView.setOnItemClickListener(this);

        while (listcounter<reminders.size()) {
            Reminder rmndr = reminders.get(listcounter);

            long ttstamp = rmndr.getTstamp();
            if(ttstamp>=tstamp){
                arraylist.add(rmndr);
            }
            else {
                db_reminder.deleteReminder(rmndr);
            }
            listcounter++;
        }
        adapter = new ListViewAdapter(getActivity(), arraylist);
        listView.setAdapter(adapter);
    }

    public void resetCurrentDateTime(){

        Date cDate = new Date();
        date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        time = new SimpleDateFormat("HH:mm").format(cDate);
        tstamp = cDate.getTime();

        tvDisplayDate.setText(date);
        tvDisplayTime.setText(time);
    }

    public void addListenerOnButton1() {

        btnChangeDate1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                newTimeFragment.show(fragmentTransaction, "Time picker");
            }
        });
    }
    public void onTimeSelected (int selected_hour, int selected_minute) {
        hour = selected_hour;
        min = selected_minute;

        Date cDate = new Date(year-1900,month,day,hour,min);
        time = new SimpleDateFormat("HH:mm").format(cDate);
        tstamp = cDate.getTime();

        tvDisplayTime.setText(time);
    }

    public void addListenerOnButton() {

        btnChangeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                newDateFragment.show(fragmentTransaction, "Date picker");
            }
        });
    }

    public void onDateSelected (int selected_year, int selected_month, int selected_day) {
        year = selected_year;
        month = selected_month;
        day = selected_day;

        Date cDate = new Date(year-1900,month,day,hour,min);
        date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        tstamp = cDate.getTime();
        tvDisplayDate.setText(date);
    }

    public void addListenerOnButton2() {

        btnChangeDate2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Date cDate = new Date();
                long ttstamp = cDate.getTime();

                String tag = text.getText().toString();
                text.setText("");
                if(tag.equals("")) {
                    Toast.makeText(getActivity(), "Title Empty !!", Toast.LENGTH_SHORT).show();
                }
                else if(tstamp<=ttstamp) {
                    Toast.makeText(getActivity(), "Invalid Date or Time !!", Toast.LENGTH_SHORT).show();
                }
                else {
                    db_reminder.addReminder(new Reminder(tstamp, tag, date, time));
                    temp = db_reminder.getReminder(tstamp);
                    Toast.makeText(getActivity(), "Alarm Set !!", Toast.LENGTH_SHORT).show();

                    alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
                    alarmIntent.putExtra(TAG, tag);
                    manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    pendingIntent = PendingIntent.getBroadcast(getActivity(), temp.getID(), alarmIntent, 0);
                    manager.setExact(AlarmManager.RTC_WAKEUP, tstamp, pendingIntent);
                }

                List<Reminder> reminders = db_reminder.getAllReminders();

                int listcounter=0;
                arraylist = new ArrayList<Reminder>();

                while (listcounter<reminders.size()) {
                    Reminder rmndr = reminders.get(listcounter);

                    long tttstamp = rmndr.getTstamp();
                    if(tttstamp>=ttstamp){
                        arraylist.add(rmndr);
                    }
                    listcounter++;
                }
                adapter = new ListViewAdapter(getActivity(), arraylist);
                //Binds the Adapter to the ListView
                listView.setAdapter(adapter);
                resetCurrentDateTime();
            }
        });
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        temp = new Reminder(arraylist.get(position).getID());
        db_reminder.deleteReminder(temp);
        alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        alarmIntent.putExtra(TAG, temp.getTag());
        pendingIntent = PendingIntent.getBroadcast(getActivity(), temp.getID(), alarmIntent, 0);
        manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(getActivity(), "Alarm Canceled", Toast.LENGTH_SHORT).show();
        setCurrentDateOnView();
    }
}

