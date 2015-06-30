package com.beaconapp.user.beaconapp1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;


public class MainActivity extends ActionBarActivity {

    TextView tv_main,tv_left,tv_right,header;
    String str_desk = "", str_outdoor = "", str_office = "";
    Long time_desk = 0L,time_office = 0L,time_outdoor = 0L;
    int secs, mins, hours;
    Handler chHandler = new Handler();
    ImageSwitcher sw_main,sw_left,sw_right;
    SharedPreferences sharedPref;
    int defaultValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chHandler.postDelayed(timer, 0);
        setContentView(R.layout.activity_main);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int start = sharedPref.getInt(getString(R.string.shared_start), 0);
        if (start == 0) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.shared_start), 1);
            editor.commit();

            Intent intent = new Intent(MainActivity.this, NotificationService.class);
            startService(intent);

            Intent intent1 = new Intent(MainActivity.this, LoggerService.class);
            startService(intent1);
        }

        sw_main = (ImageSwitcher) findViewById(R.id.sw_main);
        sw_left = (ImageSwitcher) findViewById(R.id.sw_left);
        sw_right = (ImageSwitcher) findViewById(R.id.sw_right);

        sw_main.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                return myView;
            }
        });
        sw_left.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                return myView;
            }
        });
        sw_right.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                return myView;
            }
        });

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        sw_main.setInAnimation(in);
        sw_main.setOutAnimation(out);
        sw_left.setInAnimation(in);
        sw_left.setOutAnimation(out);
        sw_right.setInAnimation(in);
        sw_right.setOutAnimation(out);

    }

    @Override
    protected void onResume( ) {
        super.onResume();
    }

    @Override
    protected void onStart( ) {
        super.onStart();
    }
    @Override
    protected void onStop( ) {
        super.onStop();
    }
    @Override
    protected void onDestroy( ) {
        super.onDestroy();
    }

    public Runnable timer = new Runnable() {
        public void run() {
            tv_main = (TextView) findViewById(R.id.timer_main);
            tv_left = (TextView) findViewById(R.id.timer_left);
            tv_right = (TextView) findViewById(R.id.timer_right);
            header = (TextView) findViewById(R.id.header);

            int c;
            int pos = 3;
            try {
                pos = sharedPref.getInt(getString(R.string.shared_position), 3);

                time_desk = sharedPref.getLong(getString(R.string.shared_timer_desk), 0);

                time_office = sharedPref.getLong(getString(R.string.shared_timer_office), 0);

                time_outdoor = sharedPref.getLong(getString(R.string.shared_timer_outdoor), 0);

            } catch (Exception e) {
            }

            secs = (int) (time_desk / 1000);
            mins = secs / 60;
            secs = secs % 60;
            hours = mins / 60;
            mins = mins % 60;
            str_desk = ""+hours+" : "+mins+" : "+secs;

            secs = (int) (time_office / 1000);
            mins = secs / 60;
            secs = secs % 60;
            hours = mins / 60;
            mins = mins % 60;
            str_office = ""+hours+" : "+mins+" : "+secs;

            secs = (int) (time_outdoor / 1000);
            mins = secs / 60;
            secs = secs % 60;
            hours = mins / 60;
            mins = mins % 60;
            str_outdoor = ""+hours+" : "+mins+" : "+secs;

            if (pos == 1) {

                sw_main.setImageResource(R.drawable.work_large);
                sw_left.setImageResource(R.drawable.outdoor);
                sw_right.setImageResource(R.drawable.office);
                header.setText("HOURS AT YOUR DESK");
                tv_main.setText(str_desk);
                tv_left.setText(str_outdoor);
                tv_right.setText(str_office);
            } else if (pos == 2) {
                sw_main.setImageResource(R.drawable.office_large);
                sw_left.setImageResource(R.drawable.work);
                sw_right.setImageResource(R.drawable.outdoor);
                header.setText("HOURS INSIDE OFFICE");
                tv_main.setText(str_office);
                tv_left.setText(str_desk);
                tv_right.setText(str_outdoor);
            } else if (pos == 3) {
                sw_main.setImageResource(R.drawable.outdoor_large);
                sw_left.setImageResource(R.drawable.office);
                sw_right.setImageResource(R.drawable.work);
                header.setText("HOURS OUTSIDE OFFICE");
                tv_main.setText(str_outdoor);
                tv_left.setText(str_office);
                tv_right.setText(str_desk);
            }

            chHandler.postDelayed(timer, 1000);
        }
    };

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
