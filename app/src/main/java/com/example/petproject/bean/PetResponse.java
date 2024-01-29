package com.example.petproject.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PetResponse implements Parcelable {
    public String avatar;
    public String birth;
    public int breed;
    public int gender;
    public String id;//todo check string.?
    public int isSpayed;
    public int isVaccinated;
    public String name;
    public int type;
    public String weight;
    public String deviceId;
    public String deviceName;

    public PetResponse(String avatar, String birth, int breed, int gender, String id, int isSpayed,
                       int isVaccinated, String name, int type, String weight, String deviceId, String deviceName) {
        this.avatar = avatar;
        this.birth = birth;
        this.breed = breed;
        this.gender = gender;
        this.id = id;
        this.isSpayed = isSpayed;
        this.isVaccinated = isVaccinated;
        this.name = name;
        this.type = type;
        this.weight = weight;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        // 写入数据到 Parcel
        dest.writeString(avatar);
        dest.writeString(birth);
        dest.writeInt(breed);
        dest.writeInt(gender);
        dest.writeString(id);
        dest.writeInt(isSpayed);
        dest.writeInt(isVaccinated);
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeString(weight);
        dest.writeString(deviceId);
        dest.writeString(deviceName);
    }

    // Parcelable 接口实现
    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable 接口实现
    public static final Creator<PetResponse> CREATOR = new Creator<PetResponse>() {
        @Override
        public PetResponse createFromParcel(Parcel in) {
            return new PetResponse(in);
        }

        @Override
        public PetResponse[] newArray(int size) {
            return new PetResponse[size];
        }
    };

    // Parcelable 接口实现
    private PetResponse(Parcel in) {
        // 从 Parcel 读取数据
        avatar = in.readString();
        birth = in.readString();
        breed = in.readInt();
        gender = in.readInt();
        id = in.readString();
        isSpayed = in.readInt();
        isVaccinated = in.readInt();
        name = in.readString();
        type = in.readInt();
        weight = in.readString();
        deviceId = in.readString();
        deviceName = in.readString();
    }
}
