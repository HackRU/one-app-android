package org.hackru.oneapp.hackru.ui.main.events

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import org.hackru.oneapp.hackru.R
import android.view.View
import kotlinx.android.synthetic.main.fragment_events.*
import org.hackru.oneapp.hackru.ui.main.MainActivity

class EventsFragment : Fragment() {
    val TAG = "EventsFragment"

    private val TIME_SUNDAY_MIDNIGHT: Long = 1538870400000

    companion object {
        fun newInstance() = EventsFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as MainActivity).supportActionBar?.title = getString(R.string.title_events)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        container_events.adapter = ViewPagerAdapter(childFragmentManager)
        container_events.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container_events))

        // If it is Sunday, then show the Sunday tab. Else show the Saturday tab
        val timeDiff = System.currentTimeMillis() - TIME_SUNDAY_MIDNIGHT
        if(timeDiff > 1000 * 60 * 60 * 24 ) {
            container_events.currentItem = 1
        }
    }

    internal inner class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        var tabTitles = arrayOf("SATURDAY", "SUNDAY")

        override fun getItem(position: Int): Fragment? {

            when (position) {
                0 -> return SaturdayFragment()
                1 -> return SundayFragment()
            }
            return null
        }

        override fun getCount(): Int {
            return tabTitles.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tabTitles[position]
        }

    }

}

