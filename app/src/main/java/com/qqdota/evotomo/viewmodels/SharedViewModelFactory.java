package com.qqdota.evotomo.viewmodels;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SharedViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static SharedViewModelFactory instance;

    public static synchronized SharedViewModelFactory getInstance() {
        if (instance == null)
            instance = new SharedViewModelFactory();
        return instance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) AuthViewModel.getInstance();
    }
}
