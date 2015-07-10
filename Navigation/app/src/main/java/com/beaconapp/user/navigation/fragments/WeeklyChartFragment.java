package com.beaconapp.user.navigation.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.classes.DailyStat;
import com.beaconapp.user.navigation.database.DatabaseHandler;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2/7/15.
 */
public class WeeklyChartFragment extends Fragment implements DatePickerFragment.WeeklyChartFragment {

    DatabaseHandler db;
    HorizontalBarChart chart;

    List<DailyStat> weekly;
    TextView noDataDisplay, datePicker;
    long timestamp, picked_timestamp;
    DatePickerFragment newDateFragment;
    Date sDate,eDate,cDate;
    String startDate, endDate;
    ImageView preWeek, nextWeek;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly_chart_layout, container, false);

        Calendar calendar = Calendar.getInstance();
        timestamp = calendar.getTimeInMillis();
        picked_timestamp = timestamp;
        newDateFragment = new DatePickerFragment(this);
        datePicker = (TextView)view.findViewById(R.id.weekView);
        db = new DatabaseHandler(getActivity());
        noDataDisplay = (TextView)view.findViewById(R.id.noDataWeek);
        noDataDisplay.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "digital-7.ttf"));
        noDataDisplay.setTextSize(25f);
        chart = (HorizontalBarChart)view.findViewById(R.id.weeklybchart);
        preWeek = (ImageView)view.findViewById(R.id.previousWeek);
        nextWeek = (ImageView)view.findViewById(R.id.nextWeek);
        weekly = new ArrayList<>();

        setView();
        datePickerFunction();
        previousWeekFunction();
        nextWeekFunction();
        return view;
    }

    public void setView(){
        weekly = db.getWeeklyStat(picked_timestamp);

        ArrayList<BarEntry> Yvalue1 = new ArrayList<>();
        ArrayList<BarEntry> Yvalue2 = new ArrayList<>();
        ArrayList<BarEntry> Yvalue3 = new ArrayList<>();

        sDate = new Date(((picked_timestamp/604800000L)*604800000L)-19799000L);
        eDate = new Date(sDate.getTime()+604798000L);
        startDate = new SimpleDateFormat("yyyy.MM.dd").format(sDate);
        endDate = new SimpleDateFormat("yyyy.MM.dd").format(eDate);
        datePicker.setText(startDate + " - " + endDate);

        if(weekly.size()!=0) {

            chart.setVisibility(View.VISIBLE);
            noDataDisplay.setVisibility(View.INVISIBLE);
            for (int i = 0; i < weekly.size(); i++) {
                Yvalue1.add(new BarEntry((float) weekly.get(i).getDesk_time(), i));
                Yvalue2.add(new BarEntry((float) weekly.get(i).getOffice_time(), i));
                Yvalue3.add(new BarEntry((float) weekly.get(i).getOutdoor_time(), i));
            }
            BarDataSet dataset1 = new BarDataSet(Yvalue1, "At Desk");
            dataset1.setColor(Color.rgb(255, 191, 6));

            BarDataSet dataset2 = new BarDataSet(Yvalue2, "At Office");
            dataset2.setColor(Color.rgb(76,175,81));

            BarDataSet dataset3 = new BarDataSet(Yvalue3, "Outside Office");
            dataset3.setColor(Color.rgb(1,187,212));

            ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
            dataSets.add(dataset1);
            dataSets.add(dataset2);
            dataSets.add(dataset3);

            ArrayList<String> labels = new ArrayList<String>();
            labels.add("Mon");
            labels.add("Tue");
            labels.add("Wed");
            labels.add("Thu");
            labels.add("Fri");
            labels.add("Sat");
            labels.add("Sun");

            chart.animateXY(3000, 3000);

            BarData data = new BarData(labels,dataSets);
            chart.setData(data);
            data.setGroupSpace(200);
            chart.setDescription("");
        }
        else{
            chart.setVisibility(View.INVISIBLE);
            noDataDisplay.setVisibility(View.VISIBLE);
        }
    }

    public void datePickerFunction() {

        datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                newDateFragment.show(fragmentTransaction, "Date picker");
            }
        });
    }

    public void previousWeekFunction(){
        preWeek.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                picked_timestamp = picked_timestamp - 604800000L;
                setView();
            }
        });
    }

    public void nextWeekFunction(){
        nextWeek.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                picked_timestamp = picked_timestamp + 604800000L;
                setView();
            }
        });
    }

    @Override
    public void onDateSelected(int selected_year, int selected_month, int selected_day) {

        cDate = new Date(selected_year-1900,selected_month,selected_day);
        picked_timestamp = cDate.getTime();
        setView();
    }
}