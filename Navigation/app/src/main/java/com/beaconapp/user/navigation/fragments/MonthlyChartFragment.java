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


public class MonthlyChartFragment extends Fragment implements DatePickerFragment.MonthlyChartFragment {

    DatabaseHandler db;
    HorizontalBarChart chart;

    List<DailyStat> monthly;
    TextView monthPick, noDataDisplay;
    ImageView preMonth, nextMonth;
    DatePickerFragment newDateFragment;
    long timestamp, picked_timestamp;
    Calendar calendar;
    Date currentDate, cDate;
    String month;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly_chart_layout, container, false);

        calendar = Calendar.getInstance();
        timestamp = calendar.getTimeInMillis();

        picked_timestamp = timestamp;
        monthPick = (TextView) view.findViewById(R.id.monthView);
        newDateFragment = new DatePickerFragment(this);
        preMonth = (ImageView) view.findViewById(R.id.previousMonth);
        nextMonth = (ImageView) view.findViewById(R.id.nextMonth);
        db = new DatabaseHandler(getActivity());
        noDataDisplay = (TextView)view.findViewById(R.id.noDataMonth);
        noDataDisplay.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "digital-7.ttf"));
        noDataDisplay.setTextSize(25f);
        chart = (HorizontalBarChart)view.findViewById(R.id.monthlybchart);
        monthly = new ArrayList<>();

        setView();
        datePickerFunction();
        previousMonthFunction();
        nextMonthFunction();
        return view;
    }

    public void setView() {
        monthly = db.getMonthlyStat(timestamp);

        ArrayList<BarEntry> Yvalue1 = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

        currentDate = new Date(picked_timestamp);
        month = new SimpleDateFormat("MMM, yyyy").format(currentDate);
        monthPick.setText(month);

        if(monthly.size()!=0) {

            chart.setVisibility(View.VISIBLE);
            noDataDisplay.setVisibility(View.INVISIBLE);
            for (int i = 0; i < monthly.size(); i++) {

                float val1 = (float) monthly.get(i).getDesk_time();
                float val2 = (float) monthly.get(i).getOffice_time();
                float val3 = (float) monthly.get(i).getOutdoor_time();

                Yvalue1.add(new BarEntry(new float[]{
                        val1, val2, val3
                }, i));
                labels.add("" + i);
            }


            BarDataSet set1 = new BarDataSet(Yvalue1, "");

            set1.setColors(new int[]{Color.rgb(1, 187, 212), Color.rgb(76, 175, 81), Color.rgb(255, 191, 6)});
            set1.setStackLabels(new String[]{
                    "At Desk", "At Office", "Outside Office"});

            chart.animateXY(3000, 3000);


            BarData data = new BarData(labels, set1);
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

        monthPick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                newDateFragment.show(fragmentTransaction, "Date picker");
            }
        });
    }

    public void previousMonthFunction(){
        preMonth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                picked_timestamp = calendar.getTimeInMillis();
                setView();
            }
        });
    }

    public void nextMonthFunction(){
        nextMonth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                picked_timestamp = calendar.getTimeInMillis();
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
