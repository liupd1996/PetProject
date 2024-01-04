package com.example.petproject.retrofit;

import com.example.petproject.bean.LoginResponse;
import com.example.petproject.bean.RegisterRequest;
import com.example.petproject.bean.RemoteResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


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
                                                  @Field("scope")  String scope,
                                                  @Field("client_secret") String client_secret);



    @POST("/user/register")
    Observable<RemoteResult<Object>> register(@Body RegisterRequest request);

//    @POST("/api/home/application")
//    Observable<RemoteResult<AntiResponse>> getAntiResult(@Header("token") String token, @Body AntiRequest request);
//
//    @GET("/api/apply/applicationList")
//    Observable<RemoteResult<List<CodeListBean>>> getCodeList(@Header("Authorization") String author);
//
//    @GET("/api/apply/applicationDetail")
//    Observable<RemoteResult<AppDetailResponse>> getApplicationDetail(@Header("Authorization") String author, @Query("id") int id);
//
//    @GET("/api/apply/applicationColumnInfo")
//    Observable<RemoteResult< List<ColumnListBean> >> getColumnInfo(@Header("Authorization") String author, @Query("id") int id);
//
//    @POST("/api/auth/login")
//    Observable<RemoteResult<LoginResponse>> login(@Header("token") String token, @Body LoginRequest request);
//
//    @GET("/api/user/userInfo")
//    Observable<RemoteResult<UserInfoResponse>> getUserInfo(@Header("Authorization") String author);
//
//    @POST("/api/apply/contentAdd")
//    Observable<RemoteResult<Object>> addHistory(@Header("Authorization") String author, @Body HistoryRequest request);//记录回形码解码时间
//
//    @GET("/api/apply/versionNumber")
//    Observable<RemoteResult<VersionResult>> getVersion(@Header("token") String author);
//
//    //java 服务器   --接口来自zhangmengda，返回结果为链接做过的所有的码的url链接.
//    @POST("http://newp.vrcode.com:8011/OpenSkill/searchVrcmRecord")
//    Observable<VRRemoteResult<List<String>>> checkVRCode(@Body VRCheckRequest request);
//
//    @POST("http://192.168.1.212:8013/hxcode/encode")
//    Observable<VRRemoteResult<String>> encode(@Body HXRequest request);
//
//
//    //健康状态查询
//    @POST("https://fjjkm1.nebulabd.cn/ebus/jkm/healthcode-scanqr/healthcodescanqr/openapi/gatecheck/healthinfo")
//    @Headers({"Content-Type:application/json; charset=UTF-8", //固定headers.
//            "Cache-Control:no-cache"})
//    Observable<TestResult> test(@HeaderMap Map<String, String> headers, @Body TestRequest testRequest);
//
//    //健康状态查询,内部测试服务器
//    @POST("http://192.168.1.34:8013/health/qr")
//    Observable<TestResult> test2(@Body TestRequest2 testRequest);

}
