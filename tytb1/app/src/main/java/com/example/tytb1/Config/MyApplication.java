package com.example.tytb1.Config;

import android.app.Application;
import android.util.Log;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", "dgaltasqa");
        config.put("api_key", "117686252291233");
        config.put("api_secret", "-GuCF9JicYjbFL_fntRbfW1BzvU");

        try {
            MediaManager.init(this, config);
            Log.d(TAG, "MediaManager initialized successfully");
        } catch (IllegalStateException e) {
            Log.d(TAG, "MediaManager is already initialized");
        }
    }
}