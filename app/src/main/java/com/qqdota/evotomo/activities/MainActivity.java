package com.qqdota.evotomo.activities;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.qqdota.evotomo.R;
import com.qqdota.evotomo.adapters.PagesAdapter;
import com.qqdota.evotomo.fragments.*;
import com.qqdota.evotomo.utils.MyDialog;
import com.qqdota.evotomo.utils.PrefManager;
import com.qqdota.evotomo.utils.Redirect;
import com.qqdota.evotomo.viewmodels.AuthViewModel;
import com.qqdota.evotomo.viewmodels.MainViewModel;
import com.qqdota.evotomo.viewmodels.SharedViewModelFactory;

public class MainActivity extends AppCompatActivity {
    private final String PAGE = "MAIN_ACTIVITY_PAGE";
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MenuItem menuItemSearch;

    private PrefManager prefManager;
    private AuthViewModel authViewModel;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
        initializeUIComponents();
        renderUIComponents();
    }

    /**
     * The method that will initialize all the view models and other classes
     * or components needed for this activity to run
     */
    private void initializeComponents() {
        // Initialize the components
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        authViewModel = new ViewModelProvider(this, SharedViewModelFactory.getInstance()).get(AuthViewModel.class);
        authViewModel.setContext(this);

        verifyAccess();

        // Observe for the changes of the livedata
        authViewModel.isVerified().observe(this, isVerified -> {
            if (!isVerified)
                Redirect.getInstance().FromTo(MainActivity.this, LoginActivity.class, null);
        });

        authViewModel.getError().observe(this, s -> {
            //if (authViewModel.getPage() == PAGE)
            MyDialog.getInstance().createDialog(MainActivity.this, "Oops. Something went wrong.", s).show();
        });


        mainViewModel.isRefreshing().observe(this, isRefreshing -> {
            if (isRefreshing)
                if (menuItemSearch != null)
                    menuItemSearch.collapseActionView();
                mainViewModel.setQueryText(null);
        });
    }

    /**
     * The method that will initialize all the UI components
     */
    private void initializeUIComponents() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);
    }

    /**
     * The method that will render all the UI components configurations
     */
    private void renderUIComponents() {
        if (!mainViewModel.isRendered()) {
            if (authViewModel.getAccountType() == 0)
                setupPagesAdmin();
            else
                setupPagesStudent();
            mainViewModel.setRendered(true);
        }
    }

    /**
     * The method that will setup the pages to rendered for the admin
     */
    private void setupPagesAdmin() {
        tabLayout.setupWithViewPager(viewPager);

        PagesAdapter adapter = new PagesAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new DashboardFragment(), "Home");
        adapter.addFragment(new SessionFragment(), "Sessions");
        adapter.addFragment(new AccountFragment(), "Accounts");
        adapter.addFragment(new PositionFragment(), "Positions");

        viewPager.setAdapter(adapter);

        int[] icons = {
                R.drawable.ic_home_black_24dp,
                R.drawable.ic_assignment_black_24dp,
                R.drawable.ic_person_black_24dp,
                R.drawable.ic_assessment_black_24dp
        };
        // Setup the icons of the tablaoyut
        for(int i = 0; i < adapter.getCount(); i++) {
            tabLayout.getTabAt(i).setIcon(icons[i]);
        }
    }

    /**
     * The method that will setup the pages to rendered for the student or voter
     */
    private void setupPagesStudent() {
        tabLayout.setupWithViewPager(viewPager);

        PagesAdapter adapter = new PagesAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new StudentSessionFragment(), "Joined Session");

        viewPager.setAdapter(adapter);
    }

    /**
     * The method that will verify current user if it has authorization or not
     */
    private void verifyAccess() {
        if (!mainViewModel.isRendered())
            authViewModel.verifyAccess();
    }

    /**
     * The method to logout the user
     */
    private void logoutUser() {
        authViewModel.logout();
    }

    /**
     * The interface class for the search query text
     */
    private final class SearchViewQueryTextListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            if (!TextUtils.isEmpty(query))
                mainViewModel.setQueryText(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }

    /**
     * The method executes when the app creates its menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);

        // Gets the search view and other components associated with it
        menuItemSearch = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)menuItemSearch.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchViewQueryTextListener());

        return true;
    }

    /**
     * The method executes when the user selects an item from the menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.menu_profile:
                break;
            case R.id.menu_settings:
                break;
            case R.id.menu_logout:
                logoutUser();
                break;
            default:
                break;
        }

        return true;
    }
}
