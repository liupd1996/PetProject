package com.example.petproject.bean;

public class PetRequest {
    String avatar;
    String birth;
    int breed;
    int gender;
    int id;
    int isSpayed;
    int isVaccinated;
    String name;
    int type;
    String weight;

    public PetRequest(String avatar, String birth, int gender, int isSpayed, int isVaccinated, String name, int type, String weight) {
        this.avatar = avatar;
        this.birth = birth;
        this.gender = gender;
        this.isSpayed = isSpayed;
        this.isVaccinated = isVaccinated;
        this.name = name;
        this.type = type;
        this.weight = weight;
    }
}
