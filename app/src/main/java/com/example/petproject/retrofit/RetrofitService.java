package com.example.petproject.retrofit;

import com.example.petproject.bean.InstallResponse;
import com.example.petproject.bean.LoginResponse;
import com.example.petproject.bean.PetRequest;
import com.example.petproject.bean.RegisterRequest;
import com.example.petproject.bean.RemoteResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RetrofitService {


    @GET("/msm/send/{phone}")
    Observable<RemoteResult<Object>> getVerify(@Path("phone") String phone);

//    @POST("/oauth/token")
//    Observable<RemoteResult<LoginResponse>> login(@Body LoginRequest request);

    @FormUrlEncoded
    @POST("/oauth/token")
    Observable<LoginResponse> login(@Field("phone") String username,
                                    @Field("smsCode") String password,
                                    @Field("grant_type") String grantType,
                                    @Field("client_id") String client_id,
                                    @Field("scope") String scope,
                                    @Field("client_secret") String client_secret);


    @POST("/user/register")
    Observable<RemoteResult<Object>> register(@Body RegisterRequest request);

    @POST("/pet/insert")
    Observable<RemoteResult<Object>> petInsert(@Header("Authorization") String authorization, @Body PetRequest request);

    @GET("/pet/searchByUser")
    Observable<RemoteResult<Object>> petSearch(@Header("Authorization") String authorization);

    @GET("https://www.pgyer.com/apiv2/app/install")
    Observable<InstallResponse> appInstall(@Query("_api_key") String apiKey,
                                           @Query("appKey") String appKey,
                                           @Query("buildKey") String buildKey);

}
