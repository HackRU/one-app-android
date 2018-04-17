package org.hackru.oneapp.hackru;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.hackru.oneapp.hackru.api.model.Announcement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Sean on 12/11/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.AnnouncementViewHolder> {

    private Context context;
    private List<JsonObject> eventList;

    public EventAdapter(Context context, List<JsonObject> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public AnnouncementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.announcements_card_layout, null);
        AnnouncementViewHolder holder = new AnnouncementViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AnnouncementViewHolder holder, int position) {
        JsonObject event = eventList.get(position);


//        holder.date.setText(date);
//        holder.message.setText(message);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        TextView date, message;

        public AnnouncementViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            message = (TextView) itemView.findViewById(R.id.message);
        }
    }

}
