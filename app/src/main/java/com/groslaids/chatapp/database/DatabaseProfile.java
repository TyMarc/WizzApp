package com.groslaids.chatapp.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Base64;
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
import com.groslaids.chatapp.tools.AvatarGenerator;

import java.io.ByteArrayOutputStream;

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
        initMyUser();
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

    public void initMyUser() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            addProfileEventListener();
        }
    }

    private void addProfileEventListener() {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i(TAG, "addProfileEventListener: uid=" + uid);
        usersDatabase.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                addFCMToken(context);
                Log.d(TAG, "User name: " + user.userName + ", email " + user.email);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void createUser(final String uid){
        user = new User();
        user.uid = uid;
        Bitmap bitmap = AvatarGenerator.generate(context, 50, 50);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        user.thumbnailBase64 = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
        usersDatabase.child(uid).setValue(user);
        addProfileEventListener();
    }

    public void addFCMToken(final Context context) {
        FirebaseApp.getInstance().getToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    final String token = task.getResult().getToken();
                    String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    Log.i(TAG, "onFCMFetchSuccessful");
                    if (user.hasFcmTokenChanged(deviceId, token)) {
                        Log.i(TAG, "onFCMFetchSuccessful: user.devices=" + user.devices.size());
                        usersDatabase.child(user.uid).child("devices").setValue(user.devices);
                    }
                }
            }
        });
    }
}
