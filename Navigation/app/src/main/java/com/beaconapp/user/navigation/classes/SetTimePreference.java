package com.beaconapp.user.navigation.classes;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.beaconapp.user.navigation.R;


public class SetTimePreference extends Preference implements TimePickerDialog.OnTimeSetListener {

    Context mContext;
    String mFromTime, mToTime, mCategory, fromTo;
    TextView mFromTimeView, mToTimeView, mTitle;
    RelativeLayout setTime;

    public SetTimePreference(Context context, String fromTime, String toTime, String category) {
        super(context);

        mContext = context;
        mFromTime = fromTime;
        mToTime = toTime;
        mCategory = category;
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        return inflater.inflate(R.layout.preference_set_time, null);
    }

    @Override
    protected void onBindView (@NonNull View view) {
        super.onBindView(view);
        setTime = (RelativeLayout)view.findViewById(R.id.fromToSet);
        setTime.setMinimumHeight(400);
        mFromTimeView = (TextView)view.findViewById(R.id.fromTextView);
        mFromTimeView.setText(mFromTime);
        mFromTimeView.setTextSize(18f);
        mToTimeView = (TextView)view.findViewById(R.id.toTextView);
        mToTimeView.setText(mToTime);
        mToTimeView.setTextSize(18f);
        mTitle = (TextView)view.findViewById(R.id.titleFromTo);
        mTitle.setText(mCategory);
        mTitle.setTextSize(16f);

        mFromTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromTo = "from";
                showTimeDialog();
            }
        });

        mToTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromTo = "to";
                showTimeDialog();
            }
        });
    }
    private void showTimeDialog(){

        new TimePickerDialog(mContext,this, 10, 10, true).show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String hour=""+hourOfDay, min=""+minute, picked_time, category;

        if(hourOfDay<10){hour = "0"+hourOfDay;}
        if(minute<10){min = "0"+minute;}
        picked_time = ""+hour+" : "+min;

        if (mCategory.equals("Office Time")) {
            category = "office";
        }
        else {
            category = "lunch";
        }

        if (fromTo.equals("from")) {
            mFromTimeView.setText(picked_time);
        }
        else {
            mToTimeView.setText(picked_time);
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putString(category+"_"+fromTo+"_time", picked_time);
        sharedPrefEditor.putInt("pref_key_" + category + "_time_" + fromTo + "_hour", hourOfDay);
        sharedPrefEditor.putInt("pref_key_"+category+"_time_"+fromTo+"_minute", minute);
        sharedPrefEditor.apply();
    }
}

