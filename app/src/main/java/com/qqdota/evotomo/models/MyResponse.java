package com.qqdota.evotomo.models;

import com.google.gson.annotations.SerializedName;

public class MyResponse {
    @SerializedName("response")
    private String response;
    @SerializedName("message")
    private String message;

    public String getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }
}
