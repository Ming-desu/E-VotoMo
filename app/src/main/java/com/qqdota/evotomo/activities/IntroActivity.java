package com.qqdota.evotomo.activities;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.qqdota.evotomo.R;
import com.qqdota.evotomo.SplashActivity;
import com.qqdota.evotomo.adapters.PagesAdapter;
import com.qqdota.evotomo.fragments.IntroSetupAccountFragment;
import com.qqdota.evotomo.fragments.IntroSetupDoneFragment;
import com.qqdota.evotomo.fragments.IntroSetupServerFragment;
import com.qqdota.evotomo.utils.NonSwipeableViewPager;
import com.qqdota.evotomo.utils.PageIndicator;
import com.qqdota.evotomo.utils.Redirect;
import com.qqdota.evotomo.viewmodels.IntroViewModel;
import com.qqdota.evotomo.viewmodels.IntroViewModelFactory;

public class IntroActivity extends AppCompatActivity {
    private PageIndicator pageIndicator;
    private Button buttonNext, buttonPrevious;
    private NonSwipeableViewPager viewPager;

    private IntroViewModel introViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        initializeUIComponents();
        initializeComponents();
    }

    private void initializeComponents() {
        introViewModel = new ViewModelProvider(this, new IntroViewModelFactory(this)).get(IntroViewModel.class);
        introViewModel.getGoNext().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean value) {
                if (value) {
                    if (pageIndicator.getPage() == 3)
                        doneSetup();

                    pageIndicator.setPage(pageIndicator.getPage() + 1);
                    viewPager.setCurrentItem(pageIndicator.getPage() - 1);
                    buttonPrevious.setEnabled(true);
                }
            }
        });
    }

    private void initializeUIComponents() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        pageIndicator = findViewById(R.id.page_indicator);
        viewPager = findViewById(R.id.viewpager);

        PagesAdapter adapter = new PagesAdapter(getSupportFragmentManager(), 0);
        adapter.addFragment(new IntroSetupServerFragment(), "Setup Server");
        adapter.addFragment(new IntroSetupAccountFragment(), "Setup Account");
        adapter.addFragment(new IntroSetupDoneFragment(), "Setup Done");

        viewPager.setAdapter(adapter);

        buttonPrevious = findViewById(R.id.button_previous);
        buttonNext = findViewById(R.id.button_next);

        buttonPrevious.setOnClickListener(v -> {
            buttonNext.setText("Next");
            introViewModel.setGoNext(false);
            pageIndicator.setPage(pageIndicator.getPage() - 1);
            viewPager.setCurrentItem(pageIndicator.getPage() - 1);
            if (pageIndicator.getPage() == 1)
                buttonPrevious.setEnabled(false);
        });

        buttonNext.setOnClickListener(v -> {
            if (pageIndicator.getPage() + 1 == 3)
                buttonNext.setText("Finish");

            introViewModel.validate(pageIndicator.getPage());
        });
    }

    private void doneSetup() {
        Redirect.getInstance().FromTo(this, SplashActivity.class, null);
    }
}
