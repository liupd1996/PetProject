package com.example.petproject.utils;

import android.text.TextUtils;

public class Utils {
    private static final String TAG = "Utils";

    public static void split(String result) {
        String[] strings = result.split("-");
    }

    public static boolean isMobileNumber(String mobiles) {
        String telRegex = "^[1]\\d{10}$";
        //String telRegex = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }
}
