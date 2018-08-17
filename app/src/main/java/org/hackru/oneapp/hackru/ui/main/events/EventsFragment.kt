package org.hackru.oneapp.hackru.ui.main.events

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import org.hackru.oneapp.hackru.R
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_events.*

class EventsFragment : Fragment() {
    val TAG = "EventsFragment"

    companion object {
        fun newInstance() = EventsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        container_events.adapter = ViewPagerAdapter(childFragmentManager)
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

