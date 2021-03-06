package org.hackru.oneapp.hackru.ui.main.events

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_saturday_sunday.*
import org.hackru.oneapp.hackru.HackRUApp
import org.hackru.oneapp.hackru.R
import org.hackru.oneapp.hackru.api.Resource
import org.hackru.oneapp.hackru.ui.main.MainActivity
import javax.inject.Inject

class SundayFragment : Fragment() {
    // TAG is used with Android's Log class to organize debugging logs
    val TAG = "SundayFragment"
    private lateinit var viewModel: EventsViewModel
    private val eventsAdapter = EventsAdapter()
    private lateinit var mainActivity: MainActivity
    @Inject
    lateinit var viewModelFactory: EventsViewModelFactory


    companion object {
        // Wondering why we use newInstance() instead of a constructor? Read this: https://stackoverflow.com/a/30867846/9968228
        fun newInstance() = SundayFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        (mainActivity.application as HackRUApp).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_saturday_sunday, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rv_events.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventsAdapter
        }

        viewModel = ViewModelProviders.of(mainActivity, viewModelFactory).get(EventsViewModel::class.java)
        viewModel.events?.observe(this, Observer {
            it?.let {
                when(it.state) {
                    Resource.LOADING -> {
                        // The resource is loading
                        progressbar_events.visibility = View.VISIBLE
                        error_message.visibility = View.GONE
                    }
                    Resource.SUCCESS -> {
                        // The resource has successfully been fetched
                        progressbar_events.visibility = View.GONE
                        error_message.visibility = View.GONE
                        eventsAdapter.items = it.data[1]
                    }
                    Resource.FAILURE -> {
                        // There was an error fetching the resource
                        progressbar_events.visibility = View.GONE
                        if(eventsAdapter.items.isEmpty()) {
                            error_message.visibility = View.VISIBLE
                            error_message.text = it.msg
                        } else {
                            Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }

}