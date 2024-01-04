package com.example.petproject.bean;

import com.google.gson.annotations.SerializedName;

public class JsonRequest {
    private int RequestType;
    private Data Data;

    public int getRequestType() {
        return RequestType;
    }

    public void setRequestType(int requestType) {
        RequestType = requestType;
    }

    public Data getData() {
        return Data;
    }

    public void setData(Data data) {
        Data = data;
    }

    public static class Data {
        @SerializedName("TerminalID")
        private String TerminalID;
        private boolean Subscribe;

        public String getTerminalID() {
            return TerminalID;
        }

        public void setTerminalID(String terminalID) {
            TerminalID = terminalID;
        }

        public boolean isSubscribe() {
            return Subscribe;
        }

        public void setSubscribe(boolean subscribe) {
            Subscribe = subscribe;
        }
    }
}



