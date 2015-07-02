package com.beaconapp.user.navigation;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity{

    public String TITLES[];
    public int ICONS[];

    private SharedPreferences savednotes;

    String NAME = "";
    String EMAIL = "";
    int PROFILE = R.drawable.profile;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout mDrawer;

    ActionBarDrawerToggle mDrawerToggle;
    public static  int position = 0;
    private FrameLayout containerlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TITLES = new String[]{"Home", "Statistics", "Reminders", "Profile", "Settings"};
        ICONS = new int[]{R.drawable.home, R.drawable.graph, R.drawable.reminder, R.drawable.profile, R.drawable.settings};

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);// Letting the system know that the list objects are of fixed size

        savednotes = PreferenceManager.getDefaultSharedPreferences(this);
        NAME = savednotes.getString("NAME_KEY", "NAME");
        EMAIL = savednotes.getString("COMPANY_NAME_KEY", "COMPANY NAME");

        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        mRecyclerView.setAdapter(mAdapter);// Setting the adapter to RecyclerView
        containerlayout = (FrameLayout)findViewById(R.id.container);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new HomeFragment())
                .commit();
        ICONS[0] = R.drawable.home_active;
        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        mRecyclerView.setAdapter(mAdapter);

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
                    Toast.makeText(MainActivity.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    position = recyclerView.getChildPosition(child);

                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

        mDrawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawer,R.drawable.ic_menu,R.string.openDrawer,R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                    //Do Nothing
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                switch (position){
                    case 0:break;
                    case 1:resetAdapter();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new HomeFragment())
                                .commit();
                        ICONS[0] = R.drawable.home_active;
                        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);
                        mRecyclerView.setAdapter(mAdapter);
                        break;
                    case 2:resetAdapter();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new StatisticsFragment())
                                .commit();
                        ICONS[1] = R.drawable.graph_active;
                        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);
                        mRecyclerView.setAdapter(mAdapter);
                        break;
                    case 3:resetAdapter();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new NotificationFragment())
                                .commit();
                        ICONS[2] = R.drawable.reminder_active;
                        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);
                        mRecyclerView.setAdapter(mAdapter);
                        break;
                    case 4:resetAdapter();
                        containerlayout.removeAllViews();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new ProfileFragment())
                                .commit();
                        ICONS[3] = R.drawable.profile_active;
                        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);
                        mRecyclerView.setAdapter(mAdapter);
                        break;
                    case 5:resetAdapter();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new SettingsFragment())
                                .commit();
                        ICONS[4] = R.drawable.settings_active;
                        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);
                        mRecyclerView.setAdapter(mAdapter);
                        break;
                    default:resetAdapter();
                        getFragmentManager().beginTransaction()
                            .replace(R.id.container, new HomeFragment())
                            .commit();
                        break;
                }

            }

        }; // Drawer Toggle Object Made
        mDrawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

    }

    public void resetAdapter(){

        ICONS = new int[]{R.drawable.home, R.drawable.graph, R.drawable.reminder, R.drawable.profile, R.drawable.settings};
        savednotes = PreferenceManager.getDefaultSharedPreferences(this);
        NAME = savednotes.getString("NAME_KEY", "NAME");
        EMAIL = savednotes.getString("COMPANY_NAME_KEY", "COMPANY NAME");
        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


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

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}