package com.repos.browser.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.repos.browser.model.AccessToken;

public interface LoginRepository {
    LiveData<AccessToken> getAccessToken(@NonNull final String code, @NonNull final String state);
}

