package com.example.bonjoo.model;

import com.google.firebase.Timestamp;

public class UserModel {

    private String username;
    private String phone;
    private Timestamp createdTimeststamp;
    private String userID;
    private String fcmToken;

    public UserModel() {
    }

    public UserModel(String username, String phone, Timestamp createdTimeststamp,String userID) {
        this.username = username;
        this.phone = phone;
        this.createdTimeststamp = createdTimeststamp;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getCreatedTimeststamp() {
        return createdTimeststamp;
    }

    public void setCreatedTimeststamp(Timestamp createdTimeststamp) {
        this.createdTimeststamp = createdTimeststamp;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
