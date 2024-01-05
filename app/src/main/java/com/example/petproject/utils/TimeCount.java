package com.example.petproject.utils;


import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

public class TimeCount extends CountDownTimer {

    private final TextView tv_count;

    public TimeCount(long millisInFuture, long countDownInterval, TextView tv_count) {
        super(millisInFuture, countDownInterval);
        this.tv_count = tv_count;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        tv_count.setEnabled(false);
        String text = millisUntilFinished / 1000 + "秒";
        Log.d("1111", "onTick: " + text);
        tv_count.setText(text);
        tv_count.invalidate();
    }

    @Override
    public void onFinish() {
        tv_count.setEnabled(true);
        tv_count.setText("获取验证码");

    }

}

