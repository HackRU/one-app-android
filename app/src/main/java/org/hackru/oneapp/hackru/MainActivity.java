package org.hackru.oneapp.hackru;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
//import android.support.design.widget.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import org.hackru.oneapp.hackru.utils.SharedPreferencesUtility;

public class MainActivity extends AppCompatActivity implements QRDialogueFragment.OnLogoutClickListener {
    String TAG = "";

    FloatingActionMenu fabMenu;
    FloatingActionButton fabMap, fabQR, fabScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // END BOILERPLATE CODE


        /* ===== BOTTOM NAVIGATION ===== */
        //TODO: Fix jitter on navigation change
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, new TimerFragment(), "timer").commit(); // Adds the timer fragment when the app launches so it's displaying

        final BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.menu_timer).setChecked(true); // Makes the timer menu item checked on app load since it's the first item that appears (not sure if this is necessary)
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                bottomNavigationView.getMenu().findItem(R.id.menu_announcements).setChecked(false);
                bottomNavigationView.getMenu().findItem(R.id.menu_event).setChecked(false);
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

                    if(fragmentManager.findFragmentByTag("timer") !=null) {fragmentTransaction.hide(fragmentManager.findFragmentByTag("timer"));}
                    if(fragmentManager.findFragmentByTag("announcements") !=null) {fragmentTransaction.hide(fragmentManager.findFragmentByTag("announcements"));}
                    fragmentTransaction.commit();
                    return true;
                }

                return false;
            }
        });
        /* ===== /BOTTOM NAVIGATION ===== */



        /* ===== FLOATING ACTION BUTTON ===== */
        //TODO: Make FAB appear above the fragments
        fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        fabMap = (FloatingActionButton) findViewById(R.id.fabMap);
        fabQR = (FloatingActionButton) findViewById(R.id.fabQR);
        fabScanner = (FloatingActionButton) findViewById(R.id.fabScanner);

        fabMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fabMenu.close(true);
                //someshit

            }
        });
        fabQR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fabMenu.close(true);
                final QRDialogueFragment QRFragment = new QRDialogueFragment();
                QRFragment.show(fragmentManager, "fragment_qrdialogue");
            }
        });
        fabScanner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fabMenu.close(true);
                //someshit

            }
        });
        /* ===== /FLOATING ACTION BUTTON ===== */

    }

    public void onLogoutClick() {
        SharedPreferencesUtility.setAuthToken(MainActivity.this, "");
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
        finish();
    }
}