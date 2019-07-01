package com.repos.browser.model;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Factory for ProjectsViewModel
 */
public class ProjectsViewModelFactory implements ViewModelProvider.Factory {

    private final String mToken;

    public ProjectsViewModelFactory(String token) {
        mToken = token;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProjectsViewModel.class)) {
            return (T) new ProjectsViewModel(mToken);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}