package com.repos.browser.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.repos.browser.BuildConfig;
import com.repos.browser.data.LoginRepository;
import com.repos.browser.data.LoginRepositoryImpl;

import java.util.UUID;

public class LoginViewModel extends ViewModel {

    private MediatorLiveData<AccessToken> mLoginResult = new MediatorLiveData<>();
    private LoginRepository mLoginRepository;

    public LoginViewModel() {
        this.mLoginRepository = new LoginRepositoryImpl();
    }

    public LiveData<AccessToken> getmLoginResult() {
        return mLoginResult;
    }

    public String getLoginUrl(){
       return LoginRepositoryImpl.API_OAUTH_BASE_URL +
               LoginRepositoryImpl.AUTHORIZE_METHOD +
               "?client_id=" + BuildConfig.GithubClientId +
                "&redirect_uri=" + LoginRepositoryImpl.REDIRECT_URI +
                "&scope=repo" +
                "&state=" + UUID.randomUUID().toString() +
                "&allow_signup=false";
    }

    public void getUserToken(@NonNull final String code, @NonNull final String state) {
        mLoginResult.addSource(mLoginRepository.getAccessToken(code, state),
                accessToken -> mLoginResult.setValue(accessToken));
    }
}
