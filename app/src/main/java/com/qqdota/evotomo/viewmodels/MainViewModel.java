package com.qqdota.evotomo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private boolean isRendered = false;
    private String currentTab;
    private MutableLiveData<String> queryText = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>();

    public boolean isRendered() {
        return isRendered;
    }

    public void setRendered(boolean rendered) {
        isRendered = rendered;
    }

    public String getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(String tab) {
        currentTab = tab;
    }

    public LiveData<String> getQueryText() {
        return queryText;
    }

    public void setQueryText(String query) {
        queryText.setValue(query);
    }

    public LiveData<Boolean> isRefreshing() {
        return isRefreshing;
    }

    public void setIsRefreshing(Boolean value) {
        isRefreshing.setValue(value);
    }
}
