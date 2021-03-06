package com.beaconapp.user.navigation.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.classes.MyAdapter;
import com.beaconapp.user.navigation.fragments.HomeFragment;
import com.beaconapp.user.navigation.fragments.NotificationFragment;
import com.beaconapp.user.navigation.fragments.ProfileFragment;
import com.beaconapp.user.navigation.fragments.ReminderFragment;
import com.beaconapp.user.navigation.fragments.SettingsFragment;
import com.beaconapp.user.navigation.fragments.StatisticsFragment;


public class MainActivity extends AppCompatActivity {

    public static int position = 0;
    public static MenuItem reminder_action;
    public final String TITLES[] = {"Home", "Statistics", "Reminders", "Profile", "Settings"};
    public int ICONS[];

    private SharedPreferences savednotes;
    FragmentManager fragmentManager;

    String NAME = "", EMAIL = "", PROFILE = "",typeOfLaunch = "";
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout mDrawer;
    ActionBarDrawerToggle mDrawerToggle;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        typeOfLaunch = getIntent().getStringExtra("LaunchType");

        if (getSupportActionBar()!= null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_ic_menu);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mToolbar = new Toolbar(this);

        savednotes = PreferenceManager.getDefaultSharedPreferences(this);
        PROFILE = savednotes.getString("PATH_KEY", PROFILE);
        NAME = savednotes.getString("NAME_KEY", "Name");
        EMAIL = savednotes.getString("COMPANY_NAME_KEY", "Company Name");
        ICONS = new int[]{R.drawable.home_active, R.drawable.graph, R.drawable.reminder, R.drawable.profile, R.drawable.settings};

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        touchListenerFunction();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        mDrawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,R.string.openDrawer,R.string.closeDrawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
                if(position==4) {
                    PROFILE = savednotes.getString("PATH_KEY", PROFILE);
                    NAME = savednotes.getString("NAME_KEY", "Name");
                    EMAIL = savednotes.getString("COMPANY_NAME_KEY", "Company Name");
                    mAdapter = new MyAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        position = 3;
        loadCorrespondingFragment();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void resetAdapter(){

        ICONS = new int[]{R.drawable.home, R.drawable.graph, R.drawable.reminder, R.drawable.profile, R.drawable.settings};
        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadCorrespondingFragment() {
        switch (position){
            case 0:break;
            case 1:resetAdapter();
                reminder_action.setVisible(false);
                fragmentManager.beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                ICONS[0] = R.drawable.home_active;
                break;
            case 2:resetAdapter();
                reminder_action.setVisible(false);
                fragmentManager.beginTransaction().replace(R.id.container, new StatisticsFragment()).commit();
                ICONS[1] = R.drawable.graph_active;
                break;
            case 3:resetAdapter();
                reminder_action.setVisible(true);
                reminder_action.setIcon(R.drawable.add_reminder);
                fragmentManager.beginTransaction().replace(R.id.container, new NotificationFragment()).commit();
                ICONS[2] = R.drawable.reminder_active;
                break;
            case 4:resetAdapter();
                reminder_action.setVisible(true);
                reminder_action.setIcon(R.drawable.pencil);
                fragmentManager.beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
                ICONS[3] = R.drawable.profile_active;
                break;
            case 5:resetAdapter();
                reminder_action.setVisible(false);
                fragmentManager.beginTransaction().replace(R.id.container, new SettingsFragment()).commit();
                ICONS[4] = R.drawable.settings_active;
                break;
            default:resetAdapter();
                reminder_action.setVisible(false);
                fragmentManager.beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setActionBarTitle(String title) {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void setActionBarColor(Drawable color) {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(color);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        reminder_action = menu.findItem(R.id.reminder);
        reminder_action.setIcon(R.drawable.add_reminder);
        reminder_action.setVisible(false);
        if (typeOfLaunch.equals("reminder")) {
            position = 3;
            loadCorrespondingFragment();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.reminder && position == 3) {

            ReminderFragment dialogFragment = new ReminderFragment();
            dialogFragment.show(getSupportFragmentManager().beginTransaction(), "");
        }

        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void touchListenerFunction(){

        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    mDrawer.closeDrawers();
                    position = recyclerView.getChildAdapterPosition(child);
                    loadCorrespondingFragment();
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });
    }
}
