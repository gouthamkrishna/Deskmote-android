package com.beaconapp.user.navigation.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.classes.DailyStat;
import com.beaconapp.user.navigation.database.DatabaseHandler;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class DailyChartFragment extends Fragment implements DatePickerFragment.DailyChartFragment {

    DatabaseHandler db;
    private RelativeLayout mainLayout;
    private PieChart mchart;
    private long Ydata[] = {0, 0, 0}, total,stotal,mtotal,htotal;
    long current_timestamp, picked_timestamp;
    TextView datePick, noDataDisplay, deskValue, officeValue, outsideValue;
    ImageView preDay, nextDay;
    DatePickerFragment newDateFragment;
    Date cDate;
    String date, deskTime, officeTime, outsideTime;

    SharedPreferences sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_chart_layout, container, false);

        datePick = (TextView)view.findViewById(R.id.dateView);
        noDataDisplay = (TextView)view.findViewById(R.id.noDataDay);
        preDay = (ImageView)view.findViewById(R.id.previousDate);
        nextDay = (ImageView)view.findViewById(R.id.nextDate);
        deskValue = (TextView)view.findViewById(R.id.desk_value);
        officeValue = (TextView)view.findViewById(R.id.inside_value);
        outsideValue = (TextView)view.findViewById(R.id.outside_value);

        outsideValue.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "digital-7.ttf"));
        outsideValue.setTextSize(20f);
        deskValue.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "digital-7.ttf"));
        deskValue.setTextSize(20f);
        officeValue.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "digital-7.ttf"));
        officeValue.setTextSize(20f);
        noDataDisplay.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "digital-7.ttf"));
        noDataDisplay.setTextSize(25f);
        Calendar calendar = Calendar.getInstance();
        current_timestamp = calendar.getTimeInMillis();
        picked_timestamp = current_timestamp;
        mainLayout = (RelativeLayout)view.findViewById(R.id.mainLayout);
        newDateFragment = new DatePickerFragment(this);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        setView();
        datePickerFunction();
        previousDayFunction();
        nextDayFunction();
        return view;
    }

    private void addData() {

        ArrayList<Entry> Yval = new ArrayList<Entry>();
        for (int i = 0; i < 3; i++) {
            Yval.add(new Entry(Ydata[i], i));
        }
        ArrayList<String> Xval = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            Xval.add("");
        }

        PieDataSet dataset = new PieDataSet(Yval, "");
        dataset.setSliceSpace(0);
        dataset.setSelectionShift(8);
        mchart.getLegend().setEnabled(false);
        dataset.setColors(new int[]{Color.rgb(1, 187, 212), Color.rgb(76, 175, 81), Color.rgb(255, 191, 6)});
        PieData data = new PieData(Xval, dataset);

        data.setDrawValues(false);
        mchart.setData(data);
        mchart.invalidate();
        mchart.setDrawSliceText(false);

    }

    public void datePickerFunction() {

        datePick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                newDateFragment.show(fragmentTransaction, "Date picker");
            }
        });
    }

    public void previousDayFunction(){
        preDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                picked_timestamp = picked_timestamp - 86400000L;
                setView();
            }
        });
    }

    public void nextDayFunction(){
        nextDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                picked_timestamp = picked_timestamp + 86400000L;
                setView();
            }
        });
    }

    public void onDateSelected (int selected_year, int selected_month, int selected_day) {

        cDate = new Date(selected_year-1900,selected_month,selected_day);
        picked_timestamp = cDate.getTime();
        setView();
    }

    private void setView() {

        cDate = new Date(picked_timestamp);
        date = new SimpleDateFormat("MMM dd, yyyy").format(cDate);
        datePick.setText(date);

        if(current_timestamp-picked_timestamp >= 86400000) {
            db = new DatabaseHandler(getActivity());
            DailyStat dailyStat = db.getSelectedDayStat(picked_timestamp);
            if (dailyStat!=null) {
                Ydata[0] = dailyStat.getDesk_time();
                Ydata[1] = dailyStat.getOffice_time();
                Ydata[2] = dailyStat.getOutdoor_time();
            }
            else {
                Ydata[0] = 0;
                Ydata[1] = 0;
                Ydata[2] = 0;
            }
        }
        else if (picked_timestamp-current_timestamp > 60000) {
            Ydata[0] = 0;
            Ydata[1] = 0;
            Ydata[2] = 0;
        }
        else {
            Ydata[0] = sharedPref.getLong(getString(R.string.shared_timer_desk), 0);
            Ydata[1] = sharedPref.getLong(getString(R.string.shared_timer_office), 0);
            Ydata[2] = sharedPref.getLong(getString(R.string.shared_timer_outdoor), 0);
        }

        total = Ydata[0] + Ydata[1] + Ydata[2];
        stotal = total/1000;
        mtotal = stotal/60;
        htotal = mtotal/60;

        if(total!=0) {

            mainLayout.setVisibility(View.VISIBLE);
            noDataDisplay.setVisibility(View.INVISIBLE);

            mchart = new PieChart(getActivity());

            mainLayout.addView(mchart);
            mainLayout.setBackgroundColor(Color.WHITE);

            mchart.setDescription("");
            mchart.setDrawHoleEnabled(true);
            mchart.setHoleColorTransparent(true);
            mchart.setCenterTextSize(25f);
            mchart.setCenterTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "digital-7.ttf"));

            mchart.setCenterText("  TOTAL \n\n " + htotal + ":" + mtotal);
            mchart.setHoleRadius(60);
            addData();

        }
        else{
            mainLayout.setVisibility(View.INVISIBLE);
            noDataDisplay.setVisibility(View.VISIBLE);
        }

        deskTime = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(Ydata[0]),
                TimeUnit.MILLISECONDS.toMinutes(Ydata[0]) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(Ydata[0])));
        officeTime = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(Ydata[1]),
                TimeUnit.MILLISECONDS.toMinutes(Ydata[1]) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(Ydata[1])));
        outsideTime = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(Ydata[2]),
                TimeUnit.MILLISECONDS.toMinutes(Ydata[2]) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(Ydata[2])));


        deskValue.setText(deskTime);
        officeValue.setText(officeTime);
        outsideValue.setText(outsideTime);

        mchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                if (entry == null)
                    return;

            }

            @Override
            public void onNothingSelected() {

            }
        });

    }
}
