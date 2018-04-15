package org.hackru.oneapp.hackru;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.GregorianCalendar;

public class TimerFragment extends Fragment {
    String TAG = "TimerFragment";

    private ProgressBar progressBar;
    private TextView timerCount;
    private TextView timerTitle;
    private TextView endMessage;
    private long timerInMillis;
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

        GregorianCalendar cal = new GregorianCalendar();

        Log.e(TAG, "" + cal.getTimeInMillis());
        final long currentTime = cal.getTimeInMillis();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference().child("deadline");;
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again whenever data at this location is updated.
                long deadline = dataSnapshot.getValue(Long.class);
                timerInMillis = deadline - currentTime;
                if(timerInMillis > 86400000) {
                    if(countDownTimer != null) countDownTimer.cancel();
                    progressBar.setMax(1);
                    progressBar.setProgress(1);
                    timerCount.setText("24:00:00");
                } else if(timerInMillis < 0) {
                    if(countDownTimer != null) countDownTimer.cancel();
                    timerTitle.setVisibility(View.GONE);
                    timerCount.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    endMessage.setVisibility(View.VISIBLE);
                    if(countDownTimer != null) countDownTimer.cancel();
                } else {
                    if(countDownTimer != null) countDownTimer.cancel();
                    start();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void start() {
        timerTitle.setVisibility(View.VISIBLE);
        timerCount.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        endMessage.setVisibility(View.GONE);
        //Sets the int that represents 100% progress
        progressBar.setMax(86400); // 86400 seconds is 24 hours

        countDownTimer = new CountDownTimer(timerInMillis, 100) {
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
        };

        countDownTimer.start();
    }
}
