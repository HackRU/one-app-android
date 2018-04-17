package org.hackru.oneapp.hackru;

import org.hackru.oneapp.hackru.api.model.Announcement;
import org.hackru.oneapp.hackru.api.model.AnnouncementsResponse;
import org.hackru.oneapp.hackru.api.service.HackRUService;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AnnouncementsFragment extends Fragment {
    String TAG = "AnnouncementFragment";

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
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onresume!!!");
        checkDatabase();
    }


    public void checkDatabase() {
        announcementList = new ArrayList<Announcement>();
        recyclerView = (RecyclerView) ((MainActivity)getActivity()).findViewById(R.id.recyclerViewAnnouncements);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //getActivity() should get the activity's context? Instead of arguing "this"
        updateCards();

        final ProgressBar loadingBar = (ProgressBar) getView().findViewById(R.id.loadingBarAnnouncements);
        loadingBar.setIndeterminate(true);
        loadingBar.setVisibility(ProgressBar.VISIBLE);

        //Fetch Announcements
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HackRUService hackRUService = retrofit.create(HackRUService.class);
        Call<AnnouncementsResponse> getAnnouncements = hackRUService.getAnnouncements();
        getAnnouncements.enqueue(new Callback<AnnouncementsResponse>() {

            @Override
            public void onResponse(Call<AnnouncementsResponse> call, Response<AnnouncementsResponse> response) {
                if(response.isSuccessful()) {
                    Log.i("Announcements", "Get request successful");
                    AnnouncementsResponse resp = response.body();
                    announcementList = resp.getBody();
                    Log.i("Announcements Response", resp.toString());
                    loadingBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    updateCards();
                } else {
                    Log.i("Announcements", "Bad response");
                    Log.i("Announcements", response.body().toString());
                    Log.i("Announcements", response.errorBody().toString());
                    Log.i("Announcements", response.message().toString());
                    loadingBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AnnouncementsResponse> call, Throwable t) {
                Log.i("Announcements", "Get request failed");
                Log.i("Announcements", t.getLocalizedMessage());
                loadingBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }
    public void updateCards() {
        adapter = new AnnouncementAdapter(getActivity(), announcementList);
        recyclerView.setAdapter(adapter);
    }


}
