package com.groslaids.chatapp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marc-antoinehinse on 2017-10-01.
 */
public class User {

    public String uid;
    public String displayName;
    public String userName;
    public String email;
    public int age;
    public String photoUrl;

    public String thumbnailBase64;
    public List<Device> devices;
    public List<String> contacts;

    @Exclude
    public Bitmap thumbnail;

    public User() {
        devices = new ArrayList<>();
    }

    @Exclude
    public void addDevice(Device device) {
        devices.add(device);
    }

    @Exclude
    public Bitmap getThumbnail() {
        if(thumbnail == null) {
            byte[] thumbArray = Base64.decode(thumbnailBase64, Base64.DEFAULT);
            thumbnail = BitmapFactory.decodeByteArray(thumbArray, 0, thumbArray.length);
        }

        return thumbnail;
    }

    @Exclude
    public boolean hasFcmTokenChanged(String deviceId, String token) {
        boolean isFound = false;
        for(Device d : devices) {
            if(d != null) {
                if (TextUtils.equals(d.deviceId, deviceId)) {
                    isFound = true;
                    if(!TextUtils.equals(d.fcmToken, token)) {
                        d.fcmToken = token;
                        return true;
                    }
                }
            }
        }

        if(!isFound) {
            Device device = new Device();
            device.deviceId = deviceId;
            device.fcmToken = token;
            addDevice(device);
            return true;
        }

        return false;
    }
}
