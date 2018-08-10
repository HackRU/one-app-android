package org.hackru.oneapp.hackru.ui.main.announcements

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_announcements.*
import org.hackru.oneapp.hackru.HackRUApp
import org.hackru.oneapp.hackru.R
import org.hackru.oneapp.hackru.api.Resource
import javax.inject.Inject

class AnnouncementsFragment : android.support.v4.app.Fragment() {
    // TAG is used with Android's Log class to organize debugging logs
    val TAG = "AnnouncementsFragment"
    lateinit var viewModel: AnnouncementsViewModel
    @Inject
    lateinit var viewModelFactory: AnnouncementsViewModelFactory

    companion object {
        // Wondering why we use newInstance() instead of a constructor? Read this: https://stackoverflow.com/a/30867846/9968228
        fun newInstance() = AnnouncementsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_announcements, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Move to onAttach?
        (activity?.application as HackRUApp).appComponent.inject(this)

        val announcementsAdapter = AnnouncementsAdapter()
        rv_announcements.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = announcementsAdapter
            // TODO: Use a decorator instead here instead of margin in rv_item_announcement.xml
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AnnouncementsViewModel::class.java)
        viewModel.announcements.observe(this, Observer {
            it?.let {
                when(it.state) {
                    Resource.LOADING -> {
                        // The resource is loading
                        // TODO: Implement progress bar with the android arch components data binding library
                    }
                    Resource.SUCCESS -> {
                        // The resource has successfully been fetched
                        announcementsAdapter.items = it.data!!
                        // TODO: Find a way where Resource.success() cannot hold null to avoid having to use "!!"
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