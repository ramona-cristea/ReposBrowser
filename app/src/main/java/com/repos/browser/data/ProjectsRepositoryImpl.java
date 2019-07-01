package com.repos.browser.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.repos.browser.model.AuthenticationInterceptor;
import com.repos.browser.model.Item;
import com.repos.browser.model.ItemsWrapper;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProjectsRepositoryImpl implements ProjectsRepository {

    private static final String BASE_URL = "https://api.github.com/";
    private static final String SORT_BY_STARS = "stars";
    private static final String ORDER_DESC = "desc";
    private GithubService mGithubService;

    public ProjectsRepositoryImpl(@NonNull final String token) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(token))
                .addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        mGithubService = retrofit.create(GithubService.class);
    }


    @Override
    public LiveData<List<Item>> searchForProjects(@NonNull String query) {
        final MutableLiveData<List<Item>> projectsLiveData = new MutableLiveData<>();
        Call<ItemsWrapper> request = mGithubService.getRepositories(query, SORT_BY_STARS, ORDER_DESC);
        request.enqueue(new Callback<ItemsWrapper>() {
            @Override
            public void onResponse(@NonNull Call<ItemsWrapper> call, @NonNull Response<ItemsWrapper> response) {
                if(response.body() != null) {
                    projectsLiveData.setValue(response.body().items);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ItemsWrapper> call, @NonNull Throwable error) {
                projectsLiveData.setValue(null);
            }
        });

        return projectsLiveData;
    }
}
