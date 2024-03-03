package com.example.petproject;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.petproject.base.BaseActivity;

public class DataActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageButton button = findViewById(R.id.iv_back);
        button.setOnClickListener(v -> {
            onBackPressed();
        });
        button.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        TextView title = findViewById(R.id.tv_bar_title);
        title.setText("数据详情");

        Intent intent = getIntent();
        int type = intent.getIntExtra("type",-1);
        switch (type) {
            case 1:
                title.setText("体温数据");
                break;
            case 2:
                title.setText("体温数据");
                break;
            case 3:
                title.setText("体温数据");
                break;
            case 4:
                title.setText("体温数据");
                break;
            case 5:
                title.setText("体温数据");
                break;
            default:
                break;
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_data;
    }
}