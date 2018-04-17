package org.hackru.oneapp.hackru;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import net.glxn.qrgen.android.QRCode;

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
        String email = SharedPreferencesUtility.getEmail(getContext());
        userEmail.setText(email);

        Bitmap myBitmap = QRCode.from(email).withColor(0xFF1F4AB5, 0xFFFFFFFF).withSize(700, 700).bitmap();
        ImageView qrImage = (ImageView) getView().findViewById(R.id.qrImage);
        qrImage.setImageBitmap(myBitmap);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "DESTROYED");
    }
}