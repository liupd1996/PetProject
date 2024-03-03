package com.example.petproject.bean;

public class WeightResponse {
    public String weight;
    public String recordDate;

    @Override
    public String toString() {
        return "WeightResponse{" +
                "weight='" + weight + '\'' +
                ", recordDate='" + recordDate + '\'' +
                '}';
    }
}
