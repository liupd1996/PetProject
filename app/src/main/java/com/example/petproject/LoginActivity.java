package com.example.petproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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
    private TextView tv_notify;
    private boolean mIsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    private void initView() {
        mEditName = findViewById(R.id.edit_account);
        mEditVerify = findViewById(R.id.verifyEditor);
        tv_notify = findViewById(R.id.tv_notify);
        mEditName.setText(ConfigPreferences.login_name(this));
        TextView btnVerify = findViewById(R.id.accessVerifyCode);
        mTimeCount = new TimeCount(60000, 1000, btnVerify);
        btnVerify.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mEditName.getText().toString())) {
                ToastUtils.continuousToast(LoginActivity.this, "手机号不能为空");
                tv_notify.setText("手机号不能为空");
                return;
            }
            if (!Utils.isMobileNumber(mEditName.getText().toString())) {
                ToastUtils.continuousToast(LoginActivity.this, "请输入正确手机号");
                tv_notify.setText("请输入正确手机号");
                return;
            }
            getVerify(mEditName.getText().toString());
        });
        findViewById(R.id.cl_title_bar).setBackground(null);


        findViewById(R.id.btn_login).setOnClickListener(v -> {
            String userName = mEditName.getText().toString();
            String verify = mEditVerify.getText().toString();
            if (TextUtils.isEmpty(userName)) {
                //ToastUtils.continuousToast(this, "请输入正确的手机号");
                tv_notify.setText("请输入正确的手机号");
                return;
            }
            if (TextUtils.isEmpty(verify)) {
                //ToastUtils.continuousToast(this, "请获取验证码");
                tv_notify.setText("请获取验证码");
                return;
            }

            if (verify.length() != 6) {
                //ToastUtils.continuousToast(this, "请获取验证码");
                tv_notify.setText("请输入6位验证码");
                return;
            }

            if (!mIsChecked) {
                //ToastUtils.continuousToast(this, "请阅读并同意用户协议");
                tv_notify.setText("请阅读并同意用户协议");
                return;
            }
            login(userName, verify);
        });

        CheckBox agreeCheckbox = findViewById(R.id.agreeCheckbox);

        agreeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mIsChecked = isChecked;
        });

        ImageButton button = findViewById(R.id.iv_back);
        button.setOnClickListener(v -> {
            onBackPressed();
        });

        initPrivacy();
    }

    private void initPrivacy() {
        TextView textView = findViewById(R.id.tv_privacy);

        // 设置原始文本
        String fullText = "我已阅读并同意《用户协议》，《隐私政策》 及 《宠友圈行为规范》";
        SpannableString spannableString = new SpannableString(fullText);

        // 获取《用户协议》在字符串中的起始和结束位置
        int startUserAgreement = fullText.indexOf("《用户协议》");
        int endUserAgreement = startUserAgreement + "《用户协议》".length();

        // 获取《隐私政策》在字符串中的起始和结束位置
        int startPrivacyPolicy = fullText.indexOf("《隐私政策》");
        int endPrivacyPolicy = startPrivacyPolicy + "《隐私政策》".length();

        // 获取《宠友圈行为规范》在字符串中的起始和结束位置
        int startBehaviorRules = fullText.indexOf("《宠友圈行为规范》");
        int endBehaviorRules = startBehaviorRules + "《宠友圈行为规范》".length();

        // 设置 ClickableSpan，实现点击事件
        ClickableSpan userAgreementSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 处理点击《用户协议》事件
                openLink("http://petminder.cn/yhxyjsytk");
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);  // 取消下划线
                ds.setColor(Color.parseColor("#6C97DA"));  // 设置颜色
            }
        };

        ClickableSpan privacyPolicySpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 处理点击《隐私政策》事件
                openLink("http://petminder.cn/yszc");
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);  // 取消下划线
                ds.setColor(Color.parseColor("#6C97DA"));  // 设置颜色
            }
        };

        ClickableSpan behaviorRulesSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 处理点击《宠友圈行为规范》事件
                openLink("http://petminder.cn/hwgf");
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);  // 取消下划线
                ds.setColor(Color.parseColor("#6C97DA"));  // 设置颜色
            }
        };

        // 将 ClickableSpan 应用到 SpannableString
        spannableString.setSpan(userAgreementSpan, startUserAgreement, endUserAgreement, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(privacyPolicySpan, startPrivacyPolicy, endPrivacyPolicy, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(behaviorRulesSpan, startBehaviorRules, endBehaviorRules, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置整体样式，包括颜色和下划线
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(this,0);
        spannableString.setSpan(textAppearanceSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 将 SpannableString 设置到 TextView，并启用点击事件
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void openLink(String url) {
        // 打开链接的逻辑，可以根据需要自定义
        Intent intent = new Intent(LoginActivity.this,WebviewActivity.class);
        intent.putExtra("result",url);
        startActivity(intent);
    }

    private void login(String userName, String verify) {
        RetrofitUtils.getRetrofitService().login(new LoginRequest(userName, 2, verify))
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<LoginResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<LoginResponse> result) {
                        if (result == null || result.data == null) {
                            return;
                        }
                        Log.d(TAG, "onNext: " + result.data.toString());
                        ConfigPreferences.setLoginName(LoginActivity.this, userName);
                        ConfigPreferences.setLoginToken(LoginActivity.this, result.data.accessToken);
                        ConfigPreferences.setRefreshToken(LoginActivity.this, result.data.refreshToken);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        mTimeCount.onFinish();
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ExceptionHandle.ResponeThrowable responeThrowable = ExceptionHandle.handleException(e);
                        Log.d(TAG, "message**********************************: " + responeThrowable.message);
                        if (responeThrowable.code.equals("020003")) {//020003为手机号未注册的情况、020005为验证码问题
                            Intent intent = new Intent(LoginActivity.this, UserCenterActivity.class);
                            intent.putExtra("phone", userName);
                            intent.putExtra("smsCode", verify);
                            startActivity(intent);
                        } else if (responeThrowable.code.equals("020005")) {
                            Log.d(TAG, "handleException 验证码问题: ");
                            tv_notify.setText("验证码错误或验证码已失效");
                        } else {
                            tv_notify.setText("系统错误");
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
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
                        //ToastUtils.customToast(LoginActivity.this, ExceptionHandle.handleException(e).message);
                        tv_notify.setText(ExceptionHandle.handleException(e).message);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}