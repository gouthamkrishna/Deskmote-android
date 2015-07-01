package com.beaconapp.user.piechart1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private RelativeLayout mainLayout;
    private PieChart mchart;
    private float Ydata[] = {20,30,10,40};
    private String Xdata[] = {"A","B","C","D"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout =(RelativeLayout)findViewById(R.id.mainLayout);
        mchart = new PieChart(this);

        mainLayout.addView(mchart);
        mainLayout.setBackgroundColor(Color.WHITE);

        mchart.setUsePercentValues(true);
        mchart.setDescription("TEST");

        mchart.setDrawHoleEnabled(true);
        mchart.setHoleColorTransparent(true);
        mchart.setHoleRadius(50);
        mchart.setTransparentCircleRadius(10);

        mchart.setRotationAngle(0);
        mchart.setRotationEnabled(true);

        mchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                if (entry == null)
                    return;
                 Toast.makeText(MainActivity.this, Xdata[entry.getXIndex()] + "'" + entry.getVal() + "%", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        addData();

        Legend i = mchart.getLegend();
        i.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        i.setXEntrySpace(7);
        i.setYEntrySpace(5);

    }

    private void addData(){
        ArrayList<Entry> Yval = new ArrayList<Entry>();
        for (int i = 0; i < Ydata.length; i++){
            Yval.add(new Entry(Ydata[i],i));
        }
        ArrayList<String> Xval = new ArrayList<String>();
        for(int i = 0 ; i < Xdata.length; i++){
            Xval.add(Xdata[i]);
        }

        PieDataSet dataset = new PieDataSet(Yval,"");
        dataset.setSliceSpace(3);
        dataset.setSelectionShift(13);

        dataset.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData data = new PieData(Xval,dataset);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);;
        data.setValueTextColor(Color.GRAY);

        mchart.setData(data);
        mchart.highlightValues(null);
        mchart.invalidate();

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
