package com.example.petproject;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.petproject.base.BaseActivity;
import com.example.petproject.utils.ConfigPreferences;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageButton button = findViewById(R.id.iv_back);
        button.setOnClickListener(v -> {
            onBackPressed();
        });
        button.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        TextView title = findViewById(R.id.tv_bar_title);
        title.setText("设置");

        findViewById(R.id.btn_exit).setOnClickListener(v -> {
            ConfigPreferences.setLoginName(this, "");
            ConfigPreferences.setLoginToken(this, "");
            startActivity(new Intent(SettingActivity.this, LoginActivity.class));
            finish();
        });

    }

    @Override
    public int getContentView() {
        return R.layout.activity_setting;
    }


}