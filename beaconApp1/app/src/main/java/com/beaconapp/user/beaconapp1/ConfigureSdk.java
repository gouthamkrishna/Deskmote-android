package com.beaconapp.user.beaconapp1;


import android.app.Application;

import com.estimote.sdk.EstimoteSDK;

public class ConfigureSdk extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initializes Estimote SDK with your App ID and App Token from Estimote Cloud.
        // You can find your App ID and App Token in the
        // Apps section of the Estimote Cloud (http://cloud.estimote.com).
        EstimoteSDK.initialize(this, "YOUR APP ID", "YOUR APP TOKEN");

        // Configure verbose debug logging.
        EstimoteSDK.enableDebugLogging(true);
    }
}
