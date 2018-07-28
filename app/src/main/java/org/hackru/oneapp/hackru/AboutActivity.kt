package org.hackru.oneapp.hackru

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
/*
Library used:
https://github.com/medyo/android-about-page.git
*/
class AboutActivity : AppCompatActivity() {

    private val description = "Thank you for downloading one app android. One app android is open source android app written in Kotlin. Check out our github for contributing."
    private val version="Version: 1.7.0"
    private val website = "https://www.hackru.org/"
    private val twitter= "https://twitter.com/thehackru"
    private val facebook= "https://www.facebook.com/theHackRU/"
    private val github= "https://github.com/HackRU/one-app-android"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make some custom element for version
        val versionElement= Element()
        versionElement.setTitle(version)

        val aboutPage: View = AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.hackru_logo)
                .setDescription(description)
                .addItem(versionElement)
                .addWebsite(website)
                .addFacebook(facebook)
                .addTwitter(twitter)
                .addGitHub(github)
                .create()
        setContentView(aboutPage)

    }
}
