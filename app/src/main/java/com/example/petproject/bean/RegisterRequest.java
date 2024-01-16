package com.example.petproject.bean;

public class RegisterRequest {
    String avatar;
    int gender = 0;
    String password = "123456";
    String phone;
    String smsCode;
    String username;

    public RegisterRequest(String avatar, int gender, String phone, String smsCode, String username) {
        this.avatar = avatar;
        this.gender = gender;
        this.phone = phone;
        this.smsCode = smsCode;
        this.username = username;
    }
}
