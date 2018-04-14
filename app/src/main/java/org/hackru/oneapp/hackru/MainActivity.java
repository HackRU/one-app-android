package org.hackru.oneapp.hackru;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
//import android.support.design.widget.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.hackru.oneapp.hackru.api.model.Login;
import org.hackru.oneapp.hackru.api.model.ReadRequest;
import org.hackru.oneapp.hackru.api.service.HackRUService;
import org.hackru.oneapp.hackru.utils.SharedPreferencesUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements QRDialogueFragment.OnLogoutClickListener {
    String TAG = "MainActivity";

    FloatingActionMenu fabMenu;
    FloatingActionButton fabLogout, fabMap, fabQR, fabScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // END BOILERPLATE CODE

        /* ===== SET PERMISSIONS ===== */
        Gson gson = new Gson();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://m7cwj1fy7c.execute-api.us-west-2.amazonaws.com/mlhtest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HackRUService hackRUService = retrofit.create(HackRUService.class);
        String email = SharedPreferencesUtility.getEmail(this);
        ReadRequest request = new ReadRequest(email);
        hackRUService.read(request).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.body().get("statusCode").getAsInt() == 200) {
                    Log.e(TAG, "Permission post submitted to API!");
                    JsonObject body = response.body();
                    if(body.getAsJsonArray("body").get(0).getAsJsonObject().get("role").getAsJsonObject().get("organizer").getAsBoolean() || body.getAsJsonArray("body").get(0).getAsJsonObject().get("role").getAsJsonObject().get("director").getAsBoolean()) {
                        SharedPreferencesUtility.setPermission(MainActivity.this, true);
                        fabScanner.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Unable to assess scanner permissions", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Unable to assess scanner permissions", Toast.LENGTH_LONG).show();
            }
        });
        /* ===== /SET PERMISSIONS ===== */


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
        //TODO: Fix buggy closing animation when timer fragment is selected
        fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        fabLogout = (FloatingActionButton) findViewById(R.id.fabLogout);
        fabMap = (FloatingActionButton) findViewById(R.id.fabMap);
        fabQR = (FloatingActionButton) findViewById(R.id.fabQR);
        fabScanner = (FloatingActionButton) findViewById(R.id.fabScanner);

        if(SharedPreferencesUtility.getPermission(this)) {
            fabScanner.setVisibility(View.VISIBLE);
        }

        fabMenu.setClosedOnTouchOutside(true);

        final QRDialogueFragment QRFragment = new QRDialogueFragment();
        final MapDialogueFragment mapFragment = new MapDialogueFragment();
        final ScannerDialogueFragment scannerFragment = new ScannerDialogueFragment();

        fabLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fabMenu.close(true);
                onLogoutClick();
            }
        });
        fabMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fabMenu.close(true);
                mapFragment.show(fragmentManager, "fragment_mapdialogue");
            }
        });
        fabQR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fabMenu.close(true);
                QRFragment.show(fragmentManager, "fragment_qrdialogue");
            }
        });
        fabScanner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fabMenu.close(true);
                scannerFragment.show(fragmentManager, "fragment_scannerdialogue");
            }
        });
        /* ===== /FLOATING ACTION BUTTON ===== */

    }

    public void onLogoutClick() {
        SharedPreferencesUtility.setAuthToken(MainActivity.this, "");
        SharedPreferencesUtility.setEmail(MainActivity.this, "");
        SharedPreferencesUtility.setPermission(MainActivity.this, false);
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
        finish();
    }
}