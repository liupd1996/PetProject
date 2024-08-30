package com.example.petproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petproject.adapter.PetAdapter;
import com.example.petproject.base.BaseActivity;
import com.example.petproject.bean.PetResponse;
import com.example.petproject.bean.RemoteResult;
import com.example.petproject.retrofit.ResultFunction;
import com.example.petproject.retrofit.RetrofitUtils;
import com.example.petproject.utils.ConfigPreferences;
import com.example.petproject.utils.ExceptionHandle;
import com.example.petproject.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PetActivity extends BaseActivity {
    private PetAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<PetResponse> list;
    private int type;
    private static final int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageButton button = findViewById(R.id.iv_back);
        button.setOnClickListener(v -> {
//            Intent intent = new Intent(PetActivity.this, MainActivity.class);
//            startActivity(intent);
            super.onBackPressed();
        });
        TextView title = findViewById(R.id.tv_bar_title);
        title.setText("我的宠物");
        findViewById(R.id.btn_add).setOnClickListener(view -> {
            if (list != null && list.size() < 4) {
                Intent intent = new Intent(PetActivity.this, AddPetActivity.class);
                intent.putExtra("type",1);
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                ToastUtils.customToast(PetActivity.this, "最多只支持四只宠物");
            }
        });

        Intent intentResult = getIntent();
        type = intentResult.getIntExtra("type", -1);
        Log.d("11111111", "onCreate: " + type);
        mRecyclerView = findViewById(R.id.rv_pet);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PetAdapter(this);
        mAdapter.setOnItemClickListener((bean, position) -> {
            if (type == 1) {//设备绑定宠物返回petId
                Intent resultIntent = new Intent(PetActivity.this, DeviceInfoActivity.class);
                resultIntent.putExtra("result_key", bean);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Intent intent = new Intent(this, AddPetActivity.class);
                intent.putExtra("bean", bean);
                startActivity(intent);
                finish();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(PetActivity.this, MainActivity.class);
//        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = "Bearer " + ConfigPreferences.login_token(PetActivity.this);
        search(token);
    }

    private void search(String token) {
        RetrofitUtils.getRetrofitService().petSearch(token)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())//todo filter
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<List<PetResponse>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<List<PetResponse>> result) {
                        Log.d("11111111", "onNext: " + type);
                        if (type == 1) {
                            list = filterList(result.data);
                        } else {
                            list = result.data;
                        }
                        mAdapter.setList(list);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ExceptionHandle.ResponeThrowable responeThrowable = ExceptionHandle.handleException(e);
                        if (responeThrowable.code.equals("020000")) {
                            ConfigPreferences.setLoginName(PetActivity.this, "");
                            ConfigPreferences.setLoginToken(PetActivity.this, "");
                            startActivity(new Intent(PetActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            ToastUtils.customToast(PetActivity.this, responeThrowable.message);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private List<PetResponse> filterList(List<PetResponse> data) {
        list = new ArrayList<>();
        for (PetResponse response : data) {
            Log.d("11111111", "filterList: " + response.deviceId);
            if (TextUtils.isEmpty(response.deviceId) || response.deviceId.equals("0")) {
                list.add(response);
            }
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("1111", "onActivityResult: ");
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // 处理成功返回的数据
                // 在这里使用 resultData
            } else if (resultCode == RESULT_CANCELED) {
                // 处理取消操作
            }
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_pet;
    }
}