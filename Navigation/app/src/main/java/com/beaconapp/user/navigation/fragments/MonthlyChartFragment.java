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


public class MonthlyChartFragment extends Fragment implements DatePickerFragment.MonthlyChartFragment {

    DatabaseHandler db;
    HorizontalBarChart chart;

    List<DailyStat> monthly;
    TextView monthPick, noDataDisplay;
    ImageView preMonth, nextMonth;
    DatePickerFragment newDateFragment;
    long picked_timestamp;
    Calendar calendar;
    String month;
    YvalueFormatter yvalueFormatter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly_chart_layout, container, false);

        calendar = Calendar.getInstance();
        picked_timestamp = calendar.getTimeInMillis();
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
        yvalueFormatter = new YvalueFormatter();
        setView();
        datePickerFunction();
        previousMonthFunction();
        nextMonthFunction();
        return view;
    }

    public void setView() {
        chart.removeAllViews();
        monthly = db.getMonthlyStat(picked_timestamp);

        ArrayList<BarEntry> Yvalue1 = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        calendar.setTimeInMillis(picked_timestamp);
        month = new SimpleDateFormat("MMM, yyyy", Locale.getDefault()).format(calendar.getTime());
        monthPick.setText(month);

        if(monthly.size()!=0) {

            chart.setVisibility(View.VISIBLE);
            int day = 0;
            noDataDisplay.setVisibility(View.INVISIBLE);

            for (int i = 0; i < calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                labels.add(i,""+(i+1));
            }

            for (int i =0; i < monthly.size(); i++) {
                float val1, val2, val3;
                calendar.setTimeInMillis(monthly.get(i).getTstamp());
                val1 = (float) monthly.get(i).getDesk_time();
                val2 = (float) monthly.get(i).getOffice_time();
                val3 = (float) monthly.get(i).getOutdoor_time();
                day = calendar.get(Calendar.DAY_OF_MONTH);

                Yvalue1.add(new BarEntry(new float[]{
                        val1, val2, val3
                }, calendar.get(Calendar.DAY_OF_MONTH)-1));
                labels.remove((calendar.get(Calendar.DAY_OF_MONTH)-1));
                labels.add((calendar.get(Calendar.DAY_OF_MONTH)-1), ""+day);
            }
//
//            for (int i = 0; i < calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
//
//                float val1, val2, val3;
//                if (i < monthly.size()) {
//                    val1 = (float) monthly.get(i).getDesk_time();
//                    val2 = (float) monthly.get(i).getOffice_time();
//                    val3 = (float) monthly.get(i).getOutdoor_time();
//
//                    Calendar tempCalendar = Calendar.getInstance();
//                    tempCalendar.setTimeInMillis(monthly.get(i).getTstamp());
//                    day = tempCalendar.get(Calendar.DAY_OF_MONTH);
//                }
//                else {
//                    val1 = 0f;
//                    val2 = 0f;
//                    val3 = 0f;
//                    day = day+1;
//                }
//
//
//
//                Yvalue1.add(new BarEntry(new float[]{
//                        val1, val2, val3
//                }, i));
//                labels.add("" + day);
//            }

            BarDataSet set1 = new BarDataSet(Yvalue1, "");

            set1.setColors(new int[]{Color.rgb(1, 187, 212), Color.rgb(76, 175, 81), Color.rgb(255, 191, 6)});
            set1.setStackLabels(new String[]{
                    "At Desk", "Inside Office", "Outside Office"});

            chart.animateXY(3000, 3000);


            BarData data = new BarData(labels, set1);
            chart.setData(data);
            data.setGroupSpace(200);
            chart.setDescription("");
            chart.getAxisLeft().setDrawLabels(false);
            chart.getAxisRight().setDrawLabels(false);
            data.setValueFormatter(yvalueFormatter);
            data.setDrawValues(true);
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

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, selected_year);
        calendar.set(Calendar.MONTH, selected_month);
        calendar.set(Calendar.DAY_OF_MONTH, selected_day);
        picked_timestamp = calendar.getTimeInMillis();
        setView();
    }
}