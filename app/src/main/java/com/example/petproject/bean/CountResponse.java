package com.example.petproject.bean;

public class CountResponse  {
    public String extendParamDesc;
    public String createdTime;

    @Override
    public String toString() {
        return "CountResponse{" +
                "extendParamDesc='" + extendParamDesc + '\'' +
                ", createdTime='" + createdTime + '\'' +
                '}';
    }
}
