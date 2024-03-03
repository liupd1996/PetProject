package com.example.petproject;

import android.os.Bundle;

import com.example.petproject.base.BaseActivity;

public class WeightActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_weight;
    }


}