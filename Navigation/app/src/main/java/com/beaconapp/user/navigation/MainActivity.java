package com.beaconapp.user.navigation;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see
    public String TITLES[];
    public int ICONS[];
    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    private SharedPreferences savednotes;

    String NAME = "";
    String EMAIL = "";
    int PROFILE = R.drawable.profile;

    private Toolbar toolbar ;                         // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;// Declaring Action Bar Drawer Toggle
    public static  int position = 0;


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TITLES = new String[]{"Home", "Statistics", "Reminders", "Profile", "Settings"};
        ICONS = new int[]{R.drawable.home, R.drawable.graph, R.drawable.reminder, R.drawable.profile, R.drawable.settings};

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);// Letting the system know that the list objects are of fixed size

        savednotes = PreferenceManager.getDefaultSharedPreferences(this);
        NAME = savednotes.getString("NAME_KEY", "NAME");
        EMAIL = savednotes.getString("COMPANY_NAME_KEY", "COMPANY NAME");

        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);// Setting the adapter to RecyclerView


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                    Drawer.closeDrawers();
                    Toast.makeText(MainActivity.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                   // onTouchDrawer(recyclerView.getChildPosition(child));
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


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
     //  toolbar.setNavigationIcon(R.drawable.ic_menu);
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,R.drawable.ic_menu,R.string.openDrawer,R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                switch (position){
                    case 0:break;
                    case 1:resetAdapter();
                        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                        tx.replace(R.id.container, Fragment.instantiate(MainActivity.this,"com.beaconapp.user.navigation.HomeFragment"));
                        tx.commit();
                        ICONS[0] = R.drawable.home_active;
                        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
                        mRecyclerView.setAdapter(mAdapter);
                        break;
                    case 2:resetAdapter();
                        FragmentTransaction dx = getSupportFragmentManager().beginTransaction();
                        dx.replace(R.id.container, Fragment.instantiate(MainActivity.this, "com.beaconapp.user.navigation.StatisticsFragment"));
                        dx.commit();
                        ICONS[1] = R.drawable.graph_active;
                        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
                        mRecyclerView.setAdapter(mAdapter);
                        break;
                    case 3:resetAdapter();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new NotificationFragment())
                                .commit();
                        ICONS[2] = R.drawable.reminder_active;
                        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
                        mRecyclerView.setAdapter(mAdapter);
                        break;
                    case 4:resetAdapter();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new ProfileFragment())
                                .commit();
                        ICONS[3] = R.drawable.profile_active;
                        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
                        mRecyclerView.setAdapter(mAdapter);
                        break;
                    case 5:resetAdapter();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new SettingsFragment())
                                .commit();
                        ICONS[4] = R.drawable.settings_active;
                        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
                        mRecyclerView.setAdapter(mAdapter);break;
                    default:FragmentTransaction fx = getSupportFragmentManager().beginTransaction();
                        fx.replace(R.id.container, Fragment.instantiate(MainActivity.this,"com.beaconapp.user.navigation.HomeFragment"));
                        fx.commit();break;
                }


            }



        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

    }


    public void resetAdapter(){
        ICONS = new int[]{R.drawable.home, R.drawable.graph, R.drawable.reminder, R.drawable.profile, R.drawable.settings};
        savednotes = PreferenceManager.getDefaultSharedPreferences(this);
        NAME = savednotes.getString("NAME_KEY", "NAME");
        EMAIL = savednotes.getString("COMPANY_NAME_KEY", "COMPANY NAME");
        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        mRecyclerView.setAdapter(mAdapter);// Setting the adapter to RecyclerView

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

        return super.onOptionsItemSelected(item);
    }
}