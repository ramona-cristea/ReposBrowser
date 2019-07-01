package com.repos.browser.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.repos.browser.BuildConfig;
import com.repos.browser.model.AccessToken;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginRepositoryImpl implements LoginRepository {

    public static final String API_OAUTH_BASE_URL = "https://github.com/login/oauth/";
    public static final String AUTHORIZE_METHOD = "authorize";
    public static final String REDIRECT_URI = "https://reposbrowser.com/login/callback";
    private LoginService mLoginService;

    public LoginRepositoryImpl() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API_OAUTH_BASE_URL)
                .build();
        mLoginService = retrofit.create(LoginService.class);
    }


    @Override
    public LiveData<AccessToken> getAccessToken(@NonNull String code, @NonNull String state) {
        final MutableLiveData<AccessToken> accessTokenLiveData = new MutableLiveData<>();
        Call<AccessToken> request = mLoginService.getAccessToken(BuildConfig.GithubClientId, BuildConfig.GithubSecretId, code, REDIRECT_URI, state);
        request.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                accessTokenLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable error) {
                accessTokenLiveData.setValue(null);
                Log.d("AccessToken error %s", error.getMessage());
            }
        });

        return accessTokenLiveData;
    }
}
