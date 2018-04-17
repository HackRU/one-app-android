package org.hackru.oneapp.hackru;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.hackru.oneapp.hackru.api.model.Announcement;
import org.hackru.oneapp.hackru.api.model.events.Event;
import org.hackru.oneapp.hackru.api.service.HackRUService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EventsFragment extends Fragment {
    String TAG = "EventsFragment";

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<JsonObject> eventList;
    final String BASE_URL = "https://m7cwj1fy7c.execute-api.us-west-2.amazonaws.com/mlhtest/";

    public EventsFragment() {
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
        Log.e(TAG, "on resume!!!");
        checkDatabase();
    }


    public void checkDatabase() {
        eventList = new ArrayList<JsonObject>();
        recyclerView = (RecyclerView) ((MainActivity)getActivity()).findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //getActivity() should get the activity's context? Instead of arguing "this"
        updateCards();

        final ProgressBar loadingBar = (ProgressBar) getView().findViewById(R.id.loadingBar);
        loadingBar.setIndeterminate(true);
        loadingBar.setVisibility(ProgressBar.VISIBLE);

        //Fetch Announcements
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HackRUService hackRUService = retrofit.create(HackRUService.class);
        Call<JsonObject> getEvents = hackRUService.getEvents();
        getEvents.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    Log.e(TAG, "Get request successful");
                    JsonArray events = response.body().getAsJsonObject().get("body").getAsJsonArray();
                    Iterator iterator = events.iterator();
                    while (iterator.hasNext()) {
                        Object test = iterator.next();
                        JsonElement hi = (JsonElement)test;
                        Log.e(TAG, hi.toString());

                    }
                    loadingBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
//                    updateCards();
                } else {
                    Log.e(TAG, "Bad response");
                    Log.e(TAG, response.body().toString());
                    Log.e(TAG, response.errorBody().toString());
                    Log.e(TAG, response.message().toString());
                    loadingBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i(TAG, "Get request failed");
                Log.i(TAG, t.getLocalizedMessage());
                loadingBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }
    public void updateCards() {
        adapter = new EventAdapter(getActivity(), eventList);
        recyclerView.setAdapter(adapter);
    }


}
