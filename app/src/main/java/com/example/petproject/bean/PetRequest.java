package com.example.petproject.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PetRequest implements Parcelable {
    public String avatar;
    public String birth;
    public String breed;
    public String breedId;
    public int gender;
    public String id;//todo check string.?
    public int isSpayed;
    public int isVaccinated;
    public String name;
    public int type;
    public String typeId;
    public String weight;

    public PetRequest(String avatar, String birth, String breed, String breedId, int gender, String id
            , int isSpayed, int isVaccinated, String name, int type, String typeId, String weight) {
        this.avatar = avatar;
        this.birth = birth;
        this.breed = breed;
        this.breedId = breedId;
        this.gender = gender;
        this.id = id;
        this.isSpayed = isSpayed;
        this.isVaccinated = isVaccinated;
        this.name = name;
        this.type = type;
        this.typeId = typeId;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "PetRequest{" +
                "avatar='" + avatar + '\'' +
                ", birth='" + birth + '\'' +
                ", breed='" + breed + '\'' +
                ", gender=" + gender +
                ", id='" + id + '\'' +
                ", isSpayed=" + isSpayed +
                ", isVaccinated=" + isVaccinated +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", weight='" + weight + '\'' +
                '}';
    }

    public void writeToParcel(Parcel dest, int flags) {
        // 写入数据到 Parcel
        dest.writeString(avatar);
        dest.writeString(birth);
        dest.writeString(breed);
        dest.writeString(breedId);
        dest.writeInt(gender);
        dest.writeString(id);
        dest.writeInt(isSpayed);
        dest.writeInt(isVaccinated);
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeString(typeId);
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
        breed = in.readString();
        breedId = in.readString();
        gender = in.readInt();
        id = in.readString();
        isSpayed = in.readInt();
        isVaccinated = in.readInt();
        name = in.readString();
        type = in.readInt();
        typeId = in.readString();
        weight = in.readString();
    }
}
