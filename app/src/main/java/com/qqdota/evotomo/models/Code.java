package com.qqdota.evotomo.models;

import com.google.gson.annotations.SerializedName;

public class Code {
    @SerializedName("id")
    private int id;
    @SerializedName("session_id")
    private int sessionId;
    @SerializedName("code")
    private String code;

    public int getId() {
        return id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getCode() {
        return code;
    }
}
