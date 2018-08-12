package org.hackru.oneapp.hackru.ui.main.events

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.rv_item_events.view.*
import org.hackru.oneapp.hackru.R


class EventsAdapter : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    private val titles = arrayOf("Check-In", "Opening Ceremonies", "Lunch",
            "Keynote Speaker", "Hacking Begins", "Team Building", "Dinner")

    private val details = arrayOf("Welcome to HackRU Fall 2018! Have a good one!",
            "Welcome to HackRU Fall 2018! Have a good three!", "Welcome to HackRU Fall 2018! Have a good four!",
            "Welcome to HackRU Fall 2018! Have a good five!", "Welcome to HackRU Fall 2018! Have a good six!",
            "Welcome to HackRU Fall 2018! Have a good seven!", "Welcome to HackRU Fall 2018! Have a good two!")

    private val time = arrayOf("10:00 AM", "11:30 AM", "12:00 PM",
            "12:30 PM", "01:00 PM", "01:00 PM", "06:00 PM")

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemTime: TextView = itemView.item_time
        var itemTitle: TextView = itemView.item_title
        var itemDetail: TextView = itemView.item_detail

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.rv_item_events, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTitle.text = titles[i]
        viewHolder.itemDetail.text = details[i]
        viewHolder.itemTime.text = time[i]
    }

    override fun getItemCount(): Int {
        return titles.size
    }
}