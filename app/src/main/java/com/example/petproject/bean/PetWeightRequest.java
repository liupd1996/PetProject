package com.example.petproject.bean;

public class PetWeightRequest {

    public String petId;
    public String weight;

    public PetWeightRequest(String petId, String weight) {
        this.petId = petId;
        this.weight = weight;
    }
}
