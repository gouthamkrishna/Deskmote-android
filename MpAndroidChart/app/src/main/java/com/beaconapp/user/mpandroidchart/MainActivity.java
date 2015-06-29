package com.beaconapp.user.mpandroidchart;

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

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));

        entries.add(new BarEntry(12f, 4));
        entries.add(new BarEntry(18f, 5));
        entries.add(new BarEntry(9f, 6));

        entries.add(new BarEntry(4f, 8));
        entries.add(new BarEntry(8f, 9));
        entries.add(new BarEntry(6f, 10));

        entries.add(new BarEntry(12f, 12));
        entries.add(new BarEntry(18f, 13));
        entries.add(new BarEntry(9f, 14));

        entries.add(new BarEntry(4f, 16));
        entries.add(new BarEntry(8f, 17));
        entries.add(new BarEntry(6f, 18));

        entries.add(new BarEntry(12f, 20));
        entries.add(new BarEntry(18f, 21));
        entries.add(new BarEntry(9f, 22));

        entries.add(new BarEntry(4f, 24));
        entries.add(new BarEntry(8f, 25));
        entries.add(new BarEntry(6f, 26));

        BarDataSet dataset = new BarDataSet(entries, "");
      //  dataset.setColors(ColorTemplate.JOYFUL_COLORS);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("01");
        labels.add("");
        labels.add("");
        labels.add("");
        labels.add("02");
        labels.add("");
        labels.add("");
        labels.add("");
        labels.add("03");
        labels.add("");
        labels.add("");
        labels.add("");
        labels.add("04");
        labels.add("");
        labels.add("");
        labels.add("");
        labels.add("05");
        labels.add("");
        labels.add("");
        labels.add("");
        labels.add("06");
        labels.add("");
        labels.add("");
        labels.add("");
        labels.add("07");
        labels.add("");
        labels.add("");
        labels.add("");
        HorizontalBarChart chart = (HorizontalBarChart)findViewById(R.id.bchart);
        chart.animateXY(3000, 3000);

        //setContentView(chart);

        BarData data = new BarData(labels, dataset);
        chart.setData(data);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
