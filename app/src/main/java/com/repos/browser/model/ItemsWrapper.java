package com.repos.browser.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemsWrapper {
    @SerializedName("total_count")
    public int totalCount;

    @SerializedName("incomplete_results")
    public boolean incompleteResults;

    @SerializedName("items")
    public List<Item> items = null;
}
