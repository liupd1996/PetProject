package com.example.petproject.retrofit;

public class ResultException extends Exception{
    public String code;
    public String error;

    public ResultException(String code, String error) {
        this.code = code;
        this.error = error;
    }

    @Override
    public String toString() {
        return "ResultException{" +
                "code=" + code +
                ", error='" + error + '\'' +
                '}';
    }
}
