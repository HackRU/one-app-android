package org.hackru.oneapp.hackru.ui.main.events

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.rv_item_events.view.*
import org.hackru.oneapp.hackru.R
import org.hackru.oneapp.hackru.api.models.EventsModel


class EventsAdapter : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    var items: List<EventsModel.Event> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.rv_item_events, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTitle.text = items[i].title
        viewHolder.itemDetail.text = items[i].details
        viewHolder.itemTime.text = items[i].time
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTime: TextView = itemView.item_time
        val itemTitle: TextView = itemView.item_title
        val itemDetail: TextView = itemView.item_detail
    }
}