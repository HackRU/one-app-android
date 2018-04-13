package org.hackru.oneapp.hackru;

import org.hackru.oneapp.hackru.api.model.Announcement;
import org.hackru.oneapp.hackru.api.service.HackRUService;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AnnouncementsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AnnouncementAdapter adapter;
    private List<Announcement> announcementList;
    final String BASE_URL = "https://m7cwj1fy7c.execute-api.us-west-2.amazonaws.com/mlhtest/";

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
        announcementList = new ArrayList<Announcement>();
        recyclerView = (RecyclerView) ((MainActivity)getActivity()).findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //getActivity() should get the activity's context? Instead of arguing "this"
        updateCards();


        //Fetch Announcements
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HackRUService hackRUService = retrofit.create(HackRUService.class);
        Call<List<Announcement>> getAnnouncements = hackRUService.getAnnouncements();
        getAnnouncements.enqueue(new Callback<List<Announcement>>() {

            @Override
            public void onResponse(Call<List<Announcement>> call, Response<List<Announcement>> response) {
                if(response.isSuccessful()) {
                    Log.i("Announcements", "Get request successful");
                    announcementList = response.body();
                    for(Announcement a : announcementList) {
                        Log.i("Annc.", a.getText());
                    }

                    updateCards();
                } else {
                    Log.i("Announcements", "Bad response");
                }
            }

            @Override
            public void onFailure(Call<List<Announcement>> call, Throwable t) {
                Log.i("Announcements", "Get request failed");
            }
        });
    }

    public void updateCards() {
        adapter = new AnnouncementAdapter(getActivity(), announcementList); //getActivity() should get the activity's context? Instead of arguing "this"
        recyclerView.setAdapter(adapter);
    }
}
