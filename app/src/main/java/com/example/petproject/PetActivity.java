package com.example.petproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.petproject.base.BaseActivity;
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
        findViewById(R.id.btn_add).setOnClickListener(view -> {
            Intent intent = new Intent(PetActivity.this,AddPetActivity.class);
            startActivity(intent);
        });

        String token = "Bearer " + ConfigPreferences.login_token(PetActivity.this);
        search(token);
    }

    private void search(String token) {
        RetrofitUtils.getRetrofitService().petSearch(token)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<Object>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<Object> result) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ToastUtils.customToast(PetActivity.this, ExceptionHandle.handleException(e).message);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_pet;
    }
}