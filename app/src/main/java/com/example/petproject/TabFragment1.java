package com.example.petproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.petproject.bean.JsonRequest;
import com.example.petproject.bean.JsonRequest.Data;
import com.example.petproject.customview.BatteryView;
import com.example.petproject.utils.DataUtils;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab1, container, false);
        view.findViewById(R.id.cl_news).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),DataActivity.class);
            startActivity(intent);
        });
        view.findViewById(R.id.cl_breanth).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),DataActivity.class);
            startActivity(intent);
        });
        view.findViewById(R.id.cl_sleep).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),DataActivity.class);
            startActivity(intent);
        });
        view.findViewById(R.id.cl_heart).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),DataActivity.class);
            startActivity(intent);
        });
        view.findViewById(R.id.cl_temperature).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),DataActivity.class);
            startActivity(intent);
        });
        view.findViewById(R.id.tv_exercise).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),DataActivity.class);
            startActivity(intent);
        });
        view.findViewById(R.id.btn_weight).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),DataActivity.class);
            startActivity(intent);
        });
        view.findViewById(R.id.btn_diary).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),DataActivity.class);
            startActivity(intent);
        });
        client = new OkHttpClient();
        startWebSocket();
        return view;
    }


    public void startWebSocket() {
        mWebSocketClient = new WebSocketClient(URI.create("ws://139.186.13.82:3003/terminal/realtime")) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                // 连接成功时的处理
                if (mWebSocketClient != null) {
                    JsonRequest request = new JsonRequest();
                    request.setRequestType(1);

                    Data data = new Data();
                    data.setTerminalID("10069096400");
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
                // 接收到消息时的处理
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                // 连接关闭时的处理
            }

            @Override
            public void onError(Exception e) {
                // 连接失败时的处理
            }
        };

        mWebSocketClient.connect();
    }

    private void updateView(String mExtendParamDesc) {
        double voltage = DataUtils.getBatteryPercentage(mExtendParamDesc);
        //String formattedValue = String.format("%.2f", voltage);
        TextView tv_battery = view.findViewById(R.id.tv_battery);
        tv_battery.setText("电池电量" + (int)voltage + "%");
        BatteryView batteryView = view.findViewById(R.id.battery);
        batteryView.setBatteryLevel(voltage);

        //当日里程
        String todayKm = DataUtils.extractVTodayKm(mExtendParamDesc);
        TextView tv_exercise = view.findViewById(R.id.tv_exercise);
        tv_exercise.setText(todayKm + "km");
        //卡路里

        double todayKm2 = Double.parseDouble(todayKm);
        int todayCalorieConsumption = (int)(2.5* todayKm2*1.063);
        TextView tv_cal = view.findViewById(R.id.tv_cal);
        tv_cal.setText(todayCalorieConsumption + "kcal");
        //体温
        double tempature = DataUtils.extractTemperatures(mExtendParamDesc);
        TextView tv_temperature = view.findViewById(R.id.tv_temperature);
        tv_temperature.setText(tempature + "°C");
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
        if (maxXValue< 1000) {
            petPosture = "休息中";
            respiratorRate = "10-20次/min";
            heartRate = "60-100次/秒";
            iv_sleep.setBackgroundResource(R.drawable.cat_sleep);
        } else if (maxXValue <2000 || maxXValue >=1000) {
            petPosture = "行走中";
            String respiratorRate1 = "20-30次/min";
            String respiratorRate2 = "30-40次/min";
            String heartRate1 = "100-140次/秒";
            String heartRate2 = "140-160次/秒";
            Random random = new Random();
            int index = random.nextInt(2);
            if (index == 0) {
                respiratorRate = respiratorRate1;
                heartRate = heartRate1;
            } else {
                respiratorRate = respiratorRate2;
                heartRate = heartRate2;
            }
            iv_sleep.setBackgroundResource(R.drawable.cat_walk);
        } else {
            petPosture = "运动中";
            respiratorRate = ">40次/min";
            heartRate = ">160次/秒";
            iv_sleep.setBackgroundResource(R.drawable.cat_exercise);
        }
        tv_act.setText(petPosture);
        tv_breath.setText(respiratorRate);
        tv_heart.setText(heartRate);
        Log.d(TAG, "updateView: " + voltage + "--:" +tempature);
    }

}