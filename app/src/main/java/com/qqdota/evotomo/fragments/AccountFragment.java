package com.qqdota.evotomo.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.qqdota.evotomo.R;
import com.qqdota.evotomo.adapters.PagesAdapter;
import com.qqdota.evotomo.viewmodels.MainViewModel;

public class AccountFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fabAdd;
    private MainViewModel mainViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        fabAdd = view.findViewById(R.id.fab_add);
//        fabAdd.hide();

        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);

        PagesAdapter adapter = new PagesAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new AccountSystemFragment(), "Admin");
        adapter.addFragment(new AccountStudentFragment(), "Students");
        viewPager.setAdapter(adapter);
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
