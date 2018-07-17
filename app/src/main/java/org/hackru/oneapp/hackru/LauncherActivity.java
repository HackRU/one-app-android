package org.hackru.oneapp.hackru;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.hackru.oneapp.hackru.utils.SharedPreferencesUtility;

public class LauncherActivity extends AppCompatActivity {
    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        if(SharedPreferencesUtility.getAuthToken(LauncherActivity.this).length() == 0) {
            // If the user has never logged in, make them log in
            intent = new Intent(this, LoginActivity.class);
        } else {
            // If the user has logged in before
            intent = new Intent(this, MainActivity.class);
        }

        // TODO: Is this really necessary? If SharedPreferences is async, this should be changed
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
