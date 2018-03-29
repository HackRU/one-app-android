package org.hackru.oneapp.hackru;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class AnnouncementsFragment extends Fragment {

    RecyclerView recyclerView;
    AnnouncementAdapter adapter;
    List<Announcement> announcementList;

    public AnnouncementsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_announcements, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Treat this as onCreate()
        announcementList = new ArrayList<>();
        recyclerView = (RecyclerView) ((MainActivity)getActivity()).findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //getActivity() should get the activity's context? Instead of arguing "this"

        //Insert creation of cards
        announcementList.add(
                new Announcement("December 11th at 7:11PM", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed efficitur neque libero, vitae dapibus velit egestas at."));
        announcementList.add(
                new Announcement("December 11th at 6:51PM", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed efficitur neque libero, vitae dapibus velit egestas at."));
        announcementList.add(
                new Announcement("December 11th at 6:34PM", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed efficitur neque libero, vitae dapibus velit egestas at."));
        announcementList.add(
                new Announcement("December 11th at 6:03PM", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed efficitur neque libero, vitae dapibus velit egestas at."));
        announcementList.add(
                new Announcement("December 11th at 5:39PM", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed efficitur neque libero, vitae dapibus velit egestas at."));
        announcementList.add(
                new Announcement("December 11th at 5:24PM", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed efficitur neque libero, vitae dapibus velit egestas at."));
        announcementList.add(
                new Announcement("December 11th at 4:46PM", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed efficitur neque libero, vitae dapibus velit egestas at."));
        announcementList.add(
                new Announcement("December 11th at 4:27PM", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed efficitur neque libero, vitae dapibus velit egestas at."));
        announcementList.add(
                new Announcement("December 11th at 4:20PM", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed efficitur neque libero, vitae dapibus velit egestas at."));
        announcementList.add(
                new Announcement("December 11th at 3:32PM", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed efficitur neque libero, vitae dapibus velit egestas at."));
        announcementList.add(
                new Announcement("December 11th at 3:14PM", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed efficitur neque libero, vitae dapibus velit egestas at."));
        announcementList.add(
                new Announcement("December 11th at 2:53PM", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed efficitur neque libero, vitae dapibus velit egestas at."));

        adapter = new AnnouncementAdapter(getActivity(), announcementList); //getActivity() should get the activity's context? Instead of arguing "this"
        recyclerView.setAdapter(adapter);

    }
}
