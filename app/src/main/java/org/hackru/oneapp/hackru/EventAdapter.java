package org.hackru.oneapp.hackru;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.hackru.oneapp.hackru.api.model.Event;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Sean on 12/11/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context;
    private List<Event> eventList;

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.events_card_layout, null);
        EventViewHolder holder = new EventViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = eventList.get(position);


        String finalDate = event.time;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        try {
            Date date = format.parse(event.time);
            SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a 'on' EEEE");
            finalDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.startTime.setText(finalDate);
        holder.title.setText(event.title);
        if(event.message != null) {
            holder.message.setText(event.message);
        } else {
            holder.message.setVisibility(View.GONE);
        }

        if (event.place != null) {
            holder.location.setText("Where: " + event.place);
        } else {
            holder.location.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView startTime, message, title, location;

        public EventViewHolder(View itemView) {
            super(itemView);
            startTime = (TextView) itemView.findViewById(R.id.startTime);
            message = (TextView) itemView.findViewById(R.id.message);
            title = (TextView) itemView.findViewById(R.id.title);
            location = (TextView) itemView.findViewById(R.id.location);

        }
    }

}
