package com.example.petproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.petproject.bean.JsonRequest;
import com.example.petproject.bean.JsonRequest.Data;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;

public class TabFragment1 extends Fragment {
    private static final String TAG = "TabFragment1";
    private View view;
    private TextView textView;
    private OkHttpClient client;
    private WebSocket webSocket;
    WebSocketClient mWebSocketClient;


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
        view.findViewById(R.id.cl_weight).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),DataActivity.class);
            startActivity(intent);
        });
        view.findViewById(R.id.cl_pet_diary).setOnClickListener(view -> {
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

}