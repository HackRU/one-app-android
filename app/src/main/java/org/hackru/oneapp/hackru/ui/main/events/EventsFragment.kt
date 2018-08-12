package org.hackru.oneapp.hackru.ui.main.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import org.hackru.oneapp.hackru.R
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_events.*
import kotlinx.android.synthetic.main.tablayout_events.*

class EventsFragment : android.support.v4.app.Fragment() {
    // TAG is used with Android's Log class to organize debugging logs
    val TAG = "EventsFragment"

    companion object {
        // Wondering why we use newInstance() instead of a constructor? Read this: https://stackoverflow.com/a/30867846/9968228
        val instance: EventsFragment
            get() = EventsFragment()
//        fun newInstance() = EventsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_events, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventsAdapter = EventsAdapter()
        rv_events.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventsAdapter
        }
    }
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
//        val eventsAdapter = EventsAdapter()
//        rv_events.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = eventsAdapter
//        }
//
//    }

}