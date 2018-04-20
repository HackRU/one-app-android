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

import org.hackru.oneapp.hackru.api.model.Event;
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
    private List<Event> eventList;
    final String BASE_URL = "https://m7cwj1fy7c.execute-api.us-west-2.amazonaws.com/mlhtest/";
    HackRUService hackRUService;
    ProgressBar loadingBar;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventList = new ArrayList<Event>();
        recyclerView = (RecyclerView) ((MainActivity)getActivity()).findViewById(R.id.recyclerViewEvents);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //getActivity() should get the activity's context? Instead of arguing "this"

        loadingBar = (ProgressBar) getView().findViewById(R.id.loadingBarEvents);
        loadingBar.setIndeterminate(true);
        loadingBar.setVisibility(ProgressBar.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        hackRUService = retrofit.create(HackRUService.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "on resume!!!");
        checkDatabase();
    }

    public boolean compareCache(List<Event> list) {
        for (Event item : list) {
            if(!eventList.contains(item)) return true;
        }

        return false;
    }


    public void checkDatabase() {

        Call<JsonObject> getEvents = hackRUService.getEvents();
        getEvents.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    Log.e(TAG, "Get request successful");
                    JsonArray events = response.body().getAsJsonObject().get("body").getAsJsonArray();
                    Iterator iterator = events.iterator();
                    while (iterator.hasNext()) {
                        JsonElement eventJSON = (JsonElement)iterator.next();
//                        Log.e(TAG, hi.toString());
                        String title = eventJSON.getAsJsonObject().get("summary").getAsString();
                        String description = eventJSON.getAsJsonObject().get("description")!=null ? eventJSON.getAsJsonObject().get("description").getAsString() : null;
                        String time = eventJSON.getAsJsonObject().get("start").getAsJsonObject().get("dateTime").getAsString();
                        String place = eventJSON.getAsJsonObject().get("location")!=null ? eventJSON.getAsJsonObject().get("location").getAsString() : null;
                        eventList.add(new Event(title, description, time, place));
                    }
                    updateCards();
                } else {
                    Log.e(TAG, "Bad response");
                    Log.e(TAG, response.body().toString());
                    Log.e(TAG, response.errorBody().toString());
                    Log.e(TAG, response.message().toString());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i(TAG, "Get request failed");
                Log.i(TAG, t.getLocalizedMessage());
            }
        });
    }
    public void updateCards() {
        loadingBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter = new EventAdapter(getActivity(), eventList);
        recyclerView.setAdapter(adapter);
    }


}
