package com.qqdota.evotomo.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qqdota.evotomo.R;
import com.qqdota.evotomo.viewmodels.MainViewModel;

public class PositionFragment extends Fragment {
    private FloatingActionButton fabAdd;

    private MainViewModel mainViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_position, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        fabAdd = view.findViewById(R.id.fab_add);
        fabAdd.hide();
    }

    @Override
    public void onResume() {
        super.onResume();
        fabAdd.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        fabAdd.hide();
    }
}
