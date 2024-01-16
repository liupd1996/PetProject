package com.example.petproject.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DeleteDeviceRequest implements Parcelable {
    public String deviceId;

    public DeleteDeviceRequest(String deviceId) {
        this.deviceId = deviceId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        // 写入数据到 Parcel
        dest.writeString(deviceId);
    }

    // Parcelable 接口实现
    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable 接口实现
    public static final Creator<DeleteDeviceRequest> CREATOR = new Creator<DeleteDeviceRequest>() {
        @Override
        public DeleteDeviceRequest createFromParcel(Parcel in) {
            return new DeleteDeviceRequest(in);
        }

        @Override
        public DeleteDeviceRequest[] newArray(int size) {
            return new DeleteDeviceRequest[size];
        }
    };

    // Parcelable 接口实现
    private DeleteDeviceRequest(Parcel in) {
        // 从 Parcel 读取数据
        deviceId = in.readString();
    }
}
