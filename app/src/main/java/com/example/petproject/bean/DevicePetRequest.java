package com.example.petproject.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DevicePetRequest implements Parcelable {

    public String deviceId;
    public String petId;

    public DevicePetRequest(String deviceId, String petId) {
        this.deviceId = deviceId;
        this.petId = petId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        // 写入数据到 Parcel
        dest.writeString(deviceId);
        dest.writeString(petId);
    }

    // Parcelable 接口实现
    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable 接口实现
    public static final Creator<DevicePetRequest> CREATOR = new Creator<DevicePetRequest>() {
        @Override
        public DevicePetRequest createFromParcel(Parcel in) {
            return new DevicePetRequest(in);
        }

        @Override
        public DevicePetRequest[] newArray(int size) {
            return new DevicePetRequest[size];
        }
    };

    // Parcelable 接口实现
    private DevicePetRequest(Parcel in) {
        // 从 Parcel 读取数据
        deviceId = in.readString();
        petId = in.readString();

    }
}
