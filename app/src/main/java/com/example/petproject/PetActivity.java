package com.example.petproject;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.petproject.base.BaseActivity;

public class PetActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageButton button = findViewById(R.id.iv_back);
        button.setOnClickListener(v -> {
            onBackPressed();
        });
        TextView title = findViewById(R.id.tv_bar_title);
        title.setText("我的宠物");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_pet;
    }
}