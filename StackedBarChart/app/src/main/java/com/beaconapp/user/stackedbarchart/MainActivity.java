package com.beaconapp.user.stackedbarchart;

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
        for (int i = 0; i < 30; i++) {
            float mult = 30;
            float val1 = (float) (Math.random() * mult) + mult / 3;
            float val2 = (float) (Math.random() * mult) + mult / 3;
            float val3 = (float) (Math.random() * mult) + mult / 3;

            Yvalue1.add(new BarEntry(new float[]{
                    val1, val2, val3
            }, i));
        }


        BarDataSet set1 = new BarDataSet(Yvalue1, "");
        Color c=new Color();
        public final static Color red = new Color(255, 0, 0);

        //int[] colors= new int[]{Color.GREEN,Color.BLUE,android.R.color.holo_purple};
        set1.setColors(new int[]{R.color.yellow,R.color.blue,R.color.green});
        set1.setStackLabels(new String[] {
                "At Desk", "At Office", "Outside Office"});

        ArrayList<String> labels = new ArrayList<String>();

        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");
        labels.add("6");
        labels.add("7");
        labels.add("8");
        labels.add("9");
        labels.add("10");
        labels.add("11");
        labels.add("12");
        labels.add("13");
        labels.add("14");
        labels.add("15");
        labels.add("16");
        labels.add("17");
        labels.add("18");
        labels.add("19");
        labels.add("20");
        labels.add("21");
        labels.add("22");
        labels.add("23");
        labels.add("24");
        labels.add("25");
        labels.add("26");
        labels.add("27");
        labels.add("28");
        labels.add("29");
        labels.add("30");

        HorizontalBarChart chart = (HorizontalBarChart) findViewById(R.id.bchart);
        chart.animateXY(3000, 3000);


        BarData data = new BarData(labels, set1);
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
