package com.example.lol_history.model;

import com.google.gson.annotations.SerializedName;

public class SummonerIDinfo {
    @SerializedName("id")
    private String id;

    @SerializedName("accountId")
    private String accountId;

    @SerializedName("name")
    private String name;

    @SerializedName("summonerLevel")
    private Integer summonerLevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(Integer summonerLevel) {
        this.summonerLevel = summonerLevel;
    }
}
