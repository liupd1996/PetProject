package com.example.petproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petproject.adapter.PetAdapter;
import com.example.petproject.base.BaseActivity;
import com.example.petproject.bean.PetRequest;
import com.example.petproject.bean.RemoteResult;
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

public class PetActivity extends BaseActivity {
    private PetAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<PetRequest> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageButton button = findViewById(R.id.iv_back);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(PetActivity.this, MainActivity.class);
            startActivity(intent);
        });
        TextView title = findViewById(R.id.tv_bar_title);
        title.setText("我的宠物");
        findViewById(R.id.btn_add).setOnClickListener(view -> {
            if (list != null && list.size() < 4) {
                Intent intent = new Intent(PetActivity.this, AddPetActivity.class);
                startActivity(intent);
            } else {
                ToastUtils.customToast(PetActivity.this, "最多只支持四只宠物");
            }
        });

        mRecyclerView = findViewById(R.id.rv_pet);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PetAdapter(this);
        mAdapter.setOnItemClickListener((bean, position) -> {
            Intent intent = new Intent(this, AddPetActivity.class);
            intent.putExtra("bean", bean);
            startActivity(intent);
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PetActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = "Bearer " + ConfigPreferences.login_token(PetActivity.this);
        search(token);
    }

    private void search(String token) {
        RetrofitUtils.getRetrofitService().petSearch(token)
                .subscribeOn(Schedulers.io())//todo filter
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<List<PetRequest>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<List<PetRequest>> result) {
                        Log.d("search", "onNext: " + result.data.toString());
                        list = result.data;
                        mAdapter.setList(result.data);
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