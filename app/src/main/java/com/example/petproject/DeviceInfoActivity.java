package com.example.petproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.petproject.base.BaseActivity;
import com.example.petproject.bean.DeleteDeviceRequest;
import com.example.petproject.bean.DeviceResponse;
import com.example.petproject.bean.RemoteResult;
import com.example.petproject.dialog.SureCancelDialog;
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

public class DeviceInfoActivity extends BaseActivity {
    private static final String TAG = "DeviceInfoActivity";
    String deviceId;
    String petId;
    private SureCancelDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = SureCancelDialog.newInstance("是否删除宠物", "取消", "确定");
        mDialog.setOnCancelListener(new SureCancelDialog.OnSureCancelListener() {
            @Override
            public void onCancel() {
                mDialog.dismiss();
            }

            @Override
            public void onSureListener(String text) {
                String token = "Bearer " + ConfigPreferences.login_token(DeviceInfoActivity.this);
                deviceDelete(token);
            }
        });

        ImageButton button = findViewById(R.id.iv_back);
        button.setOnClickListener(v -> {
            onBackPressed();
        });
        TextView title = findViewById(R.id.tv_bar_title);
        title.setText("设备详情");

        Intent intent = getIntent();
        DeviceResponse deviceResponse = (DeviceResponse) intent.getParcelableExtra("bean");
        deviceId = deviceResponse.deviceId;
        petId = deviceResponse.petId;
        Log.d(TAG, "deviceResponse: " + deviceResponse.toString());

        findViewById(R.id.cl_delete).setOnClickListener(view -> {
            mDialog.show(getSupportFragmentManager(), "deleteDevice");
        });
    }


    private void deviceDelete(String token) {
        if (TextUtils.isEmpty(deviceId)) {
            return;
        }
        if (!TextUtils.isEmpty(petId)) {
            ToastUtils.customToast(DeviceInfoActivity.this, "请先解绑宠物");
            return;
        }
        RetrofitUtils.getRetrofitService().deleteDevice(token, new DeleteDeviceRequest(deviceId))
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())//todo add edit
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<Object>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<Object> result) {
                        ToastUtils.customToast(DeviceInfoActivity.this, "删除成功");
                        Intent intent = new Intent(DeviceInfoActivity.this, DeviceActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ToastUtils.customToast(DeviceInfoActivity.this, ExceptionHandle.handleException(e).message);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_device_info;
    }
}