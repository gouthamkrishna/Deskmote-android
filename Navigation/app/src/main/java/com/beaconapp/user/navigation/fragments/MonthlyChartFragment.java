package com.beaconapp.user.navigation.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beaconapp.user.navigation.database.DatabaseHandler;
import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.classes.DailyStat;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MonthlyChartFragment extends Fragment {

    DatabaseHandler db;
    HorizontalBarChart chart;

    List<DailyStat> monthly;
    TextView nodata;
    long timestamp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly_chart_layout, container, false);

        Calendar calendar = Calendar.getInstance();
        timestamp = calendar.getTimeInMillis();

        db = new DatabaseHandler(getActivity());
        nodata = (TextView)view.findViewById(R.id.noDataMonth);
        nodata.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "digital-7.ttf"));
        nodata.setTextSize(25f);
        chart = (HorizontalBarChart)view.findViewById(R.id.monthlybchart);
        monthly = new ArrayList<>();
        monthly = db.getMonthlyStat(timestamp);

        ArrayList<BarEntry> Yvalue1 = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

        if(monthly.size()!=0) {

            chart.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.INVISIBLE);
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

            set1.setColors(new int[]{Color.rgb(255, 191, 6), Color.rgb(76, 175, 81), Color.rgb(1, 187, 212)});
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
            nodata.setVisibility(View.VISIBLE);
        }
        return view;
    }
}
