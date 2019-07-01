package com.repos.browser.model;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.repos.browser.data.ProjectsRepository;
import com.repos.browser.data.ProjectsRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class ProjectsViewModel extends ViewModel {
    private MediatorLiveData<List<Item>> mProjectsLiveData;
    private LiveData<List<Item>> mSourceLiveData;
    private ProjectsRepository mProjectsRepository;
    private String mFilter;

    public ProjectsViewModel(@NonNull final String token) {
        mProjectsLiveData = new MediatorLiveData<>();
        mProjectsRepository = new ProjectsRepositoryImpl(token);
    }

    @NonNull
    public LiveData<List<Item>> getRepositories() {
        return mProjectsLiveData;
    }

    public void applyFilter(String filter) {
        mFilter = filter;
        if(mSourceLiveData != null) {
            LiveData<List<Item>> filteredMap = Transformations.map(mSourceLiveData, repositories -> {
                if("None".equals(filter)) {
                    return repositories;
                }
                List<Item> filteredList = new ArrayList<>();
                for (Item item : repositories) {
                    if (filter.equals(item.getLanguage())) {
                        filteredList.add(item);
                    }
                }
                return filteredList;
            });

            mProjectsLiveData.removeSource(mSourceLiveData);
            mProjectsLiveData.addSource(filteredMap, items -> mProjectsLiveData.setValue(items));
        }
    }

    public void searchBy(@NonNull final String query) {
        mSourceLiveData = mProjectsRepository.searchForProjects(query);
        if(TextUtils.isEmpty(mFilter)) {
            mProjectsLiveData.addSource(
                    mSourceLiveData,
                    items -> mProjectsLiveData.setValue(items)
            );
        } else {
            applyFilter(mFilter);
        }
    }
}
