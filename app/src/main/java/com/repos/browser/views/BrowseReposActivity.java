package com.repos.browser.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.repos.browser.R;
import com.repos.browser.model.Item;
import com.repos.browser.model.ProjectsViewModel;
import com.repos.browser.model.ProjectsViewModelFactory;

import java.util.List;

public class BrowseReposActivity extends AppCompatActivity implements RepositoriesAdapter.RepositoryAdapterHandler {
    ProjectsViewModel mProjectsViewModel;
    EditText mSearchEdit;
    ProgressBar mProgressBar;
    RepositoriesAdapter mReposAdapter;
    String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getIntent() != null && getIntent().hasExtra(LoginActivity.TOKEN)) {
            mAccessToken = getIntent().getStringExtra(LoginActivity.TOKEN);
        }
        if(TextUtils.isEmpty(mAccessToken)) {
            finish();
        }
        mSearchEdit = findViewById(R.id.edit_search);
        mProgressBar = findViewById(R.id.progress_bar_recipes);
        mSearchEdit.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                if(imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                performSearch();
                return true;
            }
            return false;
        });

        mReposAdapter = new RepositoriesAdapter(null, this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_repositories);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(mReposAdapter);

        String[] filters = getResources().getStringArray(R.array.languages);
        Spinner spinner = findViewById(R.id.spinner_languages);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        filters);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mProjectsViewModel.applyFilter(filters[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Nothing to do here
            }
        });

        ProjectsViewModelFactory viewModelFactory = new ProjectsViewModelFactory(mAccessToken);
        mProjectsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProjectsViewModel.class);
        mProjectsViewModel.getRepositories().observe(this, this::handleResponse);
    }

    private void handleResponse(List<Item> items) {
        mProgressBar.setVisibility(View.GONE);
        if(items == null) {
            Toast.makeText(this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            return;
        }

        mReposAdapter.setData(items);
        mReposAdapter.notifyDataSetChanged();
        Log.d("BrowsRepos", "Search items " + items.size());
    }

    private void performSearch(){
        String query = mSearchEdit.getText().toString();
        if(TextUtils.isEmpty(query)) {
            Toast.makeText(this, getString(R.string.search_query_needed), Toast.LENGTH_SHORT).show();
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            mProjectsViewModel.searchBy(query);
        }
    }

    @Override
    public void onItemClicked(Item repository) {
        Uri webUrl = Uri.parse(repository.getHtmlUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, webUrl);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
