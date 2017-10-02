package com.groslaids.chatapp.model;

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
    public byte[]  thumbnail;
    public List<Device> devices;

    public User() {
        devices = new ArrayList<>();
    }

    public void addDevice(Device device) {
        devices.add(device);
    }
}
