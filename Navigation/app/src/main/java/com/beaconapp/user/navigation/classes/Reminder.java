package com.beaconapp.user.navigation.classes;


public class Reminder {

    int id;
    long tstamp;
    String mytag;
    String mytime;
    String mydate;

    public Reminder(){

    }

    public Reminder(int id,long tstamp, String tag, String date, String time){
        this.id = id;
        this.mytag = tag;
        this.mytime = time;
        this.mydate = date;
        this.tstamp = tstamp;
    }

    public Reminder(long tstamp, String tag, String date, String time){
        this.mytag = tag;
        this.mytime = time;
        this.mydate = date;
        this.tstamp = tstamp;
    }

/*
    public Reminder(int id){
        this.id = id;
    }
*/

    // getting tstamp
    public long getTstamp(){
        return this.tstamp;
    }

    // setting tstamp
    public void setTstamp(long tstamp){
        this.tstamp = tstamp;
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


