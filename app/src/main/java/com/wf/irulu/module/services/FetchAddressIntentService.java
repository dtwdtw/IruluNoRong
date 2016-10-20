package com.wf.irulu.module.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.module.user.activity.AddNewAddressActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Asynchronously handles an intent using a worker thread. Receives a ResultReceiver object and a
 * location through an intent. Tries to fetch the address for the location using a Geocoder, and
 * sends the result to the ResultReceiver.
 */
public class FetchAddressIntentService extends IntentService {
    private static final String TAG = "FetchAddressIS";

    /**
     * The receiver where results are forwarded from this service.
     */
    protected ResultReceiver mReceiver;

    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public FetchAddressIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        mReceiver = intent.getParcelableExtra(AddNewAddressActivity.RECEIVER);


        if (mReceiver == null) {
            return;
        }

        Location location = intent.getParcelableExtra(AddNewAddressActivity.LOCATION_DATA_EXTRA);

        if (location == null) {

            deliverResultToReceiver(AddNewAddressActivity.FAILURE_RESULT, null);
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (IOException ioException) {

        } catch (IllegalArgumentException illegalArgumentException) {

        }

        if (addresses == null || addresses.size() == 0) {

            deliverResultToReceiver(AddNewAddressActivity.FAILURE_RESULT, "");
        } else {


            deliverResultToReceiver(AddNewAddressActivity.SUCCESS_RESULT, addresses.get(0).getLocale().getCountry());
        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        ILog.d("TB", "deliverResultToReceiver");
        Bundle bundle = new Bundle();
        bundle.putString(AddNewAddressActivity.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}
