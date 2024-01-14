package com.example.petproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigPreferences {
    public static String LOGIN_TOKEN = "login_token";
    public static String REFRESH_TOKEN = "refresh_token";
    public static String LOGIN_NAME = "login_name";
    private static SharedPreferences config;

    public static SharedPreferences getInstance(Context context) {
        if (config == null) {
            config = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return config;
    }

    public static String login_token(Context context) {
        return getInstance(context).getString(LOGIN_TOKEN, "");
    }

    public static String refresh_token(Context context) {
        return getInstance(context).getString(REFRESH_TOKEN, "");
    }

    public static String login_name(Context context) {
        return getInstance(context).getString(LOGIN_NAME, "");
    }

    public static void setLoginToken(Context context, String loginToken) {
        getInstance(context).edit().putString(LOGIN_TOKEN, loginToken).apply();
    }

    public static void setRefreshToken(Context context, String refreshToken) {
        getInstance(context).edit().putString(REFRESH_TOKEN, refreshToken).apply();
    }

    public static void setLoginName(Context context, String loginName) {
        getInstance(context).edit().putString(LOGIN_NAME, loginName).apply();
    }


}
