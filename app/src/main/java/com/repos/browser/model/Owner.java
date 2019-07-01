package com.repos.browser.model;

import com.google.gson.annotations.SerializedName;

public class Owner {
    @SerializedName("login")
    private String login;

    @SerializedName("id")
    private int id;

    public String getLogin() {
        return login;
    }
}
