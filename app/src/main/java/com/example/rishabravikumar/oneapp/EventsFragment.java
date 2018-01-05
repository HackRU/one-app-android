package com.example.rishabravikumar.oneapp;

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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EventsFragment extends Fragment{

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
}
