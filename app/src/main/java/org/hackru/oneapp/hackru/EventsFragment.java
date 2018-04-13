package org.hackru.oneapp.hackru;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.hackru.oneapp.hackru.api.model.Announcement;
import org.hackru.oneapp.hackru.api.model.Event;
import org.hackru.oneapp.hackru.api.service.HackRUService;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventsFragment extends Fragment{

    final String BASE_URL = "https://m7cwj1fy7c.execute-api.us-west-2.amazonaws.com/mlhtest/";

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

        // Get a reference for the week view in the layout.
        WeekView mWeekView = (WeekView)view.findViewById(R.id.weekView);

        //Fetch the events from LCS
        //Just need to figure out what to do with this now...
        getEvents();

        // Set an action when any event is clicked.
        //mWeekView.setOnEventClickListener(mEventClickListener);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.

        // Set long press listener for events.
        //mWeekView.setEventLongPressListener(mEventLongPressListener);

        MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {



                // Populate the week view with some events.
                List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, 3);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.MONTH, newMonth - 1);
                startTime.set(Calendar.YEAR, newYear);
                Calendar endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.HOUR, 1);
                endTime.set(Calendar.MONTH, newMonth - 1);
                WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
                event.setColor(getResources().getColor(R.color.colorPrimary));
                events.add(event);

                startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, 3);
                startTime.set(Calendar.MINUTE, 30);
                startTime.set(Calendar.MONTH, newMonth-1);
                startTime.set(Calendar.YEAR, newYear);
                endTime = (Calendar) startTime.clone();
                endTime.set(Calendar.HOUR_OF_DAY, 4);
                endTime.set(Calendar.MINUTE, 30);
                endTime.set(Calendar.MONTH, newMonth-1);
                event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
                event.setColor(getResources().getColor(R.color.colorPrimaryDark));
                events.add(event);

                startTime = Calendar.getInstance();
                startTime.set(Calendar.DAY_OF_MONTH, 1);
                startTime.set(Calendar.HOUR_OF_DAY, 3);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.MONTH, newMonth-1);
                startTime.set(Calendar.YEAR, newYear);
                endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.HOUR_OF_DAY, 3);
                event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
                event.setColor(getResources().getColor(R.color.colorPrimary));
                events.add(event);

                startTime = Calendar.getInstance();
                startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
                startTime.set(Calendar.HOUR_OF_DAY, 15);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.MONTH, newMonth-1);
                startTime.set(Calendar.YEAR, newYear);
                endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.HOUR_OF_DAY, 3);
                event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
                event.setColor(getResources().getColor(R.color.colorAccent));
                events.add(event);

                //AllDay event
                startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, 0);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.MONTH, newMonth-1);
                startTime.set(Calendar.YEAR, newYear);
                endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.HOUR_OF_DAY, 23);
                event = new WeekViewEvent(7, getEventTitle(startTime), startTime, endTime);
                event.setColor(getResources().getColor(R.color.colorAccent));
                events.add(event);
                events.add(event);

                startTime = Calendar.getInstance();
                startTime.set(Calendar.DAY_OF_MONTH, 8);
                startTime.set(Calendar.HOUR_OF_DAY, 2);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.MONTH, newMonth-1);
                startTime.set(Calendar.YEAR, newYear);
                endTime = (Calendar) startTime.clone();
                endTime.set(Calendar.DAY_OF_MONTH, 10);
                endTime.set(Calendar.HOUR_OF_DAY, 23);
                event = new WeekViewEvent(8, getEventTitle(startTime), startTime, endTime);
                event.setColor(getResources().getColor(R.color.colorPrimary));
                events.add(event);

                // All day event until 00:00 next day
                startTime = Calendar.getInstance();
                startTime.set(Calendar.DAY_OF_MONTH, 10);
                startTime.set(Calendar.HOUR_OF_DAY, 0);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.SECOND, 0);
                startTime.set(Calendar.MILLISECOND, 0);
                startTime.set(Calendar.MONTH, newMonth-1);
                startTime.set(Calendar.YEAR, newYear);
                endTime = (Calendar) startTime.clone();
                endTime.set(Calendar.DAY_OF_MONTH, 11);
                event = new WeekViewEvent(8, getEventTitle(startTime), startTime, endTime);
                event.setColor(getResources().getColor(R.color.colorPrimaryDark));
                events.add(event);


                return events;
            }
        };
        mWeekView.setBackgroundColor(Color.WHITE);
        mWeekView.setEventTextColor(Color.WHITE);
        mWeekView.setNumberOfVisibleDays(1);
        mWeekView.setTextSize(22);
        mWeekView.setHourHeight(120);
        mWeekView.setHeaderColumnPadding(8);
        mWeekView.setHeaderRowPadding(16);
        mWeekView.setColumnGap(8);
        mWeekView.setHourSeparatorColor(Color.WHITE);
        mWeekView.setHourSeparatorHeight(4);
        mWeekView.setHeaderColumnBackgroundColor(Color.WHITE);
        mWeekView.setHeaderColumnBackgroundColor(Color.BLACK);
        mWeekView.setOverlappingEventGap(2);
        mWeekView.setMonthChangeListener(mMonthChangeListener);
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    public void getEvents() {
        //Fetch Events
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HackRUService hackRUService = retrofit.create(HackRUService.class);
        Call<List<Event>> getEvents = hackRUService.getEvents();
        getEvents.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    Log.i("Events", "Get request successful");
                    List<Event> fetchedEventsList = response.body();
                    for (Event e : fetchedEventsList) {
                        Log.i("Event.", e.toString());
                    }

                    //Since this is an inner class and the result is returned asynchronously,
                    //it's easier to call a method that updates the UI from here
                    updateEvents(fetchedEventsList);
                } else {
                    Log.i("Events", "Bad response");
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.i("Events", "Get request failed");
            }
        });
    }

    public void updateEvents(List<Event> events) {
        //Do something to update calendar once events are fetched
    }
}
