package org.hackru.oneapp.hackru.ui.main.announcements

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.rv_item_announcement.view.*
import org.hackru.oneapp.hackru.R
import org.hackru.oneapp.hackru.api.models.AnnouncementsModel

class AnnouncementsAdapter : RecyclerView.Adapter<AnnouncementsAdapter.ViewHolder>() {
    val TAG = "AnnouncementsAdapter"

    var items: List<AnnouncementsModel.Announcement> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var test = arrayOf("<!channel> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce interdum porttitor turpis quis tincidunt.",
            "<!channel2> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce interdum porttitor turpis quis tincidunt.",
            "<CD45SRT> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce interdum porttitor turpis quis tincidunt.",
            "<CD367TY> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce interdum porttitor turpis quis tincidunt.",
    "<CD89DFT> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce interdum porttitor turpis quis tincidunt.",
    "<CD45SRT> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce interdum porttitor turpis quis tincidunt.",
    "<CD35KPF> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce interdum porttitor turpis quis tincidunt.",
    "<CD26ALP> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce interdum porttitor turpis quis tincidunt.",
    "<CD29IYR> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce interdum porttitor turpis quis tincidunt.")

    override fun getItemCount() = items.size
//    override fun getItemCount() = test.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_announcement, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var s = MessageParser.stringParser(items[position].text)
        holder.announcement_text.text = s
//        holder.announcement_text.text = items[position].text    ORIGINAL_ONE
//        holder.announcement_text.text = test[position]
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val announcement_text: TextView = itemView.announcement_text
    }
}
