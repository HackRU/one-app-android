package org.hackru.oneapp.hackru;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.hackru.oneapp.hackru.utils.SharedPreferencesUtility;

public class QRDialogueFragment extends DialogFragment {
    String TAG = "QRDialogueFragment";

    public QRDialogueFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qrdialogue, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView userEmail = (TextView) getView().findViewById(R.id.userEmail);
        userEmail.setText(SharedPreferencesUtility.getEmail(getActivity()));

        // Just testing FireBaseUI, this code should be deleted
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//        StorageReference mapRef = storageRef.child("test.png");
//        Log.e(TAG, mapRef.getPath());
//        Log.e(TAG, mapRef.getName());
//        Log.e(TAG, mapRef.getBucket());
//        ImageView imageView = (ImageView) getView().findViewById(R.id.imageView);
//        Glide.with(getContext())
//                .using(new FirebaseImageLoader())
//                .load(storageRef)
//                .into(imageView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "DESTROYED");
    }
}