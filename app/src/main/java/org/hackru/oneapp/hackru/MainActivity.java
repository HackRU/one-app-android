package org.hackru.oneapp.hackru;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.hackru.oneapp.hackru.api.Login.SaveSharedPreference;

public class MainActivity extends AppCompatActivity {
    String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // END BOILERPLATE CODE

        if(SaveSharedPreference.getAuthToken(MainActivity.this).length() == 0) { // If the user isn't logged in, make them log in
            Intent loginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(loginActivityIntent);
        }

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
        fragmentManager.beginTransaction().add(R.id.content_frame, new TimerFragment(), "timer").commit(); // Adds the timer fragment when the app launches so it's displaying

        final BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.menu_timer).setChecked(true); // Makes the timer menu item checked on app load since it's the first item that appears (not sure if this is necessary)
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                bottomNavigationView.getMenu().findItem(R.id.menu_announcements).setChecked(false);
                bottomNavigationView.getMenu().findItem(R.id.menu_event).setChecked(false);
                bottomNavigationView.getMenu().findItem(R.id.menu_map).setChecked(false);
                bottomNavigationView.getMenu().findItem(R.id.menu_timer).setChecked(false);
                item.setChecked(true);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(item.getItemId() == R.id.menu_timer) {

                    if(fragmentManager.findFragmentByTag("timer")!=null) {
                        // if the fragment exists, show it
                        fragmentTransaction.show(fragmentManager.findFragmentByTag("timer"));
                    } else {
                        // if the fragment does not exist yet, add it to the fragment manager
                        fragmentTransaction.add(R.id.content_frame, new TimerFragment(), "timer");
                    }

                    if(fragmentManager.findFragmentByTag("map") !=null) {fragmentTransaction.hide(fragmentManager.findFragmentByTag("map"));}
                    if(fragmentManager.findFragmentByTag("events") !=null) {fragmentTransaction.hide(fragmentManager.findFragmentByTag("events"));}
                    if(fragmentManager.findFragmentByTag("announcements") !=null) {fragmentTransaction.hide(fragmentManager.findFragmentByTag("announcements"));}
                    fragmentTransaction.commit();
                    return true;
                } else if(item.getItemId() == R.id.menu_announcements) {

                    if(fragmentManager.findFragmentByTag("announcements")!=null) {
                        // if the fragment exists, show it
                        fragmentTransaction.show(fragmentManager.findFragmentByTag("announcements"));
                    } else {
                        // if the fragment does not exist yet, add it to the fragment manager
                        fragmentTransaction.add(R.id.content_frame, new AnnouncementsFragment(), "announcements");
                    }

                    if(fragmentManager.findFragmentByTag("map") !=null) {fragmentTransaction.hide(fragmentManager.findFragmentByTag("map"));}
                    if(fragmentManager.findFragmentByTag("events") !=null) {fragmentTransaction.hide(fragmentManager.findFragmentByTag("events"));}
                    if(fragmentManager.findFragmentByTag("timer") !=null) {fragmentTransaction.hide(fragmentManager.findFragmentByTag("timer"));}
                    fragmentTransaction.commit();
                    return true;
                } else if(item.getItemId() == R.id.menu_event) {
                    if(fragmentManager.findFragmentByTag("events")!=null) {
                        // if the fragment exists, show it
                        fragmentTransaction.show(fragmentManager.findFragmentByTag("events"));
                    } else {
                        // if the fragment does not exist yet, add it to the fragment manager
                        fragmentTransaction.add(R.id.content_frame, new EventsFragment(), "events");
                    }

                    if(fragmentManager.findFragmentByTag("map") !=null) {fragmentTransaction.hide(fragmentManager.findFragmentByTag("map"));}
                    if(fragmentManager.findFragmentByTag("timer") !=null) {fragmentTransaction.hide(fragmentManager.findFragmentByTag("timer"));}
                    if(fragmentManager.findFragmentByTag("announcements") !=null) {fragmentTransaction.hide(fragmentManager.findFragmentByTag("announcements"));}
                    fragmentTransaction.commit();
                    return true;
                } else if(item.getItemId() == R.id.menu_map) {
                    if(fragmentManager.findFragmentByTag("map")!=null) {
                        // if the fragment exists, show it
                        fragmentTransaction.show(fragmentManager.findFragmentByTag("map"));
                    } else {
                        // if the fragment does not exist yet, add it to the fragment manager
                        fragmentTransaction.add(R.id.content_frame, new MapFragment(), "map");
                    }

                    if(fragmentManager.findFragmentByTag("events") !=null) {fragmentTransaction.hide(fragmentManager.findFragmentByTag("events"));}
                    if(fragmentManager.findFragmentByTag("timer") !=null) {fragmentTransaction.hide(fragmentManager.findFragmentByTag("timer"));}
                    if(fragmentManager.findFragmentByTag("announcements") !=null) {fragmentTransaction.hide(fragmentManager.findFragmentByTag("announcements"));}
                    fragmentTransaction.commit();
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