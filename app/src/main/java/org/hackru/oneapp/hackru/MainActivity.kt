package org.hackru.oneapp.hackru

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.hackru.oneapp.hackru.utils.SharedPreferencesUtility
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    internal var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            bottom_navigation.selectedItemId = R.id.bottom_navigation_timer
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_main, fragment)
                .commit()
    }

    private fun onLogoutClick() {
        SharedPreferencesUtility.setAuthToken(this, "")
        SharedPreferencesUtility.setEmail(this, "")
        SharedPreferencesUtility.setPermission(this, false)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}