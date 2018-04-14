package org.hackru.oneapp.hackru;

import org.hackru.oneapp.hackru.api.model.Announcement;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;



/**
 * Created by Sean on 12/11/2017.
 */

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {

    private Context context;
    private List<Announcement> announcementList;

    public AnnouncementAdapter(Context context, List<Announcement> announcementsList) {
        this.context = context;
        this.announcementList = announcementsList;
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
        Announcement announcement = announcementList.get(position);
        holder.date.setText(announcement.getUser());
        holder.message.setText(announcement.getText());
    }

    @Override
    public int getItemCount() {
        return announcementList.size();
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
