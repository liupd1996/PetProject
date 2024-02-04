package com.example.petproject.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfoResponse implements Parcelable {
    public String id;
    public String username;
    public String avatar;
    public int gender;

    public void writeToParcel(Parcel dest, int flags) {
        // 写入数据到 Parcel
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(avatar);
        dest.writeInt(gender);
    }

    // Parcelable 接口实现
    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable 接口实现
    public static final Creator<UserInfoResponse> CREATOR = new Creator<UserInfoResponse>() {
        @Override
        public UserInfoResponse createFromParcel(Parcel in) {
            return new UserInfoResponse(in);
        }

        @Override
        public UserInfoResponse[] newArray(int size) {
            return new UserInfoResponse[size];
        }
    };

    // Parcelable 接口实现
    private UserInfoResponse(Parcel in) {
        // 从 Parcel 读取数据
        id = in.readString();
        username = in.readString();
        avatar = in.readString();
        gender = in.readInt();

    }

    @Override
    public String toString() {
        return "UserInfoResponse{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
