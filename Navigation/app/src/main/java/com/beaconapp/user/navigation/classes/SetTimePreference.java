package com.beaconapp.user.navigation.classes;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.beaconapp.user.navigation.R;

import java.util.Calendar;

/**
 * Created by user on 13/7/15.
 */
public class SetTimePreference extends Preference implements TimePickerDialog.OnTimeSetListener {

    Context mContext;
    String mFromTo, mTime, mCategory;
    TextView mFromToView, mTimeView;
    RelativeLayout setTime;

    public SetTimePreference(Context context, String fromTo, String time, String category) {
        super(context);

        mContext = context;
        mFromTo = fromTo;
        mTime = time;
        mCategory = category;
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        return inflater.inflate(R.layout.preference_set_time, null);
    }

    @Override
    protected void onBindView (View view) {
        super.onBindView(view);
        setTime = (RelativeLayout)view.findViewById(R.id.setTimings);
        setTime.setMinimumHeight(250);
        mFromToView = (TextView)view.findViewById(R.id.fromToTextView);
        mFromToView.setText(mFromTo);
        mFromToView.setTextSize(18f);
        mTimeView = (TextView)view.findViewById(R.id.fromToTime);
        mTimeView.setText(mTime);
        mTimeView.setTextSize(18f);

        mTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });

    }
    private void showTimeDialog(){

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(mContext,this, hour, minute, true).show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String hour=""+hourOfDay, min=""+minute, picked_time, startEnd;

        if(mFromTo.equals("FROM")) {
            startEnd = "from";
        }
        else {
            startEnd = "to";
        }

        if(hourOfDay<10){hour = "0"+hourOfDay;}
        if(minute<10){min = "0"+minute;}
        picked_time = ""+hour+" : "+min;
        mTimeView.setText(picked_time);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);;
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putString(mCategory+"_"+startEnd+"_time", picked_time);
        sharedPrefEditor.putInt("pref_key_" + mCategory + "_time_" + startEnd + "_hour", hourOfDay);
        sharedPrefEditor.putInt("pref_key_"+mCategory+"_time_"+startEnd+"_minute", minute);
        sharedPrefEditor.apply();
    }
}
