/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hackru.oneapp.hackru;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.hackru.oneapp.hackru.QRScanner.BarcodeGraphic;
import org.hackru.oneapp.hackru.QRScanner.BarcodeGraphicTracker;
import org.hackru.oneapp.hackru.QRScanner.BarcodeTrackerFactory;
import org.hackru.oneapp.hackru.QRScanner.Camera.CameraSource;
import org.hackru.oneapp.hackru.QRScanner.Camera.CameraSourcePreview;
import org.hackru.oneapp.hackru.QRScanner.Camera.GraphicOverlay;
import org.hackru.oneapp.hackru.api.model.ReadRequest;
import org.hackru.oneapp.hackru.api.service.HackRUService;
import org.hackru.oneapp.hackru.utils.SharedPreferencesUtility;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Activity for the multi-tracker app.  This app detects barcodes and displays the value with the
 * rear facing camera. During detection overlay graphics are drawn to indicate the position,
 * size, and ID of each barcode.
 */
public final class BarcodeCaptureActivity extends AppCompatActivity implements BarcodeGraphicTracker.BarcodeUpdateListener {
    private static final String TAG = "Barcode-reader";

    // intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    // constants used to pass extra data in the intent
    public static final String AutoFocus = "AutoFocus";
    public static final String UseFlash = "UseFlash";
    public static final String BarcodeObject = "Barcode";

    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;

    // helper objects for detecting taps and pinches.
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    Gson gson;
    Retrofit retrofit;
    HackRUService hackRUService;
    ConstraintLayout cameraLayout;
    String selectedEvent = null;

    /**
     * Initializes the UI and creates the detector pipeline.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_barcode_capture);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://m7cwj1fy7c.execute-api.us-west-2.amazonaws.com/mlhtest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        hackRUService = retrofit.create(HackRUService.class);

        setFinishOnTouchOutside(false);
        gson = new Gson();
        cameraLayout = (ConstraintLayout) findViewById(R.id.cameraLayout);

        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("Check-In", "Lunch 1", "Dinner", "Midnight Surprise", "T-Shirt", "Breakfast", "Lunch 2");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Log.e(TAG, item + " was slected");
                switch(item) {
                    case "Check-In":
                        selectedEvent = null;
                        break;
                    case "Lunch 1":
                        selectedEvent = "lunch_1";
                        break;
                    case "Dinner":
                        selectedEvent = "dinner";
                        break;
                    case "Midnight Surprise":
                        selectedEvent = "midnight_surprise";
                        break;
                    case "T-Shirt":
                        selectedEvent = "t_shirt";
                        break;
                    case "Breakfast":
                        selectedEvent = "breakfast";
                        break;
                    case "Lunch 2":
                        selectedEvent = "lunch_2";
                        break;
                    default:
                        Log.e(TAG, "ERROR READING DROPDOWN ITEM");
                }
            }
        });

        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay<BarcodeGraphic>) findViewById(R.id.graphicOverlay);

        // read parameters from the intent used to launch the activity.
        boolean autoFocus = getIntent().getBooleanExtra(AutoFocus, false);
        boolean useFlash = getIntent().getBooleanExtra(UseFlash, false);

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(autoFocus, useFlash);
        } else {
            requestCameraPermission();
        }

        gestureDetector = new GestureDetector(this, new CaptureGestureListener());
//        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

//        Snackbar.make(mGraphicOverlay, "Tap to capture. Pinch/Stretch to zoom",
//                Snackbar.LENGTH_LONG)
//                .show();
    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        findViewById(R.id.topLayout).setOnClickListener(listener);
//        Snackbar.make(mGraphicOverlay, "GIVE US PERMISSIONS YO",
//                Snackbar.LENGTH_INDEFINITE)
//                .setAction("Ok", listener)
//                .show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
//        boolean b = scaleGestureDetector.onTouchEvent(e);
        boolean b = false;

        boolean c = gestureDetector.onTouchEvent(e);

        return b || c || super.onTouchEvent(e);
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the barcode detector to detect small barcodes
     * at long distances.
     *
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();

        // A barcode detector is created to track barcodes.  An associated multi-processor instance
        // is set to receive the barcode detection results, track the barcodes, and maintain
        // graphics for each barcode on screen.  The factory is used by the multi-processor to
        // create a separate tracker instance for each barcode.
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay, this);
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());

        if (!barcodeDetector.isOperational()) {
            // Note: The first time that an app using the barcode or face API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any barcodes
            // and/or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w(TAG, "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, "ERROR LOW STORAGEEEE", Toast.LENGTH_LONG).show();
                Log.w(TAG, "ERROR LOW STORAGEEEE");
            }
        }

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the barcode detector to detect small barcodes
        // at long distances.
        CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setRequestedFps(24.0f);

        // make sure that auto focus is an available option
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(
                    autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null);
        }

        mCameraSource = builder
                .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .build();
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            boolean autoFocus = getIntent().getBooleanExtra(AutoFocus,false);
            boolean useFlash = getIntent().getBooleanExtra(UseFlash, false);
            createCameraSource(autoFocus, useFlash);
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                .setMessage("WE NEED CAMERA ACCESS BRUH")
                .setPositiveButton("Ok", listener)
                .show();
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    public void printLabel(String email) {
//        retrofit = new Retrofit.Builder()
//                .baseUrl("https://m7cwj1fy7c.execute-api.us-west-2.amazonaws.com/mlhtest/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        hackRUService = retrofit.create(HackRUService.class);

        final String finalEmail = email;

        HackRUService labelService = new Retrofit.Builder()
                .baseUrl("http://labels.hackru.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HackRUService.class);

        Log.e(TAG, email);

        labelService.printLabel(email).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e(TAG, "TAG PRINTED");
                Log.e(TAG, response.body().toString());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, "ERROR PRINTING TAG");
            }
        });


    }

    public void conductScan(Barcode best, String post) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(post).getAsJsonObject();

        hackRUService.update(obj).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                    Log.e(TAG, "QR scan submitted to API!");
                if(response.isSuccessful()) {
                    onScanSuccess();
                    String body = response.toString();
                    Log.e(TAG, body);
                } else {
                    Toast.makeText(getBaseContext(), "Error in API request", Toast.LENGTH_LONG).show();
                    onScanFailure();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Unable to reach scanner API", Toast.LENGTH_LONG).show();
                onScanFailure();
            }
        });
    }

    public void onScanSuccess() {
        cameraLayout.setBackgroundResource(R.drawable.scanner_success_animation);
        AnimationDrawable successAnimation = (AnimationDrawable) cameraLayout.getBackground();
        successAnimation.setEnterFadeDuration(500);
        successAnimation.setExitFadeDuration(500);
        successAnimation.setOneShot(true);
        successAnimation.start();
    }

    public void onScanFailure() {
        cameraLayout.setBackgroundResource(R.drawable.scanner_failure_animation);
        AnimationDrawable successAnimation = (AnimationDrawable) cameraLayout.getBackground();
        successAnimation.setEnterFadeDuration(500);
        successAnimation.setExitFadeDuration(500);
        successAnimation.setOneShot(true);
        successAnimation.start();
    }


    /**
     * onTap returns the tapped barcode result to the calling Activity.
     *
     * @param rawX - the raw position of the tap
     * @param rawY - the raw position of the tap.
     * @return true if the activity is ending.
     */
    private boolean onTap(float rawX, float rawY) {
        // Find tap point in preview frame coordinates.
        int[] location = new int[2];
        mGraphicOverlay.getLocationOnScreen(location);
        float x = (rawX - location[0]) / mGraphicOverlay.getWidthScaleFactor();
        float y = (rawY - location[1]) / mGraphicOverlay.getHeightScaleFactor();

        // Find the barcode whose center is closest to the tapped point.
        Barcode best = null;
        float bestDistance = Float.MAX_VALUE;
        for (BarcodeGraphic graphic : mGraphicOverlay.getGraphics()) {
            Barcode barcode = graphic.getBarcode();
            if (barcode.getBoundingBox().contains((int) x, (int) y)) {
                // Exact hit, no need to keep looking.
                best = barcode;
                break;
            }
            float dx = x - barcode.getBoundingBox().centerX();
            float dy = y - barcode.getBoundingBox().centerY();
            float distance = (dx * dx) + (dy * dy);  // actually squared distance
            if (distance < bestDistance) {
                best = barcode;
                bestDistance = distance;
            }
        }

        if (best != null) {
            //            Intent data = new Intent();
//            data.putExtra(BarcodeObject, best);
//            setResult(CommonStatusCodes.SUCCESS, data);
//            finish();


            /* ====== API CALL ====== */
            String post = "";
            if(selectedEvent == null) {
                post = "{" +
                        "'user_email': " + "'" + best.displayValue + "'," +
                        "'auth_email': " + "'" + SharedPreferencesUtility.getEmail(this) + "'," +
                        "'auth': " + "'" + SharedPreferencesUtility.getAuthToken(this) + "'," +
                        "'updates': {'$set': {'registration_status': 'checked-in', 'day_of.checked_in': true}}" +
                        "}";
            } else {
                post = "{" +
                        "'user_email': " + "'" + best.displayValue + "'," +
                        "'auth_email': " + "'" + SharedPreferencesUtility.getEmail(this) + "'," +
                        "'auth': " + "'" + SharedPreferencesUtility.getAuthToken(this) + "'," +
                        "'updates': {'$inc': {'day_of." + selectedEvent +"': 1}}" +
                        "}";
            }

            ReadRequest request = new ReadRequest(best.displayValue);
            final Barcode finalBest = best;
            final String finalPost = post;
            final String email = best.displayValue;
            cameraLayout.setBackgroundResource(R.drawable.scanner_loading_animation);
            AnimationDrawable loadingAnimation = (AnimationDrawable) cameraLayout.getBackground();
            loadingAnimation.setEnterFadeDuration(200);
            loadingAnimation.setExitFadeDuration(200);
            loadingAnimation.start();
            Log.e(TAG, best.displayValue);

            hackRUService.read(request).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.isSuccessful()) {
                        JsonObject body = response.body();
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BarcodeCaptureActivity.this, R.style.AppTheme_Dialogue_Alert);
                        if(body.getAsJsonArray("body").size() == 0) {
                            builder.setMessage("This person needs to sign up first!");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    onScanFailure();
                                }
                            })
                                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    onScanFailure();

                                }
                            });
                            builder.create().show();
                            return;
                        }
                        JsonElement registrationStatus = body.getAsJsonArray("body").get(0).getAsJsonObject().get("registration_status");
                        if(!SharedPreferencesUtility.getAllowWaitlist(BarcodeCaptureActivity.this) && !registrationStatus.getAsString().equals("registered")) {
                            builder.setMessage("Before 11AM we only allow accepted hackers. This hacker cannot be checked in at this time.");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    onScanFailure();
                                }
                            })
                                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            onScanFailure();

                                        }
                                    });
                            builder.create().show();
                            return;
                        }
                        JsonObject dayOf = body.getAsJsonArray("body").get(0).getAsJsonObject().get("day_of").getAsJsonObject();
                        JsonElement event = dayOf.get(selectedEvent);
                        if (event == null) {
                            if(dayOf.get("checked_in") == null || !dayOf.get("checked_in").getAsBoolean()) {
                                printLabel(email);
                                conductScan(finalBest, finalPost);
                            } else {
                                builder.setMessage("This person has already checked in. Do you want to print another label?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        onScanSuccess();
                                        printLabel(email);
                                    }
                                })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                onScanSuccess();
                                            }
                                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        onScanSuccess();

                                    }
                                });
                                builder.create().show();
                            }
                        } else {
                            int scanCount = event.getAsInt();
                            if(scanCount > 0) {
                                if(scanCount == 1) builder.setMessage("This person has had 1 serving already. Do you want to decline them?");
                                else builder.setMessage("This person has had " + scanCount + " servings already. Do you want to decline them?");
                                builder.setPositiveButton("Decline", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                onScanFailure();
                                            }
                                        })
                                        .setNegativeButton("Allow", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                conductScan(finalBest, finalPost);
                                            }
                                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        onScanFailure();

                                    }
                                });
                                builder.create().show();
                            } else {
                                conductScan(finalBest, finalPost);
                            }
                        }

                    } else {
                        Toast.makeText(getBaseContext(), "Error checking user history, please try again", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "Unable to reach scanner API", Toast.LENGTH_LONG).show();
                }
            });


            return true;
        }
        return false;
    }

    private class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return onTap(e.getRawX(), e.getRawY()) || super.onSingleTapConfirmed(e);
        }
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        /**
         * Responds to scaling events for a gesture in progress.
         * Reported by pointer motion.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should consider this event
         * as handled. If an event was not handled, the detector
         * will continue to accumulate movement until an event is
         * handled. This can be useful if an application, for example,
         * only wants to update scaling factors if the change is
         * greater than 0.01.
         */
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }

        /**
         * Responds to the beginning of a scaling gesture. Reported by
         * new pointers going down.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should continue recognizing
         * this gesture. For example, if a gesture is beginning
         * with a focal point outside of a region where it makes
         * sense, onScaleBegin() may return false to ignore the
         * rest of the gesture.
         */
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        /**
         * Responds to the end of a scale gesture. Reported by existing
         * pointers going up.
         * <p/>
         * Once a scale has ended, {@link ScaleGestureDetector#getFocusX()}
         * and {@link ScaleGestureDetector#getFocusY()} will return focal point
         * of the pointers remaining on the screen.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         */
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            mCameraSource.doZoom(detector.getScaleFactor());
        }
    }

    @Override
    public void onBarcodeDetected(Barcode barcode) {
        //do something with barcode data returned
    }
}
