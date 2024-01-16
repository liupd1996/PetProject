package com.example.petproject.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceResponse implements Parcelable {
    public String deviceId;
    public String deviceName;
    public String petId;
    public String petName;
    public String petAvatar;

    public void writeToParcel(Parcel dest, int flags) {
        // 写入数据到 Parcel
        dest.writeString(deviceId);
        dest.writeString(deviceName);
        dest.writeString(petId);
        dest.writeString(petName);
        dest.writeString(petAvatar);
    }

    // Parcelable 接口实现
    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable 接口实现
    public static final Creator<DeviceResponse> CREATOR = new Creator<DeviceResponse>() {
        @Override
        public DeviceResponse createFromParcel(Parcel in) {
            return new DeviceResponse(in);
        }

        @Override
        public DeviceResponse[] newArray(int size) {
            return new DeviceResponse[size];
        }
    };

    // Parcelable 接口实现
    private DeviceResponse(Parcel in) {
        // 从 Parcel 读取数据
        deviceId = in.readString();
        deviceName = in.readString();
        petId = in.readString();
        petName = in.readString();
        petAvatar = in.readString();

    }

    @Override
    public String toString() {
        return "DeviceResponse{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", petId='" + petId + '\'' +
                ", petName='" + petName + '\'' +
                ", petAvatar='" + petAvatar + '\'' +
                '}';
    }
}
