package com.example.xyzreader;

import android.app.Application;

import timber.log.Timber;

public class XyzReaderApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.uprootAll();
            Timber.plant(new Timber.DebugTree());
        }
    }
}
