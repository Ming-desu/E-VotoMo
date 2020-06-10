package com.qqdota.evotomo.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.qqdota.evotomo.R;
import com.qqdota.evotomo.viewmodels.IntroViewModel;

public class IntroSetupAccountFragment extends Fragment {
    private TextInputLayout textLayoutFirstname, textLayoutLastname, textLayoutUsername, textLayoutPassword;
    private TextInputLayout[] textLayouts;

    private IntroViewModel introViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro_setup_account, container, false);
        initializeUIComponents(view);
        initializeComponents();
        return view;
    }

    /**
     * The method that will initialize all the view models and other classes
     * or components needed for this activity to run
     */
    private void initializeComponents() {
        introViewModel = new ViewModelProvider(getActivity()).get(IntroViewModel.class);
        introViewModel.setTextLayouts(textLayouts);
    }

    /**
     * The method that will initialize all the UI components
     * @param view
     */
    private void initializeUIComponents(View view) {
        textLayoutFirstname = view.findViewById(R.id.text_input_firstname);
        textLayoutLastname = view.findViewById(R.id.text_input_lastname);
        textLayoutUsername = view.findViewById(R.id.text_input_username);
        textLayoutPassword = view.findViewById(R.id.text_input_password);
        textLayouts = new TextInputLayout[] {
                textLayoutFirstname,
                textLayoutLastname,
                textLayoutUsername,
                textLayoutPassword
        };
    }
}
