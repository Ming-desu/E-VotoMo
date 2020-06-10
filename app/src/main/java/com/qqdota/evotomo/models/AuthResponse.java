package com.qqdota.evotomo.models;

import com.google.gson.annotations.SerializedName;

public class AuthResponse extends MyResponse {
    @SerializedName("type")
    private int type;

    public int getType() {
        return type;
    }
}
