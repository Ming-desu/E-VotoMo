package com.qqdota.evotomo.models;

import com.google.gson.annotations.SerializedName;

public class SessionForm {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("vote_start")
    private String voteStart;
    @SerializedName("vote_end")
    private String voteEnd;
    @SerializedName("added_by")
    private int addedBy;

    public SessionForm(String name, String voteStart, String voteEnd, int addedBy) {
        this.name = name;
        this.voteStart = voteStart;
        this.voteEnd = voteEnd;
        this.addedBy = addedBy;
    }

    public void setId(int id) {
        this.id = id;
    }
}
