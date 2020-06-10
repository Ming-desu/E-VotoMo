package com.qqdota.evotomo.viewmodels;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class IntroViewModelFactory implements ViewModelProvider.Factory {
    private Context context;

    public IntroViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) IntroViewModel.getInstance(context);
    }
}
