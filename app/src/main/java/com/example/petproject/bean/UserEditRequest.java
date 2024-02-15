package com.example.petproject.bean;

public class UserEditRequest {
    String avatar;
    int gender;
    String id;
    String username;

    public UserEditRequest(String avatar, int gender, String id, String username) {
        this.avatar = avatar;
        this.gender = gender;
        this.id = id;
        this.username = username;
    }
}
