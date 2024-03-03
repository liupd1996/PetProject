package com.example.petproject.bean;

public class AvatarResponse {
    public String fileName;
    public String fileDownloadUri;

    @Override
    public String toString() {
        return "AvatarResponse{" +
                "fileName='" + fileName + '\'' +
                ", fileDownloadUri='" + fileDownloadUri + '\'' +
                '}';
    }
}
