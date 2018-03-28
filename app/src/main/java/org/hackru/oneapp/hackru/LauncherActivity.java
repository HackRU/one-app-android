package org.hackru.oneapp.hackru;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class LauncherActivity extends AppCompatActivity {
    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        // END BOILERPLATE CODE

        if(SharedPreferencesUtility.getAuthToken(LauncherActivity.this).length() == 0) { // If the user has never logged in, make them log in
            intent = new Intent(this, LoginActivity.class);
        } else { // If the user has logged in before (still waiting on validate from Heman)
            intent = new Intent(this, HackerActivity.class);
        }

        // TODO: FIX BUG WHERE APP CRASHES FROM INFINITE LOOP IF LOCATION PERMISSION IS DENIED
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LauncherActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            while(true) {
                if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    break;
                }
                ActivityCompat.requestPermissions(LauncherActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(intent != null) {
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "Launcher activity error", Toast.LENGTH_LONG).show();
                            intent = new Intent(LauncherActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, 500);
    }
}
