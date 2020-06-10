package com.qqdota.evotomo.viewmodels;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SessionViewModelFactory implements ViewModelProvider.Factory {
    private Context context;

    public SessionViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SessionViewModel(context);
    }
}
