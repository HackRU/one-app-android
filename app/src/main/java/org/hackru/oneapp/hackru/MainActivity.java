package org.hackru.oneapp.hackru;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.hackru.oneapp.hackru.utils.SharedPreferencesUtility;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottom_navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        if(savedInstanceState == null) {
            bottom_navigation.setSelectedItemId(R.id.bottom_navigation_timer);
        }
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_main, fragment)
                .commit();
    }

    private void onLogoutClick() {
        SharedPreferencesUtility.setAuthToken(this, "");
        SharedPreferencesUtility.setEmail(this, "");
        SharedPreferencesUtility.setPermission(this, false);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}