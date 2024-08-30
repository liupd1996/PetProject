package com.example.petproject.retrofit;

import com.example.petproject.bean.AvatarResponse;
import com.example.petproject.bean.CountResponse;
import com.example.petproject.bean.DeleteDeviceRequest;
import com.example.petproject.bean.DevicePetRequest;
import com.example.petproject.bean.DeviceRequest;
import com.example.petproject.bean.DeviceResponse;
import com.example.petproject.bean.InstallResponse;
import com.example.petproject.bean.LoginRequest;
import com.example.petproject.bean.LoginResponse;
import com.example.petproject.bean.PetRequest;
import com.example.petproject.bean.PetResponse;
import com.example.petproject.bean.PetWeightRequest;
import com.example.petproject.bean.RefreshRequest;
import com.example.petproject.bean.RefreshResponse;
import com.example.petproject.bean.RegisterRequest;
import com.example.petproject.bean.RemoteResult;
import com.example.petproject.bean.UserEditRequest;
import com.example.petproject.bean.UserInfoResponse;
import com.example.petproject.bean.WeightResponse;
import com.example.petproject.bean.WeightSearchResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RetrofitService {


    @GET("/msm/send/{phone}")
    Observable<RemoteResult<Object>> getVerify(@Path("phone") String phone);

//    @POST("/oauth/token")
//    Observable<RemoteResult<LoginResponse>> login(@Body LoginRequest request);

//    @FormUrlEncoded
//    @POST("/oauth/token")
//    Observable<LoginResponse> login(@Field("phone") String username,
//                                    @Field("smsCode") String password,
//                                    @Field("grant_type") String grantType,
//                                    @Field("client_id") String client_id,
//                                    @Field("scope") String scope,
//                                    @Field("client_secret") String client_secret);

    @POST("/user/login")
    Observable<RemoteResult<LoginResponse>> login(@Body LoginRequest loginRequest);

    @POST("/user/register")
    Observable<RemoteResult<Object>> register(@Body RegisterRequest request);

    @POST("/user/refreshToken/{token}")
    Observable<RemoteResult<RefreshResponse>> refreshToken(@Path("token") String token);

    @PUT("/user/edit")
    Observable<RemoteResult<Object>> userEdit(@Header("Authorization") String authorization, @Body UserEditRequest request);

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

    @Multipart
    @POST("/user/upload")
    Observable<RemoteResult<AvatarResponse>> uploadImage(@Part MultipartBody.Part file);

    @GET("/info/getDetail")
    Observable<RemoteResult<UserInfoResponse>> getDetail(@Header("Authorization") String authorization);

    //查询宠物品种
    @GET("/petBreed/search")
    Observable<RemoteResult<Object>> breedSearch(@Header("Authorization") String authorization, @Query("type") int type);


    @GET("/count/breathe")//呼吸统计
    Observable<RemoteResult<Object>> breatheCount(@Header("Authorization") String authorization, @Query("type") int type, @Query("deviceId") String deviceId);

    @GET("/count/heartRate")//心跳统计
    Observable<RemoteResult<Object>> heartRateCount(@Header("Authorization") String authorization, @Query("type") int type, @Query("deviceId") String deviceId);

    @GET("/count/motion")//运动统计
    Observable<RemoteResult<Object>> motionCount(@Header("Authorization") String authorization, @Query("type") int type, @Query("deviceId") String deviceId);

    @GET("/count/sleep")//睡眠统计
    Observable<RemoteResult<Object>> sleepCount(@Header("Authorization") String authorization, @Query("type") int type, @Query("deviceId") String deviceId);

    @GET("/count/temperature")//温度统计
    Observable<RemoteResult<Object>> temperatureCount(@Header("Authorization") String authorization, @Query("type") int type, @Query("deviceId") String deviceId);

    @GET("/count/all")//温度统计
    Observable<RemoteResult<List<CountResponse>>> countAll(@Header("Authorization") String authorization, @Query("type") int type, @Query("deviceId") String deviceId);

    @GET("/pet/searchWeight")//体重搜索
    Observable<RemoteResult<WeightSearchResponse>> searchWeight(@Header("Authorization") String authorization, @Query("pageSize") int pageSize, @Query("petId") String petId );

    @POST("/pet/insertWeight")
    Observable<RemoteResult<Object>> addWeight(@Header("Authorization") String authorization, @Body PetWeightRequest request);




}
