package com.example.rishabravikumar.oneapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TimerFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView timerCount;
    private TextView timerTitle;
    //    private int timeInMillis = 3672000; // 1 hour 12 minutes
    private int timeInMillis = 30000; // 30 seconds
    private int hours;
    private int minutes;
    private int seconds;
    private CountDownTimer countDownTimer;

    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = (ProgressBar) ((MainActivity)getActivity()).findViewById(R.id.progressBar);
        timerCount = (TextView) ((MainActivity)getActivity()).findViewById(R.id.timerCount);
        timerTitle = (TextView) ((MainActivity)getActivity()).findViewById(R.id.timerTitle);
        start();
    }

    public void start() {
        //Sets the int that represents 100% progress
        progressBar.setMax(timeInMillis/1000);

        countDownTimer = new CountDownTimer(timeInMillis, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                hours = (int)millisUntilFinished/3600000;
                minutes = ((int)millisUntilFinished - (hours*3600000))/60000;
                seconds = ((int)millisUntilFinished - (hours*3600000) - (minutes*60000))/1000;

                timerCount.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));

                //Sets the progress of the bar to the current amount of seconds
                progressBar.setProgress(Math.round(millisUntilFinished * 0.001f));
            }

            @Override
            public void onFinish() {
                timerTitle.setText("Done!");
                timerCount.setVisibility(View.INVISIBLE);
            }
        }.start();

        countDownTimer.start();
    }
}
