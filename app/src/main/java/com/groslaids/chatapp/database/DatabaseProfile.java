package com.groslaids.chatapp.database;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by marc_ on 2017-10-01.
 */

public class DatabaseProfile {
    private static DatabaseProfile instance;
    private FirebaseDatabase firebaseDatabase;

    private DatabaseProfile() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public static DatabaseProfile getInstance() {
        if(instance == null) {
            instance = new DatabaseProfile();
        }

        return instance;
    }
}
