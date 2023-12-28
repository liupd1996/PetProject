package com.example.petproject.bean;

public class LoginResponse {
    public String access_token;
    public String refresh_token;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "access_token='" + access_token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                '}';
    }
}
