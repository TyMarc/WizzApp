package com.groslaids.chatapp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.groslaids.chatapp.database.DatabaseProfile;

/**
 * Created by marc_ on 2017-10-01.
 */

public class WizzApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseProfile.init(this);
    }
}
