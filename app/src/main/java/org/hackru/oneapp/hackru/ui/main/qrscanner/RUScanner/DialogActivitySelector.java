package org.hackru.oneapp.hackru.ui.main.qrscanner.RUScanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.hackru.oneapp.hackru.R;


public class DialogActivitySelector extends DialogFragment{
    public static final String KEY_EVENTS= "EVENTS_KEY_0000";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_activity_selector, null);

        // hardcoded events
        String eventsArray[] = new String[5];
        eventsArray[0] = "Breakfast" ;
        eventsArray[1] = "Lunch";
        eventsArray[2] = "Dinner";
        eventsArray[3] = "Midnight Snack";
        eventsArray[4] = "Evening Snack";


        ListView eventList = v.findViewById(R.id.lv_events_list);

        ArrayAdapter<String> mEventAdapter =  new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, eventsArray);
        eventList.setAdapter(mEventAdapter);
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              // get the item that was clicked and dimiss
                String chosen = adapterView.getItemAtPosition(i).toString();
               //  sendData(chosen, Activity.RESULT_OK);
                dismiss();
            }
        });
       return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();


    }

    public void sendData(String eventChosen, int requestCode){
        Intent i = new Intent();
        i.putExtra(KEY_EVENTS,eventChosen);
        getTargetFragment().onActivityResult(getTargetRequestCode(),requestCode,i);

    }

    public static DialogActivitySelector newInstance(){

        DialogActivitySelector dialog = new DialogActivitySelector();
        return dialog;
    }
}
