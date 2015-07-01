package com.beaconapp.user.mpandroidchart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        ArrayList<BarEntry> Yvalue1 = new ArrayList<>();
        Yvalue1.add(new BarEntry(4f, 0));
        Yvalue1.add(new BarEntry(8f, 1));
        Yvalue1.add(new BarEntry(6f, 2));
        Yvalue1.add(new BarEntry(4f, 3));
        Yvalue1.add(new BarEntry(8f, 4));
        Yvalue1.add(new BarEntry(6f, 5));
        Yvalue1.add(new BarEntry(6f, 6));

        ArrayList<BarEntry> Yvalue2 = new ArrayList<>();
        Yvalue2.add(new BarEntry(12f, 0));
        Yvalue2.add(new BarEntry(18f, 1));
        Yvalue2.add(new BarEntry(9f, 2));
        Yvalue2.add(new BarEntry(4f, 3));
        Yvalue2.add(new BarEntry(8f, 4));
        Yvalue2.add(new BarEntry(6f, 5));
        Yvalue2.add(new BarEntry(6f, 6));

        ArrayList<BarEntry> Yvalue3 = new ArrayList<>();
        Yvalue3.add(new BarEntry(4f, 0));
        Yvalue3.add(new BarEntry(8f, 1));
        Yvalue3.add(new BarEntry(6f, 2));
        Yvalue3.add(new BarEntry(4f, 3));
        Yvalue3.add(new BarEntry(8f, 4));
        Yvalue3.add(new BarEntry(6f, 5));
        Yvalue3.add(new BarEntry(6f, 6));


        BarDataSet dataset1 = new BarDataSet(Yvalue1, "At Desk");
        dataset1.setColor(Color.YELLOW);

        BarDataSet dataset2 = new BarDataSet(Yvalue2, "At Office");
        dataset2.setColor(Color.GREEN);

        BarDataSet dataset3 = new BarDataSet(Yvalue3, "Outside Office");
        dataset3.setColor(Color.CYAN);

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


        HorizontalBarChart chart = (HorizontalBarChart)findViewById(R.id.bchart);
        chart.animateXY(3000, 3000);


        BarData data = new BarData(labels,dataSets);
        chart.setData(data);
        data.setGroupSpace(200);
        chart.setDescription("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
