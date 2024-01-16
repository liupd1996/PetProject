package com.example.petproject.retrofit;

import com.example.petproject.bean.DeleteDeviceRequest;
import com.example.petproject.bean.DevicePetRequest;
import com.example.petproject.bean.DeviceRequest;
import com.example.petproject.bean.DeviceResponse;
import com.example.petproject.bean.InstallResponse;
import com.example.petproject.bean.LoginResponse;
import com.example.petproject.bean.PetRequest;
import com.example.petproject.bean.PetResponse;
import com.example.petproject.bean.RegisterRequest;
import com.example.petproject.bean.RemoteResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @PUT("/pet/update")
    Observable<RemoteResult<Object>> petUpdate(@Header("Authorization") String authorization, @Body PetRequest request);

    @DELETE("/pet/delete/{petId}")
    Observable<RemoteResult<Object>> petDelete(@Header("Authorization") String authorization, @Path("petId") String petId);

    @GET("/pet/searchByUser")
    Observable<RemoteResult<List<PetResponse>>> petSearch(@Header("Authorization") String authorization);





    @GET("/deviceOpr/searchDeviceDetail")
    Observable<RemoteResult<List<DeviceResponse>>> deviceSearch(@Header("Authorization") String authorization);

    @POST("/deviceOpr/bindUser")
    Observable<RemoteResult<Object>> addDevice(@Header("Authorization") String authorization, @Body DeviceRequest request);

    @POST("/deviceOpr/removeUser")
    Observable<RemoteResult<Object>> deleteDevice(@Header("Authorization") String authorization, @Body DeleteDeviceRequest request);

    @GET("https://www.pgyer.com/apiv2/app/install")
    Observable<InstallResponse> appInstall(@Query("_api_key") String apiKey,
                                           @Query("appKey") String appKey,
                                           @Query("buildKey") String buildKey);

    @POST("/deviceOpr/bind")
    Observable<RemoteResult<Object>> deviceBindPet(@Header("Authorization") String authorization, @Body DevicePetRequest request);

    @POST("/deviceOpr/remove")
    Observable<RemoteResult<Object>> deviceUnBindPet(@Header("Authorization") String authorization, @Body DevicePetRequest request);
}
