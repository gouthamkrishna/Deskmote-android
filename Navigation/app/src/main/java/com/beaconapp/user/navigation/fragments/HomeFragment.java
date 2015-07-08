package com.beaconapp.user.navigation.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.beaconapp.user.navigation.services.LoggerService;
import com.beaconapp.user.navigation.activities.MainActivity;
import com.beaconapp.user.navigation.services.NotificationService;
import com.beaconapp.user.navigation.R;


public class HomeFragment extends Fragment {


    TextView tv_main,tv_left,tv_right,header1,header2;
    String str_desk = "", str_outdoor = "", str_office = "";
    Long time_desk = 0L,time_office = 0L,time_outdoor = 0L;
    int secs, mins, hours;
    Handler chHandler = new Handler();
    ImageSwitcher sw_main,sw_left,sw_right;
    SharedPreferences sharedPref;
    boolean defaultValue = false;
    Drawable d;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Home");

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);

        tv_main = (TextView) root.findViewById(R.id.timer_main);
        tv_left = (TextView) root.findViewById(R.id.timer_left);
        tv_right = (TextView) root.findViewById(R.id.timer_right);
        header1 = (TextView) root.findViewById(R.id.header1);
        header2 = (TextView) root.findViewById(R.id.header2);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),"digital-7.ttf");
        tv_main.setTypeface(face);
        tv_left.setTypeface(face);
        tv_right.setTypeface(face);

        chHandler.postDelayed(timer, 0);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean start = sharedPref.getBoolean(getString(R.string.shared_start), defaultValue);
        if (!start) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.shared_start), true);
            editor.commit();

            Intent intent = new Intent(getActivity(), NotificationService.class);
            getActivity().startService(intent);

            Intent intent1 = new Intent(getActivity(), LoggerService.class);
            getActivity().startService(intent1);
        }

        sw_main = (ImageSwitcher) root.findViewById(R.id.sw_main);
        sw_left = (ImageSwitcher) root.findViewById(R.id.sw_left);
        sw_right = (ImageSwitcher) root.findViewById(R.id.sw_right);

        sw_main.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getActivity());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                return myView;
            }
        });
        sw_left.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getActivity());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                return myView;
            }
        });
        sw_right.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getActivity());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                return myView;
            }
        });

        Animation in = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
        sw_main.setInAnimation(in);
        sw_main.setOutAnimation(out);
        sw_left.setInAnimation(in);
        sw_left.setOutAnimation(out);
        sw_right.setInAnimation(in);
        sw_right.setOutAnimation(out);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        try {
            chHandler.removeCallbacks(timer);
            chHandler = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }
    @Override
    public void onDestroy() {
        try {
            chHandler.removeCallbacks(timer);
            chHandler = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public Runnable timer = new Runnable() {
        public void run() {

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
                d = new ColorDrawable(Color.rgb(77,208,225));
                ((MainActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(d);
                sw_main.setImageResource(R.drawable.work_large);
                sw_left.setImageResource(R.drawable.outdoor);
                sw_right.setImageResource(R.drawable.office);
                header1.setText("HOURS");
                header2.setText("AT YOUR DESK");
                tv_main.setText(str_desk);
                tv_left.setText(str_outdoor);
                tv_right.setText(str_office);
            } else if (pos == 2) {
                d = new ColorDrawable(Color.rgb(129,199,132));
                ((MainActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(d);
                sw_main.setImageResource(R.drawable.office_large);
                sw_left.setImageResource(R.drawable.work);
                sw_right.setImageResource(R.drawable.outdoor);
                header1.setText("HOURS");
                header2.setText("INSIDE OFFICE");
                tv_main.setText(str_office);
                tv_left.setText(str_desk);
                tv_right.setText(str_outdoor);
            } else if (pos == 3) {
                d = new ColorDrawable(Color.rgb(255,213,79));
                ((MainActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(d);
                sw_main.setImageResource(R.drawable.outdoor_large);
                sw_left.setImageResource(R.drawable.office);
                sw_right.setImageResource(R.drawable.work);
                header1.setText("HOURS");
                header2.setText("OUTSIDE OFFICE");
                tv_main.setText(str_outdoor);
                tv_left.setText(str_office);
                tv_right.setText(str_desk);
            }

            chHandler.postDelayed(timer, 1000);
        }
    };

}
