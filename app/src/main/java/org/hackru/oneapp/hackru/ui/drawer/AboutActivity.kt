package org.hackru.oneapp.hackru.ui.drawer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import org.hackru.oneapp.hackru.BuildConfig
import org.hackru.oneapp.hackru.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = getString(R.string.title_about)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Create a custom field that displays our version code
        val versionElement= Element()
        versionElement.title = "Version: ${BuildConfig.VERSION_NAME}"

        val aboutPage: View = AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher)
                .setDescription(getString(R.string.about_page_description))
                .addItem(versionElement)
                .addWebsite(getString(R.string.url_website))
                .addFacebook(getString(R.string.facebook_handle))
                .addGitHub(getString(R.string.github_handle))
                .create()
        setContentView(aboutPage)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }
}
