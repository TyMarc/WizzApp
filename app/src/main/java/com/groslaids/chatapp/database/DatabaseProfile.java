package com.groslaids.chatapp.database;

import android.content.Context;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.groslaids.chatapp.model.Device;
import com.groslaids.chatapp.model.User;

/**
 * Created by marc_ on 2017-10-01.
 */

public class DatabaseProfile {
    private static final String TAG = "DatabaseProfile";

    private static DatabaseProfile instance;
    private DatabaseReference usersDatabase;
    private User user;
    private Context context;

    private DatabaseProfile(final Context context) {
        this.context = context;
        usersDatabase = FirebaseDatabase.getInstance().getReference("users");
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            getMyUser();
            addProfileEventListener();
        }
    }

    public static void init(final Context context) {
        instance = new DatabaseProfile(context);
    }

    public static DatabaseProfile getInstance() {
        if(instance == null) {
            throw new IllegalStateException("You must init the database in the application class.");
        }

        return instance;
    }

    private void addProfileEventListener() {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i(TAG, "addProfileEventListener: uid=" + uid);
        usersDatabase.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(User.class);

                Log.d(TAG, "User name: " + user.userName + ", email " + user.email);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void getMyUser() {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i(TAG, "Finding user");
        usersDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "Found user");
                user = dataSnapshot.getValue(User.class);
                addFCMToken(context);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "Error while finding user: error=" + databaseError.getMessage());
            }
        });
    }

    public void createUser(User user){
        this.user = user;
        usersDatabase.child(user.uid).setValue(user);
        addProfileEventListener();
        addFCMToken(context);
    }

    public void addFCMToken(final Context context) {
        FirebaseApp.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    Device device = new Device();
                    device.deviceId = deviceId;
                    device.fcmToken = task.getResult().getToken();
                    user.addDevice(device);
                    usersDatabase.child(user.uid).child("devices").setValue(user.devices);
                }
            }
        });
    }


}
