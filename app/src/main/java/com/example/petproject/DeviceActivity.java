package com.example.petproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petproject.adapter.DeviceAdapter;
import com.example.petproject.base.BaseActivity;
import com.example.petproject.bean.DeviceResponse;
import com.example.petproject.bean.RemoteResult;
import com.example.petproject.retrofit.ResultFunction;
import com.example.petproject.retrofit.RetrofitUtils;
import com.example.petproject.utils.ConfigPreferences;
import com.example.petproject.utils.ExceptionHandle;
import com.example.petproject.utils.ToastUtils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DeviceActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private List<DeviceResponse> list;
    private DeviceAdapter mAdapter;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageButton button = findViewById(R.id.iv_back);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(DeviceActivity.this, MainActivity.class);
            startActivity(intent);
        });
        TextView title = findViewById(R.id.tv_bar_title);
        title.setText("绑定设备");

        findViewById(R.id.btn_bind).setOnClickListener(view -> {
            Intent intent = new Intent(DeviceActivity.this, BindActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.btn_bind2).setOnClickListener(view -> {
            Intent intent = new Intent(DeviceActivity.this, BindActivity.class);
            startActivity(intent);
        });

        mRecyclerView = findViewById(R.id.rv_devices);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DeviceAdapter(this);
        mAdapter.setOnItemClickListener((bean, position) -> {
            Intent intent = new Intent(this, DeviceInfoActivity.class);
            intent.putExtra("bean", bean);
            startActivity(intent);
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = "Bearer " + ConfigPreferences.login_token(DeviceActivity.this);
        search(token);
    }

    private void search(String token) {
        RetrofitUtils.getRetrofitService().deviceSearch(token)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())//todo filter
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<List<DeviceResponse>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<List<DeviceResponse>> result) {
                        list = result.data;
                        if (list != null && list.size() != 0) {
                            mAdapter.setList(result.data);
                            Log.d("search", "onNext: " + result.data.toString());
                            findViewById(R.id.cl_devices).setVisibility(View.VISIBLE);
                            findViewById(R.id.no_devices).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.cl_devices).setVisibility(View.GONE);
                            findViewById(R.id.no_devices).setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ToastUtils.customToast(DeviceActivity.this, ExceptionHandle.handleException(e).message);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DeviceActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_device;
    }
}