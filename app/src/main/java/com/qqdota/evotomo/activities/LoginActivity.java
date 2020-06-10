package com.qqdota.evotomo.activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.qqdota.evotomo.R;
import com.qqdota.evotomo.api.IResponse;
import com.qqdota.evotomo.utils.MyDialog;
import com.qqdota.evotomo.viewmodels.AuthViewModel;
import com.qqdota.evotomo.viewmodels.SharedViewModelFactory;

public class LoginActivity extends AppCompatActivity {
    private final String PAGE = "LOGIN_ACTIVITY_PAGE";

    private Button buttonLogin;
    private TextInputEditText editTextUsername, editTextPassword;

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeUIComponents();
        initializeComponents();
    }

    /**
     * The method that will initialize all the view models and other classes
     * or components needed for this activity to run
     */
    private void initializeComponents() {
        authViewModel = new ViewModelProvider(this, SharedViewModelFactory.getInstance()).get(AuthViewModel.class);
        authViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (authViewModel.getPage() == PAGE)
                    MyDialog.getInstance().createDialog(LoginActivity.this, "Oops. Something went wrong.", s).show();
            }
        });
    }

    /**
     * The method that will initialize all the UI components
     */
    private void initializeUIComponents() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        buttonLogin = findViewById(R.id.button_login);
        editTextUsername = findViewById(R.id.edit_text_username);
        editTextPassword = findViewById(R.id.edit_text_password);

        buttonLogin.setOnClickListener(new LoginButtonClickListener());
    }

    /**
     * The interface class for the login button
     */
    private final class LoginButtonClickListener implements View.OnClickListener, IResponse.OnSuccessListener {
        @Override
        public void onClick(View v) {
            authViewModel.setContext(LoginActivity.this);
            authViewModel.login(editTextUsername.getText().toString(), editTextPassword.getText().toString(), this);
        }

        @Override
        public void onSuccess() {
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }
}
