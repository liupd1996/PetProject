package com.example.petproject.bean;

public class LoginResponse {
    public String accessToken;
    public String refreshToken;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "access_token='" + accessToken + '\'' +
                ", refresh_token='" + refreshToken + '\'' +
                '}';
    }
}
