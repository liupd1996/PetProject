package com.example.petproject.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceRequest implements Parcelable {
//    public int id;
    public String deviceName;
//    public int userId;

    public DeviceRequest(String deviceName) {
        this.deviceName = deviceName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        // 写入数据到 Parcel
        dest.writeString(deviceName);
//        dest.writeInt(id);
//        dest.writeInt(userId);
    }

    // Parcelable 接口实现
    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable 接口实现
    public static final Creator<DeviceRequest> CREATOR = new Creator<DeviceRequest>() {
        @Override
        public DeviceRequest createFromParcel(Parcel in) {
            return new DeviceRequest(in);
        }

        @Override
        public DeviceRequest[] newArray(int size) {
            return new DeviceRequest[size];
        }
    };

    // Parcelable 接口实现
    private DeviceRequest(Parcel in) {
        // 从 Parcel 读取数据
        deviceName = in.readString();
//        id = in.readInt();
//        userId = in.readInt();
    }
}
