package org.hackru.oneapp.hackru;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class MapDialogueFragment extends DialogFragment {
    String TAG = "MapDialogueFragment";

    public MapDialogueFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mapdialogue, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mapRef = storageRef.child("map.png");
        Log.e(TAG, mapRef.getPath());
        Log.e(TAG, mapRef.getName());
        Log.e(TAG, mapRef.getBucket());
        ImageView mapImage = (ImageView) getView().findViewById(R.id.mapImage);
        Glide.with(getContext())
                .using(new FirebaseImageLoader())
                .load(mapRef)
                .into(mapImage);
    }

}