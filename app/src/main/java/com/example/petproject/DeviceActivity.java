package com.example.petproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.petproject.base.BaseActivity;

public class DeviceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageButton button = findViewById(R.id.iv_back);
        button.setOnClickListener(v -> {
            onBackPressed();
        });
        TextView title = findViewById(R.id.tv_bar_title);
        title.setText("绑定设备");

        findViewById(R.id.btn_bind).setOnClickListener(view -> {
            Intent intent = new Intent(DeviceActivity.this,BindActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_device;
    }
}