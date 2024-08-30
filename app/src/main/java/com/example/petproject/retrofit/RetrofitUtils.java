package com.example.petproject.retrofit;


import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    private static final String TAG = "RetrofitUtils";

    public static RetrofitService getRetrofitService() {
        return new Retrofit.Builder()
                .baseUrl("http://47.94.99.63:8088")//http://192.168.1.34:8097 http://prod-cn.your-api-server.com
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .build()
                .create(RetrofitService.class);
    }

    /**
     * 获取OkHttpClient
     * 用于打印请求参数
     *
     * @return OkHttpClient
     */

    public static class TokenHeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            String token =""; //SpUtils是SharedPreferences的工具类，自行实现
            Request originalRequest = chain.request();
            if (token.isEmpty()) {
                return chain.proceed(originalRequest);
            }else {
                Request updateRequest = originalRequest.newBuilder()
                        .addHeader("Content-Type","application/json; charset=UTF-8")
                        .addHeader("Authorization", token)
                        //.header("Authorization", token)
                        .build();
                return chain.proceed(updateRequest);
            }
        }
    }

    public static OkHttpClient getOkHttpClient() {
        // 日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        // 新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("TestOkhttp：", message);
            }
        });
        loggingInterceptor.setLevel(level);
        // 定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        // OkHttp进行添加拦截器loggingInterceptor
        //httpClientBuilder.addNetworkInterceptor(new TokenHeaderInterceptor());
        httpClientBuilder.addInterceptor(loggingInterceptor);
        httpClientBuilder.connectTimeout(2000, TimeUnit.MILLISECONDS)
                .readTimeout(2000, TimeUnit.MILLISECONDS)
                .writeTimeout(2000, TimeUnit.MILLISECONDS);
        return httpClientBuilder.build();
    }
}
