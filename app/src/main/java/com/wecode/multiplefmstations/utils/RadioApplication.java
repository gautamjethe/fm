package com.wecode.multiplefmstations.utils;

import android.app.Application;

import com.onesignal.BuildConfig;
import com.onesignal.OneSignal;

public class RadioApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Logging set to help debug issues, remove before releasing your app.
        if (BuildConfig.DEBUG)
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}
