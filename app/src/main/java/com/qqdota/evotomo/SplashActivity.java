package com.qqdota.evotomo;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.qqdota.evotomo.activities.IntroActivity;
import com.qqdota.evotomo.activities.LoginActivity;
import com.qqdota.evotomo.activities.MainActivity;
import com.qqdota.evotomo.utils.PrefManager;
import com.qqdota.evotomo.utils.Redirect;

public class SplashActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initializeComponents();
        initializeUIComponents();

        if (!isFirstTimeLaunch())
            checkWebToken();
    }

    /**
     * The method that will initialize all the view models and other classes
     * or components needed for this activity to run
     */
    private void initializeComponents() {
        // Initialize the components
        prefManager = new PrefManager(this);
    }

    /**
     * The method that will initialize all the UI components
     */
    private void initializeUIComponents() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        progressBar = findViewById(R.id.progress_bar);
    }

    /**
     * The method that will check whether there is a web token existing in our app
     */
    private void checkWebToken() {
        // Check if there is a token saved to our app
        // Delay for 2 seconds to ensure that the splash screen
        // will be visible for the user to see
        new Handler().postDelayed(() -> {
            // Launch the login activity since there is no token available
            if (prefManager.getWebToken() == null)
                Redirect.getInstance().FromTo(SplashActivity.this, LoginActivity.class, null);
            // Launch the main activity since there is a token available
            else
                Redirect.getInstance().FromTo(SplashActivity.this, MainActivity.class, null);
        }, 1500);
    }

    private boolean isFirstTimeLaunch() {
        if (prefManager.isFirstTimeLaunch()) {
            new Handler().postDelayed(() -> {
                Redirect.getInstance().FromTo(SplashActivity.this, IntroActivity.class, null);
            }, 1500);
            return true;
        }
        return false;
    }
}
