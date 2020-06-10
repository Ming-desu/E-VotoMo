package com.qqdota.evotomo.models;

import com.google.gson.annotations.SerializedName;

public class TestResponse {
    @SerializedName("has_account")
    private boolean hasAccount;

    public boolean isHasAccount() {
        return hasAccount;
    }

    public void setHasAccount(boolean hasAccount) {
        this.hasAccount = hasAccount;
    }
}
