package com.example.petproject.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MyWebSocketClient {

    private OkHttpClient client;
    private WebSocket webSocket;

    public MyWebSocketClient() {
        client = new OkHttpClient();
    }

    public void startWebSocket() {
        Request request = new Request.Builder()
                .url("ws://139.186.13.82:3003/terminal/realtime")
                .build();

        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                // WebSocket 连接已打开
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // 接收到文本消息
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                // 接收到二进制消息
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                // WebSocket 连接正在关闭
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                // WebSocket 连接已关闭
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                // 连接失败
            }
        };

        webSocket = client.newWebSocket(request, listener);
    }

    public void sendMessage(String message) {
        webSocket.send(message);
    }

    public void closeWebSocket() {
        webSocket.close(1000, "Goodbye!");
    }
}
