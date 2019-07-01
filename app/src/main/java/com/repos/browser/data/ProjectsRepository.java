package com.repos.browser.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.repos.browser.model.Item;

import java.util.List;

public interface ProjectsRepository {
    LiveData<List<Item>> searchForProjects(@NonNull final String query);
}
