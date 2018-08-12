package org.hackru.oneapp.hackru

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.hackru.oneapp.hackru.R
import org.hackru.oneapp.hackru.ui.main.announcements.AnnouncementsFragment
import org.hackru.oneapp.hackru.ui.main.events.EventsFragment
import org.hackru.oneapp.hackru.ui.main.timer.TimerFragment

class MainActivity : AppCompatActivity() {
    // TAG is used with Android's Log class to organize debugging logs
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpActionBar()

        // Set Timer as the default tab when the app is opened for the first time or was terminated manually by the user
        if(savedInstanceState == null) {
            bottom_navigation.selectedItemId = R.id.bottom_navigation_timer
            switchFragment(TimerFragment.newInstance())
        }

        // Listen for when the user clicks on one of the bottom navigation items
        bottom_navigation.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when(it.itemId) {
                R.id.bottom_navigation_announcements -> {
                    // User clicked on Announcements
                    switchFragment(AnnouncementsFragment.newInstance())
                    true
                }
                R.id.bottom_navigation_timer -> {
                    // User clicked on Timer
                    switchFragment(TimerFragment.newInstance())
                    true
                }
                R.id.bottom_navigation_events -> {
                    // User clicked on Events
                    switchFragment(EventsFragment.instance)
                   true
                }
                else -> false
            }
        }

        // Listen for when the user clicks on one of the navigation drawer items
        drawer_navigation.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.drawer_map -> {

                }
                R.id.drawer_scanner -> {

                }
                R.id.drawer_settings -> {

                }
                R.id.drawer_about -> {

                }
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            true
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

    /**
     * Sets up our custom toolbar as the app's Action Bar/App Bar (the two terms are completely
     * interchangeable).
     *
     * It is best practice to use a theme without an action bar and then implement
     * a custom one because when you don't create a custom one, a native action bar is used that
     * looks different on each API level (for example, on API level 19-20 the native action bar
     * doesn't follow the Material Design spec).
     *
     * We also implement a custom action bar so that we can give it more functionality than is
     * available in native action bars, like putting a menu button on it that pulls out
     * the navigation drawer when pressed, or hiding the action bar when the user is scrolling
     * through a list.
     */
    private fun setUpActionBar() {
        setSupportActionBar(toolbar)
        val drawerToggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }

    /**
     * This method is called whenever the user presses the back button while in MainActivity.
     */
    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
            // If the navigation drawer is open, it is good UX etiquette to close the navigation
            // drawer when the user presses the back button.
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            // If the navigation drawer is already closed, then let the android system handle what
            // the back button does. In the case of MainActivity, the app will exit and MainActivity
            // will be destroyed.
            super.onBackPressed()
        }
    }
}
