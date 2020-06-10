package com.qqdota.evotomo.models;

import com.google.gson.annotations.SerializedName;

public class SessionResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("vote_start")
    private String voteStart;
    @SerializedName("vote_end")
    private String voteEnd;
    @SerializedName("user")
    private Account user;
    @SerializedName("code")
    private Code code;

    public SessionResponse(String name, String voteStart, String voteEnd, Account user, Code code) {
        this.name = name;
        this.voteStart = voteStart;
        this.voteEnd = voteEnd;
        this.user = user;
        this.code = code;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVoteStart() {
        return voteStart;
    }

    public String getVoteEnd() {
        return voteEnd;
    }

    public Account getUser() {
        return user;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public SessionForm getSessionForm() {
        return new SessionForm(
                name,
                voteStart,
                voteEnd,
                user.getId()
        );
    }
}
