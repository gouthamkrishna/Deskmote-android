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

/**
 * Created by user on 2/7/15.
 */
public class WeeklyChartFragment extends Fragment {

    DatabaseHandler db;
    HorizontalBarChart chart;

    List<DailyStat> weekly;
    TextView nodata;
    long timestamp;

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

        db = new DatabaseHandler(getActivity());
        nodata = (TextView)view.findViewById(R.id.noDataWeek);
        nodata.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "digital-7.ttf"));
        nodata.setTextSize(25f);
        chart = (HorizontalBarChart)view.findViewById(R.id.weeklybchart);
        weekly = new ArrayList<>();
        weekly = db.getWeeklyStat(timestamp);

        ArrayList<BarEntry> Yvalue1 = new ArrayList<>();
        ArrayList<BarEntry> Yvalue2 = new ArrayList<>();
        ArrayList<BarEntry> Yvalue3 = new ArrayList<>();

        if(weekly.size()!=0) {

            chart.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.INVISIBLE);
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
            nodata.setVisibility(View.VISIBLE);
        }

        return view;
    }
}