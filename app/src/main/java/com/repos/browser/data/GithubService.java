package com.repos.browser.data;

import com.repos.browser.model.ItemsWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubService {
    @GET("/search/repositories")
    Call<ItemsWrapper> getRepositories(
            @Query("q") String searchParameter,
            @Query("sort") String sortBy,
            @Query("order") String order);
}
