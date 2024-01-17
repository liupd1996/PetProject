package com.example.petproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.petproject.base.BaseActivity;
import com.example.petproject.bean.DeviceRequest;
import com.example.petproject.bean.RemoteResult;
import com.example.petproject.retrofit.ResultFunction;
import com.example.petproject.retrofit.RetrofitUtils;
import com.example.petproject.utils.ConfigPreferences;
import com.example.petproject.utils.ExceptionHandle;
import com.example.petproject.utils.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
            String device_id = editText.getText().toString();
            if (TextUtils.isEmpty(device_id) || device_id.length() != 11) {
                ToastUtils.customToast(BindActivity.this, "请输入11位数字");
            } else {
                try {
                    //todo check String or int
                    DeviceRequest request = new DeviceRequest(device_id);//10069096400 / todo 10069096996
                    String token = "Bearer " + ConfigPreferences.login_token(BindActivity.this);
                    deviceAdd(token, request);
                    // 在这里使用 intValue
                } catch (NumberFormatException e) {
                    // 处理转换失败的情况，比如字符串不是有效的整数
                    e.printStackTrace();
                }

            }
        });
    }

    private void deviceAdd(String token, DeviceRequest request) {
        RetrofitUtils.getRetrofitService().addDevice(token, request)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())//todo add edit
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<Object>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<Object> result) {
                        ToastUtils.customToast(BindActivity.this, "添加成功");
                        Intent intent = new Intent(BindActivity.this, DeviceActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        String message = ExceptionHandle.handleException(e).message;
                        if (message.equals("invalid_token")) {
                            ConfigPreferences.setLoginName(BindActivity.this, "");
                            ConfigPreferences.setLoginToken(BindActivity.this, "");
                            startActivity(new Intent(BindActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            ToastUtils.customToast(BindActivity.this, message);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    @Override
    public int getContentView() {
        return R.layout.activity_bind;
    }
}