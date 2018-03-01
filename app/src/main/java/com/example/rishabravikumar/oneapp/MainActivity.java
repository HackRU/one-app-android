package com.example.rishabravikumar.oneapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            // END BOILERPLATE CODE

            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255,255,76,76)));
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            // TODO: FIX BUG WHERE APP CRASHES FROM INFINITE LOOP IF LOCATION PERMISSION IS DENIED
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                while(true) {
                    if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        break;
                    }
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                }
            }

            final FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.content_frame, new MapFragment(), "map").commit(); // Adds the map fragment when the app launches so it's displaying

            final BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
            bottomNavigationView.getMenu().findItem(R.id.menu_map).setChecked(true); // Makes the map menu item checked on app load since it's the first item that appears (not sure if this is necessary)
            //TODO: RECREATE FRAGMENT IF USER CLICKS MENU ITEM THAT IS ALREADY SELECTED
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_announcements).setChecked(false);
                    bottomNavigationView.getMenu().findItem(R.id.menu_event).setChecked(false);
                    bottomNavigationView.getMenu().findItem(R.id.menu_map).setChecked(false);
                    bottomNavigationView.getMenu().findItem(R.id.menu_timer).setChecked(false);
                    item.setChecked(true);

                    if(item.getItemId() == R.id.menu_map) {
                        // Since the app starts out on the map fragment, it will always exist, so there is no need to test for whether or not it exists yet
                        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("map")).commit();

                        if(fragmentManager.findFragmentByTag("events") !=null) {fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("events")).commit();}
                        if(fragmentManager.findFragmentByTag("timer") !=null) {fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("timer")).commit();}
                        if(fragmentManager.findFragmentByTag("announcements") !=null) {fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("announcements")).commit();}

                        return true;
                    }
                    if(item.getItemId() == R.id.menu_event) {
                        if(fragmentManager.findFragmentByTag("events")!=null) {
                            // if the fragment exists, show it
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("events")).commit();
                        } else {
                            // if the fragment does not exist yet, add it to the fragment manager
                            fragmentManager.beginTransaction().add(R.id.content_frame, new EventsFragment(), "events").commit();
                        }

                        if(fragmentManager.findFragmentByTag("map") !=null) {fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("map")).commit();}
                        if(fragmentManager.findFragmentByTag("timer") !=null) {fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("timer")).commit();}
                        if(fragmentManager.findFragmentByTag("announcements") !=null) {fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("announcements")).commit();}

                        return true;
                    }
                    if(item.getItemId() == R.id.menu_timer) {

                        if(fragmentManager.findFragmentByTag("timer")!=null) {
                            // if the fragment exists, show it
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("timer")).commit();
                        } else {
                            // if the fragment does not exist yet, add it to the fragment manager
                            fragmentManager.beginTransaction().add(R.id.content_frame, new TimerFragment(), "timer").commit();
                        }

                        if(fragmentManager.findFragmentByTag("map") !=null) {fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("map")).commit();}
                        if(fragmentManager.findFragmentByTag("events") !=null) {fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("events")).commit();}
                        if(fragmentManager.findFragmentByTag("announcements") !=null) {fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("announcements")).commit();}

                        return true;
                    }

                    if(item.getItemId() == R.id.menu_announcements) {

                        if(fragmentManager.findFragmentByTag("announcements")!=null) {
                            // if the fragment exists, show it
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("announcements")).commit();
                        } else {
                            // if the fragment does not exist yet, add it to the fragment manager
                            fragmentManager.beginTransaction().add(R.id.content_frame, new AnnouncementsFragment(), "announcements").commit();
                        }

                        if(fragmentManager.findFragmentByTag("map") !=null) {fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("map")).commit();}
                        if(fragmentManager.findFragmentByTag("events") !=null) {fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("events")).commit();}
                        if(fragmentManager.findFragmentByTag("timer") !=null) {fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("timer")).commit();}

                        return true;
                    }

                    return false;
                }
            });

            FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.qr_fab);
            myFab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.e("FAB: ", "Clicked");
                    QRDialogueFragment QRFragment = new QRDialogueFragment();
                    QRFragment.show(fragmentManager, "fragment_qrdialogue");
                }
            });

        }
    }