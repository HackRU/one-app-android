package org.hackru.oneapp.hackru.ui.main.announcements

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
<<<<<<< HEAD
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
=======
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
>>>>>>> d69d810010cf862c0eddee67b3a7d58691f5f572
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_announcements.*
import org.hackru.oneapp.hackru.HackRUApp
import org.hackru.oneapp.hackru.R
import org.hackru.oneapp.hackru.api.Resource
<<<<<<< HEAD
=======
import org.hackru.oneapp.hackru.ui.main.MainActivity
>>>>>>> d69d810010cf862c0eddee67b3a7d58691f5f572
import javax.inject.Inject

class AnnouncementsFragment : android.support.v4.app.Fragment() {
    // TAG is used with Android's Log class to organize debugging logs
    val TAG = "AnnouncementsFragment"
<<<<<<< HEAD
    lateinit var viewModel: AnnouncementsViewModel
    @Inject
    lateinit var viewModelFactory: AnnouncementsViewModelFactory
=======
    private lateinit var viewModel: AnnouncementsViewModel
    @Inject lateinit var viewModelFactory: AnnouncementsViewModelFactory
    private lateinit var mainActivity: MainActivity
    private val announcementsAdapter = AnnouncementsAdapter()

>>>>>>> d69d810010cf862c0eddee67b3a7d58691f5f572

    companion object {
        // Wondering why we use newInstance() instead of a constructor? Read this: https://stackoverflow.com/a/30867846/9968228
        fun newInstance() = AnnouncementsFragment()
    }

<<<<<<< HEAD
=======
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // TODO: Guarantee non-null activity
        mainActivity = context as MainActivity
        (mainActivity.application as HackRUApp).appComponent.inject(this)
    }

>>>>>>> d69d810010cf862c0eddee67b3a7d58691f5f572
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_announcements, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
<<<<<<< HEAD
        // TODO: Move to onAttach?
        (activity?.application as HackRUApp).appComponent.inject(this)

        val announcementsAdapter = AnnouncementsAdapter()
=======

        progressbar_announcements.isIndeterminate = true
>>>>>>> d69d810010cf862c0eddee67b3a7d58691f5f572
        rv_announcements.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = announcementsAdapter
            // TODO: Use a decorator instead here instead of margin in rv_item_announcement.xml
        }

<<<<<<< HEAD
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AnnouncementsViewModel::class.java)
=======
        viewModel = ViewModelProviders.of(mainActivity, viewModelFactory).get(AnnouncementsViewModel::class.java)
>>>>>>> d69d810010cf862c0eddee67b3a7d58691f5f572
        viewModel.announcements.observe(this, Observer {
            it?.let {
                when(it.state) {
                    Resource.LOADING -> {
                        // The resource is loading
<<<<<<< HEAD
                        // TODO: Implement progress bar with the android arch components data binding library
                    }
                    Resource.SUCCESS -> {
                        // The resource has successfully been fetched
                        announcementsAdapter.items = it.data!!
                        // TODO: Find a way where Resource.success() cannot hold null to avoid having to use "!!"
=======
                        // TODO: Implement android arch data binding library
                        progressbar_announcements.visibility = View.VISIBLE
                    }
                    Resource.SUCCESS -> {
                        // The resource has successfully been fetched
                        progressbar_announcements.visibility = View.GONE
                        announcementsAdapter.items = it.data!!
>>>>>>> d69d810010cf862c0eddee67b3a7d58691f5f572
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