package com.beaconapp.user.database;

import java.util.Calendar;

/**
 * Created by user on 23/6/15.
 */
public class Reminder {

    int id;
    String mytag;
    String mytime;
    String mydate;

    public Reminder(){

    }

    public Reminder(int id, String tag){
        this.id = id;
        this.mytag = tag;
        this.mytime = java.text.DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());
        this.mydate = java.text.DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
    }

    public Reminder(String tag){
        this.mytag = tag;
        this.mytime = java.text.DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());
        this.mydate = java.text.DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
    }

    // getting ID
    public int getID(){
        return this.id;
    }

    // setting id
    public void setID(int id){
        this.id = id;
    }

    // getting tag
    public String getTag(){
        return this.mytag;
    }

    // setting tag
    public void setTag(String tag){
        this.mytag = tag;
    }

    // getting date
    public String getDate(){
        return this.mydate;
    }

    // getting time
    public String getTime(){
        return this.mytime;
    }

    // setting date
    public void setDate(String date){
        this.mydate = date;
    }

    // setting time
    public void setTime(String time){
        this.mytime = time;
    }

}
