package org.hackru.oneapp.hackru.ui.main.events

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.ViewGroup
import org.hackru.oneapp.hackru.R
import android.view.View

class EventsFragment : Fragment() {
    val TAG = "EventsFragment"
    lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    companion object {
        fun newInstance() = EventsFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_events, container, false)

        viewPager = view?.findViewById(R.id.container) as ViewPager
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPager.adapter = viewPagerAdapter
        return rootView
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
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tabTitles[position]
        }


    }

}
