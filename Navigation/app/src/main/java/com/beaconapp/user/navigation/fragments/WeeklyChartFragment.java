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
import android.widget.TextView;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.classes.DailyStat;
import com.beaconapp.user.navigation.classes.YvalueFormatter;
import com.beaconapp.user.navigation.database.DatabaseHandler;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeeklyChartFragment extends Fragment implements DatePickerFragment.WeeklyChartFragment {

    DatabaseHandler db;
    HorizontalBarChart chart;

    List<DailyStat> weekly;
    TextView noDataDisplay, datePicker;
    long picked_timestamp;
    DatePickerFragment newDateFragment;
    String startDate, endDate;
    ImageView preWeek, nextWeek;
    String daysOfWeek[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    BarData data;
    ArrayList<BarDataSet> dataSets;
    BarDataSet dataset1, dataset2, dataset3;
    ArrayList<BarEntry> Yvalue1, Yvalue2, Yvalue3;
    ArrayList<String> labels;
    YvalueFormatter yvalueFormatter;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly_chart_layout, container, false);
        db = new DatabaseHandler(getActivity());
        newDateFragment = new DatePickerFragment(this);
        weekly = new ArrayList<>();
        chart = (HorizontalBarChart)view.findViewById(R.id.weeklybchart);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        picked_timestamp = Calendar.getInstance().getTimeInMillis();
        datePicker = (TextView)view.findViewById(R.id.weekView);
        preWeek = (ImageView)view.findViewById(R.id.previousWeek);
        nextWeek = (ImageView)view.findViewById(R.id.nextWeek);
        yvalueFormatter = new YvalueFormatter();

        noDataDisplay = (TextView)view.findViewById(R.id.noDataWeek);
        noDataDisplay.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "digital-7.ttf"));
        noDataDisplay.setTextSize(25f);

        setView();
        datePickerFunction();
        previousWeekFunction();
        nextWeekFunction();
        return view;
    }

    public void setView(){
        chart.removeAllViews();

        weekly = db.getWeeklyStat(picked_timestamp);

        Yvalue1 = new ArrayList<>();
        Yvalue2 = new ArrayList<>();
        Yvalue3 = new ArrayList<>();
        labels = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(picked_timestamp);
        while (calendar.get(Calendar.DAY_OF_WEEK) >= calendar.getFirstDayOfWeek()) {
            calendar.add(Calendar.DATE, -1);
        }

        startDate = new SimpleDateFormat("MMM dd", Locale.getDefault()).format(calendar.getTime());
        calendar.add(Calendar.DATE,6);
        endDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(calendar.getTime());

        datePicker.setText(startDate + " - " + endDate);

        if(weekly.size()!=0) {

            chart.setVisibility(View.VISIBLE);
            noDataDisplay.setVisibility(View.INVISIBLE);
            for (int i = 0; i < weekly.size(); i++) {
                Yvalue1.add(new BarEntry((float) weekly.get(i).getDesk_time(), i));
                Yvalue2.add(new BarEntry((float) weekly.get(i).getOffice_time(), i));
                Yvalue3.add(new BarEntry((float) weekly.get(i).getOutdoor_time(), i));
                if (picked_timestamp < sharedPreferences.getLong("installed_weekend", 0L)){
                    calendar.setTimeInMillis(picked_timestamp);
                    labels.add(daysOfWeek[(calendar.get(Calendar.DAY_OF_WEEK)-1)+i]);
                }
                else {
                    labels.add(daysOfWeek[i]);
                }
            }


            dataset1 = new BarDataSet(Yvalue1, "At Desk");
            dataset1.setColor(Color.rgb(1, 187, 212));

            dataset2 = new BarDataSet(Yvalue2, "Inside Office");
            dataset2.setColor(Color.rgb(76, 175, 81));

            dataset3 = new BarDataSet(Yvalue3, "Outside Office");
            dataset3.setColor(Color.rgb(255, 191, 6));

            dataSets = new ArrayList<>();
            dataSets.add(dataset1);
            dataSets.add(dataset2);
            dataSets.add(dataset3);

            chart.animateXY(3000, 3000);

            data = new BarData(labels,dataSets);

            chart.setData(data);
            chart.setDescription("");
            chart.getAxisLeft().setDrawLabels(false);
            chart.getAxisRight().setDrawLabels(false);

            data.setValueFormatter(yvalueFormatter);
            data.setValueTextSize(10f);

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

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, selected_year);
        calendar.set(Calendar.MONTH, selected_month);
        calendar.set(Calendar.DAY_OF_MONTH, selected_day);
        picked_timestamp = calendar.getTimeInMillis();
        setView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.gc();
    }
}