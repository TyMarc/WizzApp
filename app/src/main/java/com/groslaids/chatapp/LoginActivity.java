package com.groslaids.chatapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.groslaids.chatapp.model.User;
import com.groslaids.chatapp.database.DatabaseProfile;

public class LoginActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    private EditText emailAddressEditText;
    private EditText passwordEditText;
    private FirebaseAuth firebaseAuth;

    public static void show(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        emailAddressEditText = (EditText) findViewById(R.id.edit_text_email);
        passwordEditText = (EditText) findViewById(R.id.edit_text_password);

        findViewById(R.id.register).setOnClickListener(this);
        findViewById(R.id.sign_in).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if(id == R.id.register) {
            register();
        } else if(id == R.id.sign_in) {
            signIn();
        }
    }

    private void register() {
        if(!TextUtils.isEmpty(emailAddressEditText.getText()) && !TextUtils.isEmpty(passwordEditText.getText())) {
            final String emailAddress = emailAddressEditText.getText().toString();
            final String password = passwordEditText.getText().toString();

            firebaseAuth.createUserWithEmailAndPassword(emailAddress, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                //User created
                                Log.i(TAG, "User created");
                                User newUser = new User();
                                newUser.uid = firebaseAuth.getCurrentUser().getUid();
                                DatabaseProfile.getInstance().createUser(newUser);
                            } else {
                                //Error
                                String message = "Failed to create user:"+task.getException().getMessage();
                                Snackbar.make(emailAddressEditText, message, 2500).show();
                            }
                        }
                    });
        }

    }

    private void signIn() {
        if(!TextUtils.isEmpty(emailAddressEditText.getText()) && !TextUtils.isEmpty(passwordEditText.getText())) {
            final String emailAddress = emailAddressEditText.getText().toString();
            final String password = passwordEditText.getText().toString();

            firebaseAuth.signInWithEmailAndPassword(emailAddress, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                //User logged in
                                User newUser = new User();
                                newUser.uid = firebaseAuth.getCurrentUser().getUid();
                                DatabaseProfile.getInstance().createUser(newUser);
                                MainActivity.show(LoginActivity.this);
                                finish();
                            } else {
                                //Error
                                Log.i(TAG, "Error when logging user");
                            }
                        }
                    });
        }
    }
}
