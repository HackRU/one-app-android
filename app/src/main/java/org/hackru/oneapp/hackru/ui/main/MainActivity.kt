package org.hackru.oneapp.hackru.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_drawer.*
import net.glxn.qrgen.android.QRCode
import org.hackru.oneapp.hackru.R
import org.hackru.oneapp.hackru.Utils
import org.hackru.oneapp.hackru.ui.drawer.LoginActivity
import org.hackru.oneapp.hackru.ui.main.announcements.AnnouncementsFragment
import org.hackru.oneapp.hackru.ui.main.events.EventsFragment
import org.hackru.oneapp.hackru.ui.drawer.scanner.MaterialBarcodeScanner
import org.hackru.oneapp.hackru.ui.drawer.scanner.MaterialBarcodeScannerBuilder
import org.hackru.oneapp.hackru.ui.main.timer.TimerFragment

class MainActivity : AppCompatActivity() {
    // TAG is used with Android's Log class to organize debugging logs
    val TAG = "MainActivity"

    var email: String? = null
    var canScan = false
    var authToken: String? = null
    var name: String = ""
    var logoutAt: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: Logout user after authtoken expires

        getUserInfo()
        setUpActionBar()
        setUpNavigationDrawer()
        setUpBottomNavigation(savedInstanceState)
        setUpFloatingActionButton()
    }

    override fun onResume() {
        super.onResume()
        getUserInfo()
        setUpNavigationDrawer()
    }

    fun getUserInfo() {
        email = Utils.SharedPreferences.getEmail(this)
        canScan = Utils.SharedPreferences.getCanScan(this)
        authToken = Utils.SharedPreferences.getAuthToken(this)
        name = Utils.SharedPreferences.getName(this) ?: ""
        logoutAt = Utils.SharedPreferences.getLogoutAt(this)
        if(logoutAt != 0L) {
            checkSession()
        }
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

    fun setUpNavigationDrawer() {

        // Show/hide the scanner if the user does/doesn't have permission to use it
        drawer_navigation.menu.findItem(R.id.drawer_scanner).isVisible = canScan

        // Show/hide the login and logout buttons depending if the user is logged in or logged out
        drawer_navigation.menu.findItem(R.id.drawer_login).isVisible = (authToken == null)
        drawer_navigation.menu.findItem(R.id.drawer_logout).isVisible = (authToken != null)

        // Change the name/email in the header
        val header: View = drawer_navigation.getHeaderView(0)
        var headerEmail = getString(R.string.navigation_drawer_email_default)
        var headerName = name
        if(authToken != null) {
            headerEmail = email
            if(name.isEmpty()) {
                headerName = "HackRU Fall 2018"
            }
        } else {
            headerName = getString(R.string.navigation_drawer_name_default)
        }

        header.findViewById<TextView>(R.id.header_drawer_name).text = headerName
        header.findViewById<TextView>(R.id.header_drawer_email).text = headerEmail

        // Listen for when the user clicks on one of the navigation drawer items
        drawer_navigation.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.drawer_map -> {

                }
                R.id.drawer_scanner -> {
                    if(checkSession()) {
                        val qrCodeScanning:MaterialBarcodeScanner = MaterialBarcodeScannerBuilder()
                                .withActivity(this)
                                .withEnableAutoFocus(true)
                                .withCenterTracker()
                                .withBackfacingCamera()
                                .build()
                        qrCodeScanning.startScan()
                    }
                }
                R.id.drawer_about -> {

                }
                R.id.drawer_login -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                R.id.drawer_logout -> {
                    val alertDialog = AlertDialog.Builder(this)
                            .setCancelable(true)
                            .setTitle("Are you sure you want to logout?")
                            .setPositiveButton("Logout") { dialogInterface, i ->
                                logout()
                                Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("Cancel") { dialogInterface, i ->
                                // Do nothing. The dialog will close by default
                            }
                            .create()
                    // Now let's change the color of the buttons from colorAccent to colorPrimary
                    alertDialog.setOnShowListener { _ ->
                        val colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary)
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(colorPrimary)
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(colorPrimary)
                    }
                    alertDialog.show()
                }
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }
    }

    fun setUpBottomNavigation(savedInstanceState: Bundle?) {
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
                    toggleActionBarElevation(true)
                    switchFragment(AnnouncementsFragment.newInstance())
                    true
                }
                R.id.bottom_navigation_timer -> {
                    // User clicked on Timer
                    toggleActionBarElevation(true)
                    switchFragment(TimerFragment.newInstance())
                    true
                }
                R.id.bottom_navigation_events -> {
                    // User clicked on Events
                    toggleActionBarElevation(false)
                    switchFragment(EventsFragment.newInstance())
                    true
                }
                else -> false
            }
        }
    }

    fun setUpFloatingActionButton() {
        // Listen for when the user clicks on the qr code floating action button
        fab_qr.setOnClickListener {
            if(logoutAt != 0L && !checkSession()) {
                // If they are logged-in, but their session has expired, open the LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
            } else if(authToken != null) {
                // If they are logged-in, show their QR code in an AlertDialog
                val dialogView = layoutInflater.inflate(R.layout.dialog_qr_code, null)
                val alertDialog = AlertDialog.Builder(this)
                        .setView(dialogView)
                        .setCancelable(true)
                        .create()
                alertDialog.show()
                val codeSize = resources.getDimensionPixelSize(R.dimen.qr_code_size)
                val dialogSize = resources.getDimensionPixelSize(R.dimen.qr_dialog_size)
                alertDialog.window?.setLayout(dialogSize, dialogSize)
                val qrCode: Bitmap = QRCode.from(email)
                        .withColor(ContextCompat.getColor(this, R.color.colorPrimary), Color.WHITE)
                        .withSize(codeSize, codeSize)
                        .bitmap()
                dialogView.findViewById<ImageView>(R.id.image_qr_code).setImageBitmap(qrCode)
            } else {
                // If they are not logged-in, open LoginActivity
                Toast.makeText(this, "You must be logged in to access your ticket", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    fun checkSession(): Boolean {
        val currentTime: Long = System.currentTimeMillis()
        if(currentTime > logoutAt) {
            logout()
            Toast.makeText(this, "Your session has expired and you have been logged out", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    fun logout() {
        authToken = null
        canScan = false
        Utils.SharedPreferences.setAuthToken(this, null)
        Utils.SharedPreferences.setCanScan(this, false)
        Utils.SharedPreferences.setName(this, null)
        setUpNavigationDrawer()
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

    /**
     * Toggles elevation on the supportActionBar. This is needed because the supportActionBar shouldn't
     * have any elevation when the user is on EventsFragment, but it should have elevation otherwise.
     *
     * @param toggle Whether or not the supportActionBar should have elevation. True is yes, false is no
     */
    fun toggleActionBarElevation(toggle: Boolean) {
        if(Build.VERSION.SDK_INT >= 21) {
            supportActionBar?.elevation = run {
                if(toggle) Utils.convertDpToPx(this, 4f)
                else 0f
            }
        }
    }

}