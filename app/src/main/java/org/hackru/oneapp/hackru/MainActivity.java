package org.hackru.oneapp.hackru;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
//import android.support.design.widget.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.hackru.oneapp.hackru.api.model.ReadRequest;
import org.hackru.oneapp.hackru.api.service.HackRUService;
import org.hackru.oneapp.hackru.utils.SharedPreferencesUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";

    FloatingActionMenu fabMenu;
    FloatingActionButton fabLogout, fabMap, fabQR, fabScanner;

    private static final int RC_BARCODE_CAPTURE = 9001;

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
        //TODO: Fix buggy closing animation when timer fragment is selected
        fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        fabLogout = (FloatingActionButton) findViewById(R.id.fabLogout);
        fabMap = (FloatingActionButton) findViewById(R.id.fabMap);
        fabQR = (FloatingActionButton) findViewById(R.id.fabQR);
        fabScanner = (FloatingActionButton) findViewById(R.id.fabScanner);

        // So the scanner knows whether or not to show when offline
        if(SharedPreferencesUtility.getPermission(this)) {
            if(fabMenu.isOpened()) {
                fabScanner.setVisibility(View.VISIBLE);
            } else {
                fabScanner.setVisibility(View.INVISIBLE);
            }
        }

        fabMenu.setClosedOnTouchOutside(true);

        final QRDialogueFragment QRFragment = new QRDialogueFragment();
        final MapDialogueFragment mapFragment = new MapDialogueFragment();



        fabLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fabMenu.close(true);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AppTheme_Dialogue_Alert);
                builder.setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                onLogoutClick();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        }).create().show();
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
                // launch barcode activity.
                Intent intent = new Intent(MainActivity.this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
        });
        /* ===== /FLOATING ACTION BUTTON ===== */

        /* ===== FIREBASE STUFF ===== */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference().child("mapEnabled");;
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again whenever data at this location is updated.
                boolean enabled = dataSnapshot.getValue(Boolean.class);
                Log.e(TAG, "Map enabled is " + enabled);
                if(enabled) {
                    if (fabMenu.isOpened()) {
                        fabMap.setVisibility(View.VISIBLE);
                    } else {
                        fabMap.setVisibility(View.INVISIBLE);
                    }
                } else {
                    fabMap.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        /* ===== /FIREBASE STUFF ===== */

        /* ===== SET PERMISSIONS ===== */
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
                    Log.i(TAG, "Permission post submitted to API!");
                    JsonObject body = response.body();
                    boolean enabled = body.getAsJsonArray("body").get(0).getAsJsonObject().get("role").getAsJsonObject().get("organizer").getAsBoolean() || body.getAsJsonArray("body").get(0).getAsJsonObject().get("role").getAsJsonObject().get("director").getAsBoolean();
                    if(enabled) {
                        SharedPreferencesUtility.setPermission(MainActivity.this, true);
                        if(fabMenu.isOpened()) {
                            fabScanner.setVisibility(View.VISIBLE);
                        } else {
                            fabScanner.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        SharedPreferencesUtility.setPermission(MainActivity.this, false);
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Unable to reach permissions API", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Unable to reach permissions API", Toast.LENGTH_LONG).show();
            }
        });
        /* ===== /SET PERMISSIONS ===== */


    }

    public void onLogoutClick() {
        SharedPreferencesUtility.setAuthToken(MainActivity.this, "");
        SharedPreferencesUtility.setEmail(MainActivity.this, "");
        SharedPreferencesUtility.setPermission(MainActivity.this, false);
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
//                    statusMessage.setText(R.string.barcode_success);
//                    barcodeValue.setText(barcode.displayValue);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
//                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
//                statusMessage.setText(String.format(getString(R.string.barcode_error),
//                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}