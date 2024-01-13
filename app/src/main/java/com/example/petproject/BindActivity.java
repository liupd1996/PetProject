package com.example.petproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.petproject.base.BaseActivity;
import com.example.petproject.utils.ToastUtils;

public class BindActivity extends BaseActivity {
    private static final String TAG = "BindActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageButton button = findViewById(R.id.iv_back);
        button.setOnClickListener(v -> {
            onBackPressed();
        });
        TextView title = findViewById(R.id.tv_bar_title);
        title.setText("绑定设备");
        EditText editText = findViewById(R.id.et_device);
        findViewById(R.id.btn_bind).setOnClickListener(view -> {
            Intent intent = new Intent(BindActivity.this, DeviceActivity.class);
            String device_id = editText.getText().toString();
            if (TextUtils.isEmpty(device_id) || device_id.length() != 6) {
                ToastUtils.customToast(BindActivity.this,"请输入六位数字");
            } else {
                intent.putExtra("device_id", device_id);
                Log.d(TAG, "onCreate: " + device_id);
                startActivity(intent);
            }
        });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_bind;
    }
}