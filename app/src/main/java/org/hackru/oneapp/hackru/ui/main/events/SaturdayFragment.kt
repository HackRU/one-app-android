package org.hackru.oneapp.hackru.ui.main.events

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_announcements.*
import kotlinx.android.synthetic.main.fragment_saturday_sunday.*
import org.hackru.oneapp.hackru.R
import org.hackru.oneapp.hackru.api.Resource
import org.hackru.oneapp.hackru.ui.main.MainActivity

class SaturdayFragment : Fragment() {
    // TAG is used with Android's Log class to organize debugging logs
    val TAG = "SaturdayFragment"
    private lateinit var viewModel: EventsViewModel
    private val eventsAdapter = EventsAdapter()


    companion object {
        // Wondering why we use newInstance() instead of a constructor? Read this: https://stackoverflow.com/a/30867846/9968228
        fun newInstance() = SaturdayFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_saturday_sunday, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rv_events.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventsAdapter
            // TODO: Use a decorator instead here instead of margin in rv_item_announcement.xml
        }

        viewModel = ViewModelProviders.of(activity as MainActivity).get(EventsViewModel::class.java)
        viewModel.saturdayEvents.observe(this, Observer {
            it?.let {
                when(it.state) {
                    Resource.LOADING -> {
                        // The resource is loading
                    }
                    Resource.SUCCESS -> {
                        // The resource has successfully been fetched
                        eventsAdapter.items = it.data!!
                    }
                    Resource.FAILURE -> {
                        // There was an error fetching the resource
                        Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

}