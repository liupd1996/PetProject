package com.example.petproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigPreferences {
    public static String LOGIN_TOKEN = "login_token";
    public static String REFRESH_TOKEN = "refresh_token";
    public static String LOGIN_NAME = "login_name";
    public static String NAME = "name";
    public static String AVATAR = "avatar";
    public static String PET_AVATAR = "pet_avatar";
    public static String _ID = "_id";
    public static String GENDER = "gender";
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

    //手机号
    public static void setLoginName(Context context, String loginName) {
        getInstance(context).edit().putString(LOGIN_NAME, loginName).apply();
    }

    //id
    public static void setID(Context context, String id) {
        getInstance(context).edit().putString(_ID, id).apply();
    }

    //昵称
    public static void setName(Context context, String name) {
        getInstance(context).edit().putString(NAME, name).apply();
    }

    //头像
    public static void setAvatar(Context context, String name) {
        getInstance(context).edit().putString(AVATAR, name).apply();
    }

    public static void setPetAvatar(Context context, String name) {
        getInstance(context).edit().putString(PET_AVATAR, name).apply();
    }

    //获取名称
    public static String name(Context context) {
        return getInstance(context).getString(NAME, "");
    }

    //获取头像名称
    public static String avatar(Context context) {
        return getInstance(context).getString(AVATAR, "");
    }

    //获取头像名称
    public static String petAvatar(Context context) {
        return getInstance(context).getString(PET_AVATAR, "");
    }

    //获取用户id
    public static String _id(Context context) {
        return getInstance(context).getString(_ID, "");
    }

    //昵称
    public static void setGender(Context context, String name) {
        getInstance(context).edit().putString(GENDER, name).apply();
    }

    //获取
    public static String gender(Context context) {
        return getInstance(context).getString(GENDER, "");
    }


}
