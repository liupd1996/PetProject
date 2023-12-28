package com.example.petproject.bean;

public class RegisterRequest {
    String avatar;
    int gender = 0;
    String password = "123456";
    String phone;
    String smsCode;
    String username;

    public RegisterRequest(String avatar, String phone, String smsCode, String username) {
        this.avatar = avatar;
        this.phone = phone;
        this.smsCode = smsCode;
        this.username = username;
    }
}
