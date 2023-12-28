package com.example.petproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.petproject.base.BaseActivity;
import com.example.petproject.bean.LoginRequest;
import com.example.petproject.bean.LoginResponse;
import com.example.petproject.bean.RegisterRequest;
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

public class UserCenterActivity extends BaseActivity {
    private static final String TAG = "UserCenterActivity";
    private TextView mTvGender;
    private TextView mTvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_user_center;
    }

    private void initView() {
        mTvName = findViewById(R.id.tv_name);
        mTvGender = findViewById(R.id.tv_gender);
        TextView title = findViewById(R.id.tv_bar_title);
        title.setText("个人中心");
        mTvName.setText("test");
        findViewById(R.id.cl_title_bar).setBackgroundColor(Color.WHITE);
        findViewById(R.id.iv_back).setOnClickListener(v -> {
            onBackPressed();
        });
        findViewById(R.id.btn_login_out).setOnClickListener(v -> {
            ConfigPreferences.setLoginToken(this, "");
            startActivity(new Intent(UserCenterActivity.this, LoginActivity.class));
            finish();
        });
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        String smsCode = intent.getStringExtra("smsCode");
        register(phone,smsCode,"test");
    }

    private void register(String phone, String smsCode, String name) {
        RetrofitUtils.getRetrofitService().register(new RegisterRequest("", phone, smsCode, name))
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<Object>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<Object> result) {
                        login(phone, smsCode);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ToastUtils.customToast(UserCenterActivity.this, ExceptionHandle.handleException(e).message);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void login(String userName, String verify) {
        RetrofitUtils.getRetrofitService().login(new LoginRequest(userName, verify))
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<LoginResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<LoginResponse> result) {
                        ConfigPreferences.setLoginName(UserCenterActivity.this, userName);
                        ConfigPreferences.setLoginToken(UserCenterActivity.this, result.data.refresh_token);
                        startActivity(new Intent(UserCenterActivity.this, MainActivity.class));
                        ToastUtils.customToast(UserCenterActivity.this, "登录成功");
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ToastUtils.customToast(UserCenterActivity.this, ExceptionHandle.handleException(e).message);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}