package com.example.petproject.bean;

public class LoginRequest {
    String phone;
    int type;
    String code;
    String password;


    public LoginRequest(String phone, int type, String code) {
        this.phone = phone;
        this.type = type;
        this.code = code;
    }
}
