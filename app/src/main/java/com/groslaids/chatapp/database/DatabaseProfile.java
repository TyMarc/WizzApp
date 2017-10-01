package com.groslaids.chatapp.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.groslaids.chatapp.Model.Users;

/**
 * Created by marc_ on 2017-10-01.
 */

public class DatabaseProfile {
    private static DatabaseProfile instance;
    private DatabaseReference usersDatabase;

    private DatabaseProfile() {
        usersDatabase = FirebaseDatabase.getInstance().getReference("users");
    }

    public static DatabaseProfile getInstance() {
        if(instance == null) {
            instance = new DatabaseProfile();
        }

        return instance;
    }

    public void createUser(Users user){
        usersDatabase.child("users").child(user.uID).setValue(user);
    }
}
