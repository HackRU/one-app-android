package org.hackru.oneapp.hackru;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TimerFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView timerCount;
    private TextView timerTitle;
    private TextView endMessage;
    private int timeInMillis = 57600000; // this is the progress of the timer. The initial value is for testing. 57600000 milliseconds is 16 hours
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
        endMessage = (TextView) ((MainActivity)getActivity()).findViewById(R.id.endMessage);
        start();
    }

    public void start() {
        //Sets the int that represents 100% progress
        progressBar.setMax(86400); // 86400 seconds is 24 hours

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
                timerTitle.setVisibility(View.GONE);
                timerCount.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                endMessage.setVisibility(View.VISIBLE);
            }
        }.start();

        countDownTimer.start();
    }
}
