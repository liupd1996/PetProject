package com.example.petproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.petproject.base.BaseActivity;
import com.example.petproject.bean.DeleteDeviceRequest;
import com.example.petproject.bean.DevicePetRequest;
import com.example.petproject.bean.DeviceResponse;
import com.example.petproject.bean.PetResponse;
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
    private SureCancelDialog mDialogUnbind;
    private static final int REQUEST_CODE = 123;
    private PetResponse petRequest;
    private TextView bind_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind_state = findViewById(R.id.tv_bind_state);
        mDialog = SureCancelDialog.newInstance("是否删除宠物", "取消", "确定");
        mDialogUnbind = SureCancelDialog.newInstance("是否解绑宠物", "取消", "确定");
        mDialogUnbind.setOnCancelListener(new SureCancelDialog.OnSureCancelListener() {
            @Override
            public void onCancel() {
                mDialogUnbind.dismiss();
            }

            @Override
            public void onSureListener(String text) {
                String token = "Bearer " + ConfigPreferences.login_token(DeviceInfoActivity.this);
                unbindPet(token);
            }
        });
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
        if (TextUtils.isEmpty(petId)) {
            bind_state.setText("未绑定");
            bind_state.setTextColor(Color.RED);
        } else {
            bind_state.setText("已绑定");
            bind_state.setTextColor(Color.GREEN);
        }
        findViewById(R.id.btn_bind_pet).setOnClickListener(view -> {
            if (TextUtils.isEmpty(petId)) {
                Intent intent1 = new Intent(DeviceInfoActivity.this, PetActivity.class);
                intent1.putExtra("type",1);
                startActivityForResult(intent1, REQUEST_CODE);
            } else {
                ToastUtils.customToast(DeviceInfoActivity.this, "该设备已绑定宠物");
            }
        });

        findViewById(R.id.btn_unbind).setOnClickListener(view -> {
            if (TextUtils.isEmpty(petId)) {
                ToastUtils.customToast(DeviceInfoActivity.this, "未绑定宠物");
            } else {
                mDialogUnbind.show(getSupportFragmentManager(), "unBind");
            }
        });

        findViewById(R.id.cl_delete).setOnClickListener(view -> {
            mDialog.show(getSupportFragmentManager(), "deleteDevice");
        });

        findViewById(R.id.cl_close).setOnClickListener(view -> {
            ToastUtils.customToast(DeviceInfoActivity.this, "功能维护中");
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // 处理成功返回的数据
                petRequest = (PetResponse) data.getParcelableExtra("result_key");
                String token = "Bearer " + ConfigPreferences.login_token(DeviceInfoActivity.this);
                bindPet(token);
                // 在这里使用 resultData
            } else if (resultCode == RESULT_CANCELED) {
                // 处理取消操作
            }
        }
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
                        String message = ExceptionHandle.handleException(e).message;
                        if (message.equals("invalid_token")) {
                            ConfigPreferences.setLoginName(DeviceInfoActivity.this, "");
                            ConfigPreferences.setLoginToken(DeviceInfoActivity.this, "");
                            startActivity(new Intent(DeviceInfoActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            ToastUtils.customToast(DeviceInfoActivity.this, message);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void bindPet(String token) {
        RetrofitUtils.getRetrofitService().deviceBindPet(token, new DevicePetRequest(deviceId, petRequest.id))
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())//todo add edit
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<Object>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<Object> result) {
                        ToastUtils.customToast(DeviceInfoActivity.this, "绑定成功");
                        petId = petRequest.id;
                        bind_state.setText("已绑定");
                        bind_state.setTextColor(Color.GREEN);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        String message = ExceptionHandle.handleException(e).message;
                        if (message.equals("invalid_token")) {
                            ConfigPreferences.setLoginName(DeviceInfoActivity.this, "");
                            ConfigPreferences.setLoginToken(DeviceInfoActivity.this, "");
                            startActivity(new Intent(DeviceInfoActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            ToastUtils.customToast(DeviceInfoActivity.this, message);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //取消绑定
    private void unbindPet(String token) {
        RetrofitUtils.getRetrofitService().deviceUnBindPet(token, new DevicePetRequest(deviceId, petId))
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())//todo add edit
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<Object>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<Object> result) {
                        ToastUtils.customToast(DeviceInfoActivity.this, "解绑成功");
                        petId = null;
                        bind_state.setText("未绑定");
                        bind_state.setTextColor(Color.RED);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        String message = ExceptionHandle.handleException(e).message;
                        if (message.equals("invalid_token")) {
                            ConfigPreferences.setLoginName(DeviceInfoActivity.this, "");
                            ConfigPreferences.setLoginToken(DeviceInfoActivity.this, "");
                            startActivity(new Intent(DeviceInfoActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            ToastUtils.customToast(DeviceInfoActivity.this, message);
                        }
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