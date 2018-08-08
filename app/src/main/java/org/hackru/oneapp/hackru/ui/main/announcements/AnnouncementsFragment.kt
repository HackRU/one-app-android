package org.hackru.oneapp.hackru.ui.main.announcements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import org.hackru.oneapp.hackru.R

class AnnouncementsFragment : android.support.v4.app.Fragment() {
    // TAG is used with Android's Log class to organize debugging logs
    val TAG = "AnnouncementsFragment"

    companion object {
        // Wondering why we use newInstance() instead of a constructor? Read this: https://stackoverflow.com/a/30867846/9968228
        fun newInstance() = AnnouncementsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_announcements, container, false)

}