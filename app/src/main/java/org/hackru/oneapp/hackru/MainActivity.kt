package org.hackru.oneapp.hackru

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // TAG is used with Android's Log class to organize debugging logs
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set Timer as the default tab when the app is opened for the first time in a while or was
        // terminated manually by the user
        if (savedInstanceState == null) {
            bottom_navigation.selectedItemId = R.id.bottom_navigation_timer
            switchFragment(TimerFragment.newInstance())
        }
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_navigation_announcements -> {
                    // User clicked on Announcements
                    switchFragment(AnnouncementsFragment.newInstance())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottom_navigation_timer -> {
                    // User clicked on Timer
                    switchFragment(TimerFragment.newInstance())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottom_navigation_events -> {
                    // User clicked on Events
                    switchFragment(EventsFragment.newInstance())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

    /**
     * Shows a fragment inside MainActivity. The SupportFragmentManager handles recycling the old
     * fragment and swapping in the new one.
     *
     * @param fragment The fragment you are trying to display. It must extend android.support.v4.app.Fragment
     * and not android.app.Fragment since we are supporting back to API level 19 (KitKat)
     */
    fun switchFragment(fragment: android.support.v4.app.Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }
}
