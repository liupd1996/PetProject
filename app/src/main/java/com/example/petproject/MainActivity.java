package com.example.petproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.petproject.adapter.MyFragmentPagerAdapter2;
import com.example.petproject.base.BaseActivity;
import com.example.petproject.bean.RefreshRequest;
import com.example.petproject.bean.RefreshResponse;
import com.example.petproject.bean.RegisterRequest;
import com.example.petproject.bean.RemoteResult;
import com.example.petproject.retrofit.ResultFunction;
import com.example.petproject.retrofit.RetrofitUtils;
import com.example.petproject.utils.ConfigPreferences;
import com.example.petproject.utils.ExceptionHandle;
import com.example.petproject.utils.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.pgyer.pgyersdk.PgyerSDKManager;
import com.pgyer.pgyersdk.callback.CheckoutCallBack;
import com.pgyer.pgyersdk.model.CheckSoftModel;

import java.util.ArrayList;
import java.util.List;

import constant.UiType;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import listener.Md5CheckResultListener;
import listener.UpdateDownloadListener;
import model.UiConfig;
import model.UpdateConfig;
import update.UpdateAppUtils;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (TextUtils.isEmpty(ConfigPreferences.login_token(this))) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            refreshToken();
        }
        checkPermission();
        //initView();
        UpdateAppUtils.init(this);
        PgyerSDKManager.checkVersionUpdate(new CheckoutCallBack() {
            @Override
            public void onNewVersionExist(CheckSoftModel model) {
                //检查版本成功（有新版本）
                Log.d("1111", "onNewVersionExist: " + model.toString());
                /**
                 *   CheckSoftModel 参数介绍
                 *
                 *    private int buildBuildVersion;//蒲公英生成的用于区分历史版本的build号
                 *     private String forceUpdateVersion;//强制更新版本号（未设置强置更新默认为空）
                 *     private String forceUpdateVersionNo;//强制更新的版本编号
                 *     private boolean needForceUpdate;//	是否强制更新
                 *     private boolean buildHaveNewVersion;//是否有新版本
                 *     private String downloadURL;//应用安装地址
                 *     private String buildVersionNo;//上传包的版本编号，默认为1 (即编译的版本号，一般来说，编译一次会
                 *    变动一次这个版本号, 在 Android 上叫 Version Code。对于 iOS 来说，是字符串类型；对于 Android 来
                 *    说是一个整数。例如：1001，28等。)
                 *     private String buildVersion;//版本号, 默认为1.0 (是应用向用户宣传时候用到的标识，例如：1.1、8.2.1等。)
                 *     private String buildShortcutUrl;//	应用短链接
                 *     private String buildUpdateDescription;//	应用更新说明
                 */
                UpdateConfig updateConfig = new UpdateConfig();
                updateConfig.setCheckWifi(true);
                updateConfig.setNeedCheckMd5(true);
                updateConfig.setAlwaysShow(true);//是否每次提醒更新
                updateConfig.setNotifyImgRes(R.mipmap.logo);
                UiConfig uiConfig = new UiConfig();
                uiConfig.setUiType(UiType.PLENTIFUL);
                UpdateAppUtils
                        .getInstance()
                        .apkUrl(model.getDownloadURL())
                        .updateTitle("版本更新")
                        .updateContent(model.getBuildDescription())
                        .uiConfig(uiConfig)
                        .updateConfig(updateConfig)
                        .setMd5CheckResultListener(result -> {
                            Log.d("1111", "onResult: " + result);
                            if (!result) {
                                ToastUtils.customToast(MainActivity.this, "签名校验失败");
                            }
                        })
                        .setUpdateDownloadListener(new UpdateDownloadListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onDownload(int progress) {

                            }

                            @Override
                            public void onFinish() {

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("1111", "onError: " + e.getMessage());
                                ToastUtils.customToast(MainActivity.this, "下载失败,请尝试扫码下载");
                            }
                        })
                        .update();
            }

            @Override
            public void onNonentityVersionExist(String string) {
                Log.d("1111", "onNonentityVersionExist: ");
                //无新版本
            }

            @Override
            public void onFail(String error) {
                //请求异常
                Log.d("1111", "onFail: ");
            }
        });
        Log.d("1111", "checkVersionUpdate: ");
    }



    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    private void initView() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(5);
        TabLayout tabLayout = findViewById(R.id.tabs);

        List<Fragment> fragments = new ArrayList<>();
        TabFragment1 fragment1 = TabFragment1.newInstance(1);
        //TabFragment2 fragment2 = TabFragment2.newInstance(2);
        TabFragment3 fragment3 = TabFragment3.newInstance(3);
        //TabFragment4 fragment4 = TabFragment4.newInstance(4);
        TabFragment5 fragment5 = TabFragment5.newInstance(5);
        fragments.add(fragment1);
        //fragments.add(fragment2);
        fragments.add(fragment3);
        //fragments.add(fragment4);
        fragments.add(fragment5);
        List<String> titles = new ArrayList<>();
        titles.add("数据");
        //titles.add("宠友圈");
        titles.add("地图");
        //titles.add("消息");
        titles.add("我的");
        MyFragmentPagerAdapter2 myFragmentPagerAdapter = new MyFragmentPagerAdapter2(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(myFragmentPagerAdapter);

        viewPager.setCurrentItem(1);

        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab tab1 = tabLayout.getTabAt(0);
        tab1.setIcon(R.drawable.data_selector); // 设置图标
        tab1.setText("数据");
        TabLayout.Tab tab2 = tabLayout.getTabAt(1);
        tab2.setText("地图");
        tab2.setIcon(R.drawable.map_selector); // 设置图标
        TabLayout.Tab tab3 = tabLayout.getTabAt(2);
        tab3.setText("我的");
        tab3.setIcon(R.drawable.me_selector); // 设置图标

//        tabLayout.setupWithViewPager(viewPager);
//        TabLayout.Tab tab1 = tabLayout.getTabAt(0);
//        tab1.setIcon(R.drawable.data_selector); // 设置图标
//        tab1.setText("数据");
//        TabLayout.Tab tab2 = tabLayout.getTabAt(1);
//        tab2.setText("宠友圈");
//        tab2.setIcon(R.drawable.friend_selector); // 设置图标
//        TabLayout.Tab tab3 = tabLayout.getTabAt(2);
//        tab3.setText("地图");
//        tab3.setIcon(R.drawable.map_selector); // 设置图标
//        TabLayout.Tab tab4 = tabLayout.getTabAt(3);
//        tab4.setText("消息");
//        tab4.setIcon(R.drawable.msg_selector); // 设置图标
//        TabLayout.Tab tab5 = tabLayout.getTabAt(4);
//        tab5.setText("我的");
//        tab5.setIcon(R.drawable.me_selector); // 设置图标
    }

    public String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.ACCESS_FINE_LOCATION
            ,Manifest.permission.ACCESS_COARSE_LOCATION
            ,Manifest.permission.ACCESS_NETWORK_STATE
            ,Manifest.permission.CAMERA
            };

    private boolean checkPermission() {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 1);
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void refreshToken() {
        String refresh_token = ConfigPreferences.refresh_token(this);
        if (TextUtils.isEmpty(refresh_token)) {
            return;
        }
        RetrofitUtils.getRetrofitService().refreshToken(refresh_token)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<RefreshResponse>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull RemoteResult<RefreshResponse> result) {
                        Log.d("111111111111111", "refreshToken***********************************: ");
                        ConfigPreferences.setLoginToken(MainActivity.this,result.data.accessToken);
                        ConfigPreferences.setRefreshToken(MainActivity.this,result.data.refreshToken);
                        //Log.d("11111111111", "onNext refreshToken: " + result.data.refreshToken);
                        //Log.d("11111111111", "onNext accessToken: " + result.data.accessToken);
                        initView();
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.d("11111111111", "onError refreshToken: ");
                        ExceptionHandle.ResponeThrowable responeThrowable = ExceptionHandle.handleException(e);
                        if (responeThrowable.code.equals("020000")) {
                            ConfigPreferences.setLoginName(MainActivity.this, "");
                            ConfigPreferences.setLoginToken(MainActivity.this, "");
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            ToastUtils.customToast(MainActivity.this, responeThrowable.message);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;
        if (1 == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }

            if (hasPermissionDismiss) {
                //
            } else {
                initView();
            }
        }
    }
}