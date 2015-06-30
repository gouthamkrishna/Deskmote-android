package com.beaconapp.user.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void remind(View view) {

        EditText text = new EditText(this);
        text = (EditText)findViewById(R.id.editText);
        String tag = text.getText().toString();

        // Inserting Reminders
        db.addReminder(new Reminder(tag));

    }

    public void print(View view) {

	    int listcounter=0;
        TextView text = (TextView)findViewById(R.id.textView);
        text.setText("initial");
        // Reading all Reminders
        List<Reminder> reminders = db.getAllReminders();

        while(listcounter<reminders.size())
        {
            Reminder rmndr = reminders.get(listcounter);
            String log = "Id: "+rmndr.getID()+" ,Date: "+rmndr.getDate()+" ,Time: "+rmndr.getTime()+" ,Tag: " + rmndr.getTag();
	        text.append(log);
            listcounter++;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}