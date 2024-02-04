package com.example.petproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.petproject.bean.RemoteResult;
import com.example.petproject.bean.UserInfoResponse;
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

public class TabFragment5 extends Fragment {
    private static final String TAG = "TabFragment1";
    private View view;
    private TextView tv_name;
    private TextView tv_id;

    public TabFragment5() {
        // Required empty public constructor
    }

    public static TabFragment5 newInstance(int id) {
        TabFragment5 fragment = new TabFragment5();
        Bundle args = new Bundle();
        args.putInt("ID", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab5, container, false);
        ConstraintLayout ll_device = view.findViewById(R.id.cl_device);
        ll_device.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), DeviceActivity.class);
            startActivity(intent);
        });
        ConstraintLayout ll_pet = view.findViewById(R.id.cl_pet);
        ll_pet.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PetActivity.class);
            startActivity(intent);
        });

        ConstraintLayout ll_setting = view.findViewById(R.id.cl_setting);
        ll_setting.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        });
        tv_name = view.findViewById(R.id.tv_name);
        tv_id = view.findViewById(R.id.tv_id);
        view.findViewById(R.id.iv_head).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),UserCenterActivity.class);
            intent.putExtra("type",1);
            startActivity(intent);
        });
        String token = "Bearer " + ConfigPreferences.login_token(getContext());
        getDetail(token);

        return view;
    }

    private void getDetail(String token) {
        RetrofitUtils.getRetrofitService().getDetail(token)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())//todo filter
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<UserInfoResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<UserInfoResponse> result) {
                        Log.d(TAG, "onNext: " + result.data.toString());
                        ConfigPreferences.setName(getContext(), result.data.username);
                        if (result.data.gender == 0) {
                            ConfigPreferences.setGender(getContext(), "男");
                        } else if (result.data.gender == 1) {
                            ConfigPreferences.setGender(getContext(), "女");
                        } else {
                            ConfigPreferences.setGender(getContext(), "未知");
                        }
                        tv_name.setText(result.data.username);
                        tv_id.setText("ID：" + result.data.id);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        String message = ExceptionHandle.handleException(e).message;
                        if (message.equals("invalid_token")) {
                            ConfigPreferences.setLoginName(getContext(), "");
                            ConfigPreferences.setLoginToken(getContext(), "");
                            startActivity(new Intent(getContext(), LoginActivity.class));
                            if (getActivity() != null) {
                                getActivity().finish();
                            }
                        } else {
                            ToastUtils.customToast(getContext(), message);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}