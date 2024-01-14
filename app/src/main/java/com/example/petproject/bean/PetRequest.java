package com.example.petproject.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PetRequest implements Parcelable {
    public String avatar;
    public String birth;
    public int breed;
    public int gender;
    public String id;
    public int isSpayed;
    public int isVaccinated;
    public String name;
    public int type;
    public String weight;

    public PetRequest(String avatar, String birth, int breed, int gender, String id
            , int isSpayed, int isVaccinated, String name, int type, String weight) {
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
    }

    // Parcelable 接口实现
    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable 接口实现
    public static final Parcelable.Creator<PetRequest> CREATOR = new Parcelable.Creator<PetRequest>() {
        @Override
        public PetRequest createFromParcel(Parcel in) {
            return new PetRequest(in);
        }

        @Override
        public PetRequest[] newArray(int size) {
            return new PetRequest[size];
        }
    };

    // Parcelable 接口实现
    private PetRequest(Parcel in) {
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
    }
}
