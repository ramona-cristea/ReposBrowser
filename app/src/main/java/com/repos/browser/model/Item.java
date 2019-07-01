package com.repos.browser.model;

import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("owner")
    private Owner owner;

    @SerializedName("html_url")
    private String htmlUrl;

    @SerializedName("stargazers_count")
    private int stargazersCount;

    @SerializedName("language")
    private String language;

    public String getName() {
        return name;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public Owner getOwner() {
        return owner;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public String getLanguage() {
        return language;
    }
}
