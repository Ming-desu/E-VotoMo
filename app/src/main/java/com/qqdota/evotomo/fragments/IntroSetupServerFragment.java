package com.qqdota.evotomo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.qqdota.evotomo.R;
import com.qqdota.evotomo.activities.LoginActivity;
import com.qqdota.evotomo.utils.MyDialog;
import com.qqdota.evotomo.utils.PrefManager;
import com.qqdota.evotomo.utils.Redirect;
import com.qqdota.evotomo.viewmodels.IntroViewModel;
import com.qqdota.evotomo.viewmodels.IntroViewModelFactory;

public class IntroSetupServerFragment extends Fragment {
    private Button buttonTest;
    private TextInputLayout textLayoutDomain;

    private IntroViewModel introViewModel;
    private PrefManager prefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro_setup_server, container, false);
        initializeUIComponents(view);
        initializeComponents();
        return view;
    }

    private void initializeComponents() {
        prefManager = new PrefManager(getActivity());
        introViewModel = new ViewModelProvider(this, new IntroViewModelFactory(getActivity())).get(IntroViewModel.class);
        introViewModel.setTestConnectionListener(new TestConnectionListener());
    }

    private void initializeUIComponents(View view) {
        buttonTest = view.findViewById(R.id.button_test);
        textLayoutDomain = view.findViewById(R.id.text_input_domain);

        buttonTest.setOnClickListener(new ButtonTestOnClickListener());
        textLayoutDomain.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0)
                    textLayoutDomain.setError(null);
                introViewModel.setUrl(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean validate() {
        if (!TextUtils.isEmpty(textLayoutDomain.getEditText().getText().toString()))
            return true;

        textLayoutDomain.setError("Server address cannot be empty");
        textLayoutDomain.getEditText().requestFocusFromTouch();
        return false;
    }

    private void testServer() {
        if (!validate())
            return;
        textLayoutDomain.getEditText().clearFocus();
        introViewModel.testConnection();
    }

    private void redirectToLogin() {
        prefManager.setFirstTimeLaunch(false);
        prefManager.setBaseUrl(introViewModel.getUrl());
        Redirect.getInstance().FromTo(getActivity(), LoginActivity.class, null);
    }

    private final class ButtonTestOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            testServer();
        }
    }

    public final class TestConnectionListener implements IntroViewModel.ITestConnection {

        @Override
        public void onSucess(boolean hasAccount) {
            if (!hasAccount)
                return;
            redirectToLogin();
        }

        @Override
        public void OnFailure() {
            MyDialog.getInstance().createDialog(getActivity(), "Oops. Something went wrong.", "Cannot connect to the server. Check the following address if correct.").show();
        }
    }
}
