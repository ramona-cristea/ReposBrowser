package com.repos.browser.views;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.repos.browser.R;
import com.repos.browser.data.LoginRepositoryImpl;
import com.repos.browser.model.AccessToken;
import com.repos.browser.model.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    public static final String TOKEN = "oauth2_token";
    private LoginViewModel mLoginViewModel;
    private ProgressBar mLoadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        final Button loginButton = findViewById(R.id.login);
        mLoadingProgressBar = findViewById(R.id.loading);

        mLoginViewModel.getmLoginResult().observe(this, accessToken -> {
            if (accessToken == null) {
                return;
            }
            mLoadingProgressBar.setVisibility(View.GONE);
            storeUserToken(accessToken);
            navigateToBrowser(accessToken);
        });

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(mLoginViewModel.getLoginUrl()));
            startActivity(intent);
        });

        SharedPreferences prefs = this.getSharedPreferences(
                getPackageName(), Context.MODE_PRIVATE);
        String tokenAsJson = prefs.getString(TOKEN, "");
        if(!TextUtils.isEmpty(tokenAsJson)) {
            AccessToken accessToken = new Gson().fromJson(tokenAsJson, AccessToken.class);
            navigateToBrowser(accessToken);
        }
    }

    private void navigateToBrowser(AccessToken token){
        Intent browser = new Intent(this, BrowseReposActivity.class);
        browser.putExtra(TOKEN, token.getToken());
        startActivity(browser);
        finish();
    }

    private void storeUserToken(AccessToken token){
        SharedPreferences prefs = this.getSharedPreferences(
                getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putString(TOKEN, new Gson().toJson(token)).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(LoginRepositoryImpl.REDIRECT_URI)) {
            String code = uri.getQueryParameter("code");
            String state = uri.getQueryParameter("state");
            if (code != null && state != null) {
                mLoadingProgressBar.setVisibility(View.VISIBLE);
                mLoginViewModel.getUserToken(code, state);
            } else if (uri.getQueryParameter("error") != null) {
                Toast.makeText(this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
