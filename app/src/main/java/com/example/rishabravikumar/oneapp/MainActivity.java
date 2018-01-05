package com.example.rishabravikumar.oneapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255,255,76,76)));
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){}
            else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                while(true) {
                    if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        break;
                    }
                }
            }

            final FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            final TimerFragment timerFragment = new TimerFragment();
            final EventsFragment eventsFragment = new EventsFragment();
            final MapFragment mapFragment = new MapFragment();
            final AnnouncementsFragment announcementsFragment = new AnnouncementsFragment();
            fragmentTransaction.add(R.id.content_frame, timerFragment);
            fragmentTransaction.detach(timerFragment);
            fragmentTransaction.add(R.id.content_frame, eventsFragment);
            fragmentTransaction.detach(eventsFragment);
            fragmentTransaction.add(R.id.content_frame, announcementsFragment);
            fragmentTransaction.detach(announcementsFragment);
            fragmentTransaction.add(R.id.content_frame, mapFragment);
            fragmentTransaction.commit();

            final BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    item.setChecked(true);
                    bottomNavigationView.getMenu().findItem(R.id.menu_announcements).setChecked(false);
                    bottomNavigationView.getMenu().findItem(R.id.menu_event).setChecked(false);
                    bottomNavigationView.getMenu().findItem(R.id.menu_map).setChecked(false);
                    bottomNavigationView.getMenu().findItem(R.id.menu_timer).setChecked(false);
                    item.setChecked(true);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    if(item.getItemId() == R.id.menu_timer) {
                        fragmentTransaction.hide(mapFragment);
                        fragmentTransaction.detach(eventsFragment);
                        fragmentTransaction.detach(announcementsFragment);
                        fragmentTransaction.attach(timerFragment);
                        fragmentTransaction.commit();
                    }
                    if(item.getItemId() == R.id.menu_map) {
                        fragmentTransaction.detach(timerFragment);
                        fragmentTransaction.detach(eventsFragment);
                        fragmentTransaction.detach(announcementsFragment);
                        fragmentTransaction.show(mapFragment);
                        fragmentTransaction.commit();
                    }
                    if(item.getItemId() == R.id.menu_announcements) {
                        fragmentTransaction.detach(timerFragment);
                        fragmentTransaction.detach(eventsFragment);
                        fragmentTransaction.hide(mapFragment);
                        fragmentTransaction.attach(announcementsFragment);
                        fragmentTransaction.commit();
                    }
                    if(item.getItemId() == R.id.menu_event) {
                        fragmentTransaction.detach(timerFragment);
                        fragmentTransaction.hide(mapFragment);
                        fragmentTransaction.detach(announcementsFragment);
                        fragmentTransaction.attach(eventsFragment);
                        fragmentTransaction.commit();
                    }
                    return false;
                }
            });
        }
    }