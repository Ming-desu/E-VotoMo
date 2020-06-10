package com.qqdota.evotomo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private static SharedViewModel instance;
    private MutableLiveData<String> name = new MutableLiveData<>();

    public static synchronized SharedViewModel getInstance() {
        if (instance == null)
            instance = new SharedViewModel();
        return instance;
    }

    public LiveData<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }
}
