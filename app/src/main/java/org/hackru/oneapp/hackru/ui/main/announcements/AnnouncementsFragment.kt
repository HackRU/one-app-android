package org.hackru.oneapp.hackru.ui.main.announcements

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_announcements.*
import org.hackru.oneapp.hackru.HackRUApp
import org.hackru.oneapp.hackru.R
import org.hackru.oneapp.hackru.api.Resource
import org.hackru.oneapp.hackru.ui.main.MainActivity
import javax.inject.Inject

class AnnouncementsFragment : android.support.v4.app.Fragment() {
    // TAG is used with Android's Log class to organize debugging logs
    val TAG = "AnnouncementsFragment"
    private lateinit var viewModel: AnnouncementsViewModel
    @Inject
    private lateinit var viewModelFactory: AnnouncementsViewModelFactory
    private lateinit var mainActiviy: MainActivity
    private val announcementsAdapter = AnnouncementsAdapter()


    companion object {
        // Wondering why we use newInstance() instead of a constructor? Read this: https://stackoverflow.com/a/30867846/9968228
        fun newInstance() = AnnouncementsFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // TODO: Guarantee non-null activity
        mainActiviy = context as MainActivity
        (mainActiviy.application as HackRUApp).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_announcements, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        progressbar_announcements.isIndeterminate = true
        rv_announcements.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = announcementsAdapter
            // TODO: Use a decorator instead here instead of margin in rv_item_announcement.xml
        }

        viewModel = ViewModelProviders.of(mainActiviy, viewModelFactory).get(AnnouncementsViewModel::class.java)
        viewModel.announcements.observe(this, Observer {
            it?.let {
                when(it.state) {
                    Resource.LOADING -> {
                        // The resource is loading
                        // TODO: Implement android arch data binding library
                        progressbar_announcements.visibility = View.VISIBLE
                    }
                    Resource.SUCCESS -> {
                        // The resource has successfully been fetched
                        progressbar_announcements.visibility = View.GONE
                        announcementsAdapter.items = it.data!!
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