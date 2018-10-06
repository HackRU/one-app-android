package org.hackru.oneapp.hackru.ui.drawer.scanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.hackru.oneapp.hackru.HackRUApp;
import org.hackru.oneapp.hackru.R;
import org.hackru.oneapp.hackru.Utils;
import org.hackru.oneapp.hackru.api.models.DayOfModel;
import org.hackru.oneapp.hackru.api.models.UpdateModel;
import org.hackru.oneapp.hackru.api.services.LcsService;
import org.hackru.oneapp.hackru.api.services.MiscService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cdflynn.android.library.checkview.CheckView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaterialBarcodeScannerActivity extends AppCompatActivity {

    private static final int RC_HANDLE_GMS = 9001;
    private static final String RQ_DIALOG = "rq_dialog";
    public static final String EVENTS = "events_extra";
    private static final int RC_DIALOG_ACTIVITY = 9002;

    private static final String TAG = "MaterialBarcodeScanner";

    private MaterialBarcodeScanner mMaterialBarcodeScanner;
    private MaterialBarcodeScannerBuilder mMaterialBarcodeScannerBuilder;

    private BarcodeDetector barcodeDetector;

    private CameraSourcePreview mCameraSourcePreview;

    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;

    private SoundPoolPlayer mSoundPoolPlayer;

    private String previous = "";

    private boolean mFlashOn = false;

    private String[] events = null;
    private int currentEvent = 0;

    private Handler handler = new Handler();

    @Inject
    MiscService miscService;
    @Inject
    LcsService lcsService;

    String PRINTER_BASE_URL = null;

    private void setupScanner() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MaterialBarcodeScannerActivity.this)
                .setView(getLayoutInflater().inflate(R.layout.dialog_progress_circle, null))
                .setTitle("Updating events...")
                .setCancelable(false)
                .create();
        alertDialog.show();
        miscService.getScannerEvents().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body() != null) {
                    events = response.body().split("\n");
                    final TextView currentEvent = findViewById(R.id.current_event);
                    currentEvent.setText("Scanning for " + events[0]);
                    fetchPrinterURL(alertDialog);
                } else {
                    showSetupScannerFailure();
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showSetupScannerFailure();
                alertDialog.dismiss();
            }
        });
    }

    private void fetchPrinterURL(final AlertDialog alertDialog) {
        miscService.getPrinterURL().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body() != null) {
                    PRINTER_BASE_URL = response.body().trim();
                    setupChangeEventButton();
                } else {
                    showSetupScannerFailure();
                }
                alertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showSetupScannerFailure();
                alertDialog.dismiss();
            }
        });
    }

    private void showSetupScannerFailure() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MaterialBarcodeScannerActivity.this)
                .setCancelable(false)
                .setTitle("Couldn't load scanning data from server")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setupScanner();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .create();
        alertDialog.show();
    }

    private void setupChangeEventButton() {
        Button changeEventButton = findViewById(R.id.btn_change_event);
        changeEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MaterialBarcodeScannerActivity.this)
                        .setTitle("Pick an event")
                        .setItems(events, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TextView currentEventLabel = findViewById(R.id.current_event);
                                currentEvent = i;
                                currentEventLabel.setText("Scanning for " + events[currentEvent]);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void checkScannedBefore(final String barcode) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MaterialBarcodeScannerActivity.this)
                .setView(getLayoutInflater().inflate(R.layout.dialog_progress_circle, null))
                .setTitle("Sending to server...")
                .setCancelable(false)
                .create();
        alertDialog.show();
        DayOfModel.Query query = new DayOfModel.Query(barcode);
        final String scannerEmail = Utils.SharedPreferences.INSTANCE.getEmail(this);
        final String token = Utils.SharedPreferences.INSTANCE.getAuthToken(this);
        DayOfModel.Request request = new DayOfModel.Request(scannerEmail, token, query);
        lcsService.getUserDayOf(request).enqueue(new Callback<DayOfModel.Response>() {
            @Override
            public void onResponse(Call<DayOfModel.Response> call, Response<DayOfModel.Response> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<DayOfModel.User> userList = response.body().getBody();
                    if(userList.isEmpty()) {
                        alertDialog.dismiss();
                        Toast.makeText(MaterialBarcodeScannerActivity.this, "Email isn't registered to a user", Toast.LENGTH_LONG).show();
                    } else {
                        Map<String, Object> dayOf = userList.get(0).getDay_of();
                        if(!dayOf.containsKey(events[currentEvent])) {
                            changeUserDayOf(alertDialog, barcode, scannerEmail, token);
                        } else {
                            showUserScannedBefore(barcode, scannerEmail, token);
                            alertDialog.dismiss();
                        }
                    }
                } else {
                    showScanFailure(barcode);
                }
            }

            @Override
            public void onFailure(Call<DayOfModel.Response> call, Throwable t) {
                showScanFailure(barcode);
            }
        });
    }

    private void showUserScannedBefore(final String barcode, final String scannerEmail, final String token) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MaterialBarcodeScannerActivity.this)
                .setTitle("This user was already scanned for this event. Continue?")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(MaterialBarcodeScannerActivity.this)
                                .setView(getLayoutInflater().inflate(R.layout.dialog_progress_circle, null))
                                .setTitle("Sending to server...")
                                .setCancelable(false)
                                .create();
                        alertDialog.show();
                        changeUserDayOf(alertDialog, barcode, scannerEmail, token);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing. The dialog will dismiss by default.
                    }
                })
                .create();
        alertDialog.show();
    }

    private void changeUserDayOf(final AlertDialog alertDialog, final String barcode, String scannerEmail, String token) {
        UpdateModel.Request request = new UpdateModel.Request(scannerEmail, token, barcode, events[currentEvent]);
        lcsService.updateUserDayOf(request).enqueue(new Callback<UpdateModel.Response>() {
            @Override
            public void onResponse(Call<UpdateModel.Response> call, Response<UpdateModel.Response> response) {
                if(response.isSuccessful() && response.body() != null) {
                    int statusCode = response.body().getStatusCode();
                    if(statusCode >= 200 && statusCode < 300) {
                        showScanSuccess();
                    } else {
                        showScanFailure(barcode, response.body().getBody());
                    }
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UpdateModel.Response> call, Throwable t) {
                showScanFailure(barcode);
                alertDialog.dismiss();
            }
        });
    }

    private void showScanSuccess() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_checkmark, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(MaterialBarcodeScannerActivity.this)
                .setView(dialogView)
                .setTitle("Success!")
                .setCancelable(false)
                .create();
        CheckView checkView = dialogView.findViewById(R.id.check);
        alertDialog.show();
        checkView.check();
        Runnable dismissCheckmark = new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        };
        handler.postDelayed(dismissCheckmark, 1000);
    }

    private void showScanFailure(final String barcode) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MaterialBarcodeScannerActivity.this)
                .setTitle("Network error")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkScannedBefore(barcode);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing. The dialog will dismiss by default.
                    }
                })
                .create();
        alertDialog.show();
    }

    private void showScanFailure(final String barcode, String errorMessage) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MaterialBarcodeScannerActivity.this)
                .setTitle(errorMessage)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkScannedBefore(barcode);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing. The dialog will dismiss by default.
                    }
                })
                .create();
        alertDialog.show();
    }

    private void onDetect(final Barcode barcode) {
        if(events == null) return;
        if(!barcode.displayValue.equals(previous)) {
            Log.d(TAG, "New code!");
            previous = barcode.displayValue;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    checkScannedBefore(barcode.displayValue);
                }
            });
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if(getWindow() != null){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            Log.e(TAG, "Barcode scanner could not go into fullscreen mode!");
        }

       /* ArrayList<String> eventsList = getIntent().getStringArrayListExtra(EVENTS);
        mEvents = new String[eventsList.size()];
        mEvents = eventsList.oArray(mEvents);*/

        setContentView(R.layout.activity_scanner);

        ((HackRUApp)getApplication()).appComponent.inject(this);
        setupScanner();
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException {

        // check that the device has play services available.
        mSoundPoolPlayer = new SoundPoolPlayer(this);
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dialog.show();
        }
        mGraphicOverlay = (GraphicOverlay<BarcodeGraphic>)findViewById(R.id.graphicOverlay);
        BarcodeGraphicTracker.NewDetectionListener listener =  new BarcodeGraphicTracker.NewDetectionListener() {
            @Override
            public void onNewDetection(Barcode barcode) {
                Log.d(TAG, "Barcode detected! - " + barcode.displayValue);
                EventBus.getDefault().postSticky(barcode);
                updateCenterTrackerForDetectedState();
                if(mMaterialBarcodeScannerBuilder.isBleepEnabled()){
                    //                        mSoundPoolPlayer.playShortResource(R.raw.bleep);
                }
                //                    mGraphicOverlay.postDelayed(new Runnable() {
                //                        @Override
                //                        public void run() {
                //                            finish();
                //                        }
                //                    },50);

                onDetect(barcode);

            }
        };
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay, listener, mMaterialBarcodeScannerBuilder.getTrackerColor());
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());
        CameraSource mCameraSource = mMaterialBarcodeScannerBuilder.getCameraSource();
        if (mCameraSource != null) {
            try {
                mCameraSourcePreview = (CameraSourcePreview) findViewById(R.id.preview);
                mCameraSourcePreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMaterialBarcodeScanner(MaterialBarcodeScanner materialBarcodeScanner){
        this.mMaterialBarcodeScanner = materialBarcodeScanner;
        mMaterialBarcodeScannerBuilder = mMaterialBarcodeScanner.getMaterialBarcodeScannerBuilder();
        barcodeDetector = mMaterialBarcodeScanner.getMaterialBarcodeScannerBuilder().getBarcodeDetector();
        startCameraSource();
        setupLayout();
    }

    private void setupLayout() {
//        String topText = mMaterialBarcodeScannerBuilder.getText();
//        if(!mMaterialBarcodeScannerBuilder.getText().equals("")){
//            currentEvent.setText(topText);
//        }

        setupCenterTracker();
    }

    private void setupEvents(){
        // get events from database
    }

    private void setupCenterTracker() {
        if(mMaterialBarcodeScannerBuilder.getScannerMode() == MaterialBarcodeScanner.SCANNER_MODE_CENTER){
            final ImageView centerTracker  = (ImageView) findViewById(R.id.barcode_square);
        //    centerTracker.setImageResource(mMaterialBarcodeScannerBuilder.getTrackerResourceID());
            mGraphicOverlay.setVisibility(View.INVISIBLE);
        }
    }

    private void updateCenterTrackerForDetectedState() {
        if(mMaterialBarcodeScannerBuilder.getScannerMode() == MaterialBarcodeScanner.SCANNER_MODE_CENTER){
            final ImageView centerTracker  = (ImageView) findViewById(R.id.barcode_square);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   // centerTracker.setImageResource(mMaterialBarcodeScannerBuilder.getTrackerDetectedResourceID());
                }
            });
        }
    }

    private void enableTorch() throws SecurityException{
        mMaterialBarcodeScannerBuilder.getCameraSource().setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        try {
            mMaterialBarcodeScannerBuilder.getCameraSource().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disableTorch() throws SecurityException{
        mMaterialBarcodeScannerBuilder.getCameraSource().setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        try {
            mMaterialBarcodeScannerBuilder.getCameraSource().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mCameraSourcePreview != null) {
            mCameraSourcePreview.stop();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isFinishing()){
            clean();
        }
    }

    private void clean() {
        EventBus.getDefault().removeStickyEvent(MaterialBarcodeScanner.class);
        if (mCameraSourcePreview != null) {
            mCameraSourcePreview.release();
            mCameraSourcePreview = null;
        }
        if(mSoundPoolPlayer != null){
            mSoundPoolPlayer.release();
            mSoundPoolPlayer = null;
        }
    }
}
