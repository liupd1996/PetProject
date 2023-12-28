package com.example.petproject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.petproject.base.BaseActivity;
import com.example.petproject.bean.LoginRequest;
import com.example.petproject.bean.LoginResponse;
import com.example.petproject.bean.RemoteResult;
import com.example.petproject.retrofit.ResultFunction;
import com.example.petproject.retrofit.RetrofitUtils;
import com.example.petproject.utils.ConfigPreferences;
import com.example.petproject.utils.ExceptionHandle;
import com.example.petproject.utils.TimeCount;
import com.example.petproject.utils.ToastUtils;
import com.example.petproject.utils.Utils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private EditText mEditName;
    private EditText mEditVerify;
    private TimeCount mTimeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    private void initView() {
        mEditName = findViewById(R.id.edit_account);
        mEditVerify = findViewById(R.id.verifyEditor);
        mEditName.setText(ConfigPreferences.login_name(this));
        TextView btnVerify = findViewById(R.id.accessVerifyCode);
        mTimeCount = new TimeCount(60000, 1000, btnVerify);
        btnVerify.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mEditName.getText().toString())) {
                ToastUtils.continuousToast(LoginActivity.this, "手机号不能为空");
                return;
            }
            if (!Utils.isMobileNumber(mEditName.getText().toString())) {
                ToastUtils.continuousToast(LoginActivity.this, "请输入正确手机号");
                return;
            }
            getVerify(mEditName.getText().toString());
        });
        findViewById(R.id.cl_title_bar).setBackground(null);


        findViewById(R.id.btn_login).setOnClickListener(v -> {
            String userName = mEditName.getText().toString();
            String verify = mEditVerify.getText().toString();
            if (TextUtils.isEmpty(userName)) {
                ToastUtils.continuousToast(this, "账号不能为空");
                return;
            }
            if (TextUtils.isEmpty(verify)) {
                ToastUtils.continuousToast(this, "验证码不能为空");
                return;
            }
            login(userName, verify);
        });
        ImageButton button = findViewById(R.id.iv_back);
        button.setOnClickListener(v -> {
            onBackPressed();
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
                        ConfigPreferences.setLoginName(LoginActivity.this, userName);
                        ConfigPreferences.setLoginToken(LoginActivity.this, result.data.refresh_token);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        //ToastUtils.customToast(LoginActivity.this, ExceptionHandle.handleException(e).message);
                        Intent intent = new Intent(LoginActivity.this, UserCenterActivity.class);
                        intent.putExtra("phone", userName);
                        intent.putExtra("smsCode", verify);
                        startActivity(intent);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    private void start() {
        //Intent intent = getPackageManager().getLaunchIntentForPackage("com.zte.appstore.ui");//根据intent是否为空来判断是否安装应用
        try {
            Intent intent = new Intent();
            intent.setAction("com.appstore.manager.login.action");
            intent.setPackage("com.zte.appstore.ui");
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException exception) {
            ToastUtils.customToast(this, "未找到指定应用");
        }
    }

    @Override
    public void onBackPressed() {
        try {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
        } catch (Exception e) {
            Log.e(TAG, "start HOME error: ");
        }
    }

    private void getVerify(String phone) {
        RetrofitUtils.getRetrofitService().getVerify(phone)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<Object>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult result) {
                        mTimeCount.start();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ToastUtils.customToast(LoginActivity.this, ExceptionHandle.handleException(e).message);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}