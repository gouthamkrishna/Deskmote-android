package com.beaconapp.user.navigation;



public class DailyStat {
    int id;
    long tstamp;
    long desk_time;
    long office_time;
    long outdoor_time;

    public DailyStat(){

    }

    public DailyStat(int id, long tstamp, long desktime, long outdoortime, long officetime){
        this.id = id;
        this.desk_time = desktime;
        this.office_time = officetime;
        this.outdoor_time = outdoortime;
        this.tstamp = tstamp;
    }

    public DailyStat(long tstamp, long desktime, long outdoortime, long officetime){
        this.desk_time = desktime;
        this.office_time = officetime;
        this.outdoor_time = outdoortime;
        this.tstamp = tstamp;
    }

    public DailyStat(int id){
        this.id = id;
    }

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

    // getting desktime
    public long getDesk_time(){
        return this.desk_time;
    }

    // setting desktime
    public void setDesk_time(long desktime){
        this.desk_time = desktime;
    }

    // getting outdoortime
    public long getOutdoor_time(){
        return this.outdoor_time;
    }

    // getting time
    public long getOffice_time(){
        return this.office_time;
    }

    // setting outdoortime
    public void setOutdoor_time(long outdoortime){
        this.outdoor_time = outdoortime;
    }

    // setting time
    public void setOffice_time(long officetime){
        this.office_time = officetime;
    }

}

