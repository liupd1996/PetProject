package com.example.petproject.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petproject.R;


public class ToastUtils {
    public static void customToast(Context context, String string) {
        Toast toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(context).inflate(R.layout
                .layout_toast, null, false);
        TextView tvToast = view.findViewById(R.id.tv_toast);
        tvToast.setText(string);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private static Toast mToast;
    private static TextView tvToast;

    public static void continuousToast(Context context, String string) {
        if (mToast == null) {
            mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            View view = LayoutInflater.from(context).inflate(R.layout
                    .layout_toast, null, false);
            tvToast = view.findViewById(R.id.tv_toast);
            mToast.setView(view);
        }
        tvToast.setText(string);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }
}
