package com.example.petproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.petproject.bean.JsonRequest;
import com.example.petproject.bean.JsonRequest.Data;
import com.example.petproject.bean.PetResponse;
import com.example.petproject.bean.RemoteResult;
import com.example.petproject.bean.WeightSearchResponse;
import com.example.petproject.customview.BatteryView;
import com.example.petproject.dialog.SureCancelDialog;
import com.example.petproject.retrofit.ResultFunction;
import com.example.petproject.retrofit.RetrofitUtils;
import com.example.petproject.utils.ConfigPreferences;
import com.example.petproject.utils.DataUtils;
import com.example.petproject.utils.ExceptionHandle;
import com.example.petproject.utils.ToastUtils;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.WebSocket;

public class TabFragment1 extends Fragment {
    private static final String TAG = "TabFragment1";
    private View view;
    private TextView textView;
    private OkHttpClient client;
    private WebSocket webSocket;
    WebSocketClient mWebSocketClient;
    private String mExtendParamDesc;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private TextView tv_name;
    private TextView tv_birth;
    private ImageView avatar;
    private TextView tv_weight;
    private ImageView iv_gender;
    private SureCancelDialog mPetDialog;
    private SureCancelDialog mDeviceDialog;
    private String deviceId = "";
    private String _Id = "";
    private String pet_Id = "";
    private String weight;
    private String weight_num;


    public TabFragment1() {
        // Required empty public constructor
    }

    public static TabFragment1 newInstance(int id) {
        TabFragment1 fragment = new TabFragment1();
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
        Log.d(TAG, "onCreateView10086: ");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab1, container, false);
        view.findViewById(R.id.cl_news).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), DataActivity.class);
            startActivity(intent);
        });
        view.findViewById(R.id.cl_breanth).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("type", 1);
            intent.putExtra("deviceId", _Id);
            intent.putExtra("message", mExtendParamDesc);
            startActivity(intent);
        });
        view.findViewById(R.id.cl_sleep).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("type", 2);
            intent.putExtra("deviceId", _Id);
            intent.putExtra("message", mExtendParamDesc);
            startActivity(intent);
        });
        view.findViewById(R.id.cl_heart).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("type", 3);
            intent.putExtra("deviceId", _Id);
            intent.putExtra("message", mExtendParamDesc);
            startActivity(intent);
        });
        view.findViewById(R.id.cl_temperature).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("type", 4);
            intent.putExtra("deviceId", _Id);
            intent.putExtra("message", mExtendParamDesc);
            startActivity(intent);
        });
        view.findViewById(R.id.cl_exercise).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("type", 5);
            intent.putExtra("deviceId", _Id);
            intent.putExtra("message", mExtendParamDesc);
            startActivity(intent);
        });
        view.findViewById(R.id.btn_weight).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("type", 7);
            intent.putExtra("deviceId", _Id);
            intent.putExtra("message", mExtendParamDesc);
            intent.putExtra("weight", weight_num);
            intent.putExtra("pet_Id", pet_Id);
            startActivity(intent);
        });
        view.findViewById(R.id.btn_diary).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), DataActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.cl_title).setOnClickListener(view -> {
            startWebSocket(1);
        });
        tv_name = view.findViewById(R.id.tv_name);
        tv_birth = view.findViewById(R.id.tv_birth);
        avatar = view.findViewById(R.id.avatar);
        tv_weight = view.findViewById(R.id.tv_weight);
        iv_gender = view.findViewById(R.id.iv_gender);
        client = new OkHttpClient();
        String token = "Bearer " + ConfigPreferences.login_token(getContext());
        search(token);

        mPetDialog = SureCancelDialog.newInstance("请添加宠物", "取消", "确定");
        mPetDialog.setOnCancelListener(new SureCancelDialog.OnSureCancelListener() {
            @Override
            public void onCancel() {
                mPetDialog.dismiss();
            }

            @Override
            public void onSureListener(String text) {
                Intent intent = new Intent(getActivity(), PetActivity.class);
                startActivity(intent);
            }
        });

        mDeviceDialog = SureCancelDialog.newInstance("请将设备与宠物绑定", "取消", "确定");
        mDeviceDialog.setOnCancelListener(new SureCancelDialog.OnSureCancelListener() {
            @Override
            public void onCancel() {
                mDeviceDialog.dismiss();
            }

            @Override
            public void onSureListener(String text) {
                Intent intent = new Intent(getActivity(), DeviceActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String token = "Bearer " + ConfigPreferences.login_token(getContext());
        searchWeight(token, pet_Id);
    }

    private void searchWeight(String token, String pet_Id) {//todo pet update
        if (TextUtils.isEmpty(pet_Id)) {
            return;
        }
        Log.d("1111", "searchWeight pet_Id----" + pet_Id);
        RetrofitUtils.getRetrofitService().searchWeight(token, 7, pet_Id)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())//todo add edit
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<WeightSearchResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<WeightSearchResponse> result) {
                        Log.d("7777", "onNext WeightResponse: " + result.data.records.size());
                        if (result.data.records.size() > 0) {
                            weight_num = result.data.records.get(0).weight;
                            weight = weight_num + " 千克";
                            tv_weight.setText(getTextSizeSpan(weight, 0, weight_num.length() + 1));
                        }


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ExceptionHandle.ResponeThrowable responeThrowable = ExceptionHandle.handleException(e);
                        if (responeThrowable.code.equals("020000")) {
                            ConfigPreferences.setLoginName(getContext(), "");
                            ConfigPreferences.setLoginToken(getContext(), "");
                            startActivity(new Intent(getContext(), LoginActivity.class));
                            getActivity().finish();
                        } else {
                            ToastUtils.customToast(getContext(), responeThrowable.message);
                        }

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public void startWebSocket(int type) {
        Log.d(TAG, "startWebSocket: " + deviceId);
        if (TextUtils.isEmpty(deviceId)) {
            return;
        }
        mWebSocketClient = new WebSocketClient(URI.create("ws://139.186.13.82:3003/terminal/realtime")) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                // 连接成功时的处理
                if (mWebSocketClient != null) {
                    JsonRequest request = new JsonRequest();
                    request.setRequestType(1);

                    Data data = new Data();
                    data.setTerminalID(deviceId);
                    data.setSubscribe(true);

                    request.setData(data);
                    List<JsonRequest> jsonRequests = new ArrayList<>();
                    jsonRequests.add(request);
                    // 初始化 Gson 对象
                    Gson gson = new Gson();

                    // 将对象转换为 JSON 字符串
                    String jsonString = gson.toJson(jsonRequests);
                    mWebSocketClient.send(jsonString);
                }
            }

            @Override
            public void onMessage(String s) {
                Log.d(TAG, "onMessage: " + s);
                try {
                    // 解析 JSON 数组
                    JSONArray jsonArray = new JSONArray(s);
                    Log.d(TAG, "1111: " + jsonArray.length());
                    // 遍历 JSON 数组
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        // 获取 JSON 对象中的字段
                        JSONObject dataObject = jsonObject.getJSONObject("Data");
                        mExtendParamDesc = dataObject.getString("ExtendParamDesc");
                        mHandler.post(() -> {
                            updateView(mExtendParamDesc);
                        });

                        // 输出字段值
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (type == 1) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.customToast(getContext(), "获取宠物最新数据成功");
                        }
                    });
                }
                // 接收到消息时的处理
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                // 连接关闭时的处理
            }

            @Override
            public void onError(Exception e) {
                // 连接失败时的处理
                if (type == 1) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.customToast(getContext(), "获取宠物最新数据失败");
                        }
                    });
                }
            }
        };

        mWebSocketClient.connect();
    }

    private void updateView(String mExtendParamDesc) {
        //double voltage = DataUtils.getBatteryPercentage(mExtendParamDesc);
        double voltage = DataUtils.extractBattery(mExtendParamDesc);
        //String formattedValue = String.format("%.2f", voltage);
        TextView tv_battery = view.findViewById(R.id.tv_battery);
        tv_battery.setText("电池电量:" + (int) voltage + "%");
        BatteryView batteryView = view.findViewById(R.id.battery);
        batteryView.setVisibility(View.VISIBLE);
        batteryView.setBatteryLevel(voltage);

        //当日里程
        String todayKm = DataUtils.extractVTodayKm(mExtendParamDesc);
        TextView tv_exercise = view.findViewById(R.id.tv_exercise);
        String fullTodayKm = todayKm + " 千米";
        tv_exercise.setText(getTextSizeSpan(fullTodayKm, 0, fullTodayKm.length() - 2));
        //卡路里

        double todayKm2 = Double.parseDouble(todayKm);
        int todayCalorieConsumption = (int) (2.5 * todayKm2 * 1.063);
        TextView tv_cal = view.findViewById(R.id.tv_cal);
        String fullTodayCalorieConsumption = todayCalorieConsumption + " 卡路里";
        tv_cal.setText(getTextSizeSpan(fullTodayCalorieConsumption, 0, fullTodayCalorieConsumption.length() - 3));
        //体温
        double tempature = DataUtils.extractTemperatures(mExtendParamDesc);
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String formattedTemperature = decimalFormat.format(tempature);
        TextView tv_temperature = view.findViewById(R.id.tv_temperature);
        String temperature = formattedTemperature + " 摄氏度";
        tv_temperature.setText(getTextSizeSpan(temperature, 0, temperature.length() - 3));
        DataUtils.testRegexMatch(mExtendParamDesc);

        //行为检测
        TextView tv_act = view.findViewById(R.id.tv_act);
        TextView tv_breath = view.findViewById(R.id.tv_breath);
        TextView tv_heart = view.findViewById(R.id.tv_heart);
        ImageView iv_sleep = view.findViewById(R.id.iv_act);

        double maxXValue = DataUtils.extractXYZ(mExtendParamDesc);
        String petPosture = "未知";
        String respiratorRate = "未知";
        String heartRate = "未知";
        if (maxXValue < 1000) {
            petPosture = "休息中";
            respiratorRate = "10-20 次/分";
            heartRate = "60-100 次/秒";
            iv_sleep.setBackgroundResource(R.drawable.relax);
        } else if (maxXValue < 2000 || maxXValue >= 1000) {
            petPosture = "行走中";
            String respiratorRate1 = "20-30 次/分";
            String respiratorRate2 = "30-40 次/分";
            String heartRate1 = "100-140 次/秒";
            String heartRate2 = "140-160 次/秒";
            Random random = new Random();
            int index = random.nextInt(2);
            if (index == 0) {
                respiratorRate = respiratorRate1;
                heartRate = heartRate1;
            } else {
                respiratorRate = respiratorRate2;
                heartRate = heartRate2;
            }
            iv_sleep.setBackgroundResource(R.drawable.walk);
        } else {
            petPosture = "运动中";
            respiratorRate = ">40 次/分";
            heartRate = ">160 次/秒";
            iv_sleep.setBackgroundResource(R.drawable.exercise_icon);
        }

        tv_act.setText(petPosture);
        tv_breath.setText(getTextSizeSpan(respiratorRate, 0, respiratorRate.length() - 3));
        tv_heart.setText(getTextSizeSpan(heartRate, 0, heartRate.length() - 3));
        Log.d(TAG, "updateView: " + voltage + "--:" + tempature);
    }

    private void search(String token) {
        RetrofitUtils.getRetrofitService().petSearch(token)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())//todo filter
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<List<PetResponse>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<List<PetResponse>> result) {
                        updateView(result.data);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ExceptionHandle.ResponeThrowable responeThrowable = ExceptionHandle.handleException(e);
                        if (responeThrowable.code.equals("020000")) {
                            ToastUtils.customToast(getContext(), "登录过期");
                            ConfigPreferences.setLoginName(getContext(), "");
                            ConfigPreferences.setLoginToken(getContext(), "");
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            if (getActivity() != null) {
                                getActivity().finish();
                            }
                        } else {
                            ToastUtils.customToast(getContext(), responeThrowable.message);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (mPetDialog.getDialog() != null && mPetDialog.getDialog().isShowing()) {
                mPetDialog.dismiss();
            }
            if (mDeviceDialog.getDialog() != null && mDeviceDialog.getDialog().isShowing()) {
                mDeviceDialog.dismiss();
            }
        }
    }

    private void updateView(List<PetResponse> list) {
        if (list.size() == 0) {
            mPetDialog.show(getActivity().getSupportFragmentManager(), "addPet");
            return;
        }
        List<PetResponse> filterList = filterList(list);
        if (filterList.size() == 0) {
            mDeviceDialog.show(getActivity().getSupportFragmentManager(), "addDevice");
            return;
        }
        PetResponse petResponse;
        Log.d(TAG, "updateView: " + filterList.size());
        if (filterList.size() != 0) {
            petResponse = filterList.get(0);
            deviceId = petResponse.deviceName;
            _Id = petResponse.deviceId;
            pet_Id = petResponse.id;
            Log.d(TAG, "updateView9999:" + pet_Id);
            startWebSocket(0);
        } else {
            petResponse = list.get(0);
        }
        tv_name.setText(petResponse.name);

        weight = petResponse.weight + " 千克";
        weight_num = petResponse.weight;
        tv_weight.setText(getTextSizeSpan(weight, 0, petResponse.weight.length() + 1));

        if (!TextUtils.isEmpty(petResponse.avatar)) {
            Glide.with(getContext()).load("http://47.94.99.63:8088/pet/download/" + petResponse.avatar).into(avatar);
        } else {
            if (petResponse.type == 0) {
                avatar.setImageResource(R.drawable.dog_default);

            } else {
                avatar.setImageResource(R.drawable.cat_default);
            }
        }
        tv_birth.setText(petResponse.breed);

//        if (petResponse.type == 0) {
//            tv_birth.setText("狗狗");
//            avatar.setImageResource(R.drawable.dog_default);
//        } else {
//            tv_birth.setText("猫猫");
//            avatar.setImageResource(R.drawable.cat_default);
//        }
        if (petResponse.gender == 0) {
            iv_gender.setBackgroundResource(R.drawable.man);
        } else {
            iv_gender.setBackgroundResource(R.drawable.woman);
        }
    }

    private List<PetResponse> filterList(List<PetResponse> data) {
        List<PetResponse> filterList = new ArrayList<>();
        for (PetResponse response : data) {
            if (!TextUtils.isEmpty(response.deviceId) && !response.deviceId.equals("0")) {
                filterList.add(response);
            }
        }
        return filterList;
    }

    private SpannableStringBuilder getTextSizeSpan(String fullText, int start, int end) {

        // Create a SpannableString
        SpannableStringBuilder builder = new SpannableStringBuilder(fullText);

        builder.setSpan(new AbsoluteSizeSpan(26, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the size of the "km" part
        builder.setSpan(new AbsoluteSizeSpan(12, true), end, fullText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }
}