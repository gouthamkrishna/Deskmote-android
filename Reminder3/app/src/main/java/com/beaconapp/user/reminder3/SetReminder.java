package com.beaconapp.user.reminder3;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class SetReminder extends ActionBarActivity implements AdapterView.OnItemClickListener {

    public PendingIntent pendingIntent;
    public static final String TAG = "com.beaconapp.user.reminder3.TAG";
    Intent alarmIntent;
    AlarmManager manager;

    private ArrayList<Reminder> arraylist;
    DatabaseHandler db = new DatabaseHandler(this);
    Reminder temp;

    private int year, month, day, hour, min;
    private  long tstamp;
    String time = "", date = "";

    static final int DATE_DIALOG_ID = 99;
    static final int DATE_DIALOG_ID1 = 999;

    ListViewAdapter adapter;
    TextView tvDisplayDate, tvDisplayTime;
    Button btnChangeDate;
    EditText text;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);

        setCurrentDateOnView();

        addListenerOnButton();
        addListenerOnButton1();
        addListenerOnButton2();
    }


   public void setCurrentDateOnView() {

       tvDisplayDate = (TextView)findViewById(R.id.textView2);
       tvDisplayTime = (TextView)findViewById(R.id.textView3);

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
       List<Reminder> reminders = db.getAllReminders();
       int listcounter=0;
       listView = (ListView) findViewById(R.id.list);
       listView.setOnItemClickListener(this);

       while (listcounter<reminders.size()) {
           Reminder rmndr = reminders.get(listcounter);

           long ttstamp = rmndr.getTstamp();
           if(ttstamp>=tstamp){
               arraylist.add(rmndr);
           }
           else {
               db.deleteReminder(rmndr);
           }
           listcounter++;
       }
       adapter = new ListViewAdapter(this, arraylist);
       listView.setAdapter(adapter);
   }

    public void resetCurrentDateTime(){

        tvDisplayDate = (TextView)findViewById(R.id.textView2);
        tvDisplayTime = (TextView)findViewById(R.id.textView3);

        Date cDate = new Date();
        date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        time = new SimpleDateFormat("HH:mm").format(cDate);
        tstamp = cDate.getTime();

        // set current date into textview
        tvDisplayDate.setText(date);
        // set current date into textview
        tvDisplayTime.setText(time);
    }


    public void addListenerOnButton1() {

        btnChangeDate = (Button) findViewById(R.id.button2);

        btnChangeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID1);

            }
        });
    }

    public void addListenerOnButton() {

        btnChangeDate = (Button) findViewById(R.id.button);

        btnChangeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);

            }
        });
    }

    public void addListenerOnButton2() {

        btnChangeDate = (Button) findViewById(R.id.button3);

        btnChangeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Date cDate = new Date();
                long ttstamp = cDate.getTime();

                text = (EditText)findViewById(R.id.editText);
                String tag = text.getText().toString();
                text.setText("");
                if(tag.equals("")) {
                    Toast.makeText(SetReminder.this, "Title Empty !!", Toast.LENGTH_SHORT).show();
                }
                else if(tstamp<=ttstamp) {
                    Toast.makeText(SetReminder.this, "Invalid Date or Time !!", Toast.LENGTH_SHORT).show();
                }
                else {
                    db.addReminder(new Reminder(tstamp, tag, date, time));
                    temp = db.getReminder(tstamp);
                    Toast.makeText(SetReminder.this, "Alarm Set !!", Toast.LENGTH_SHORT).show();

                    alarmIntent = new Intent(SetReminder.this, AlarmReceiver.class);
                    alarmIntent.putExtra(TAG, tag);
                    manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    pendingIntent = PendingIntent.getBroadcast(SetReminder.this, temp.getID(), alarmIntent, 0);
                    manager.setExact(AlarmManager.RTC_WAKEUP, tstamp, pendingIntent);
                }

                List<Reminder> reminders = db.getAllReminders();

                int listcounter=0;
                listView = (ListView) findViewById(R.id.list);
                arraylist = new ArrayList<Reminder>();

                while (listcounter<reminders.size()) {
                    Reminder rmndr = reminders.get(listcounter);

                    long tttstamp = rmndr.getTstamp();
                    if(tttstamp>=ttstamp){
                        arraylist.add(rmndr);
                    }
                    listcounter++;
                }
                adapter = new ListViewAdapter(SetReminder.this, arraylist);
                //Binds the Adapter to the ListView
                listView.setAdapter(adapter);
                resetCurrentDateTime();
            }
        });
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month, day);

            case DATE_DIALOG_ID1:
                return new TimePickerDialog(this, timePickerListener,
                        hour, min,false);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            Date cDate = new Date(year-1900,month-1,day);
            date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

            tvDisplayDate = (TextView)findViewById(R.id.textView2);

            // set selected date into textview
            tvDisplayDate.setText(date);
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener
            = new TimePickerDialog.OnTimeSetListener() {

        // when dialog box is closed, below method will be called.
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            min = selectedMinute;

            Date cDate = new Date(year-1900,month,day,hour,min);
            time = new SimpleDateFormat("HH:mm").format(cDate);
            tstamp = cDate.getTime();

            tvDisplayTime = (TextView)findViewById(R.id.textView3);
            // set selected date into textview
            // set current time into textview
            tvDisplayTime.setText(time);

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        temp = new Reminder(arraylist.get(position).getID());
        db.deleteReminder(temp);
        alarmIntent = new Intent(SetReminder.this, AlarmReceiver.class);
        alarmIntent.putExtra(TAG, temp.getTag());
        pendingIntent = PendingIntent.getBroadcast(SetReminder.this, temp.getID(), alarmIntent, 0);
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
        setCurrentDateOnView();
    }
}
