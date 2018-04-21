package org.hackru.oneapp.hackru;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    String TAG = "EventAdapter";

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
//        Log.e(TAG, "title["+position+"] = "+event.title);
//        Log.e(TAG, "message["+position+"] = "+event.message);
//        Log.e(TAG, "time["+position+"] = "+event.time);
//        Log.e(TAG, "place["+position+"] = "+event.place);

        String finalDate = event.time;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        try {
            Date dateStart = format.parse(event.time);
            Date dateEnd = format.parse(event.endTime);
//            Log.e(TAG, ""+dateStart.getTime());
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            StringBuilder sb = new StringBuilder();
            if (event.time.equals(event.endTime)) {
                sb.append(timeFormat.format(dateStart)).append(" on ").append(dayFormat.format(dateStart));
                finalDate = sb.toString();
            } else {
                sb.append(timeFormat.format(dateStart)).append(" - ").append(timeFormat.format(dateEnd)).append(" on ").append(dayFormat.format(dateStart));
//            finalDate = dayFormat.format(dateStart);
                finalDate = sb.toString();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }



        holder.startTime.setText(finalDate);
        holder.title.setText(event.title);
        if(event.place != null && event.place.length() > 0) {
//            Log.e(TAG, ""+event.place);
            holder.location.setVisibility(View.VISIBLE);
            holder.location.setText("Where: " + event.place);
        } else {
            holder.location.setVisibility(View.GONE);
        }
        if(event.message != null && event.message.length() > 0) {
//            Log.e(TAG, ""+event.message);
            holder.message.setVisibility(View.VISIBLE);
            holder.message.setText(event.message);
        } else {
            holder.message.setVisibility(View.GONE);
        }

//        if(holder.message.getText() == null || holder.message.getText().length() == 0) holder.message.setVisibility(View.GONE);
//

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
