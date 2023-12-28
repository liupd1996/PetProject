package com.example.petproject.bean;

public class LoginRequest {
    String phone;
    String smsCode;
    String grant_type = "password";
    String client_id = "mydog";
    String scope = "all";
    String client_secret = "myDog";

    public LoginRequest(String phone, String smsCode) {
        this.phone = phone;
        this.smsCode = smsCode;
    }
}
