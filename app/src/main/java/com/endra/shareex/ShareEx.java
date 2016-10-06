package com.endra.shareex;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Endra on 10/6/2016.
 */
public class ShareEx extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
