package com.example.petproject;

import android.app.Application;
import android.content.Context;

import com.pgyer.pgyersdk.PgyerSDKManager;
import com.pgyer.pgyersdk.pgyerenum.Features;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        initPgyerSDK(this);
    }

    /**
     *  初始化蒲公英SDK
     */
    private static void initPgyerSDK(MyApp application){
        new PgyerSDKManager.Init()
                .setContext(application) //设置上下问对象
                .enable(Features.CHECK_UPDATE)//开启自动更新检测
                .start();
    }
}
