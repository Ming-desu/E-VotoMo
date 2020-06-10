package com.qqdota.evotomo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.qqdota.evotomo.R;
import com.qqdota.evotomo.activities.SessionDetailsActivity;
import com.qqdota.evotomo.adapters.SessionAdapter;
import com.qqdota.evotomo.models.SessionResponse;
import com.qqdota.evotomo.utils.MyDialog;
import com.qqdota.evotomo.utils.PrefManager;
import com.qqdota.evotomo.utils.RecyclerViewScrollListener;
import com.qqdota.evotomo.viewmodels.*;

public class SessionFragment extends Fragment implements SessionAdapter.ISessionAdapter {
    private final int SESSION_ADD = 1000;
    private final String SESSION_TAB = "SESSION_TAB";
    private RecyclerView recyclerView;
    private PrefManager prefManager;
    private FloatingActionButton fabAdd;
    private SwipeRefreshLayout swipeRefresh;

    private MainViewModel mainViewModel;
    private SessionViewModel sessionViewModel;
    private SessionAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_session, container, false);
        initializeUIComponents(view);
        initializeComponents();
        return view;
    }

    /**
     * The method that will initialize all the view models and other classes
     * or components needed for this activity to run
     */
    private void initializeComponents() {
        prefManager = new PrefManager(getContext());
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        sessionViewModel = new ViewModelProvider(getActivity(), new SessionViewModelFactory(getContext())).get(SessionViewModel.class);
    }

    /**
     * The method that will initialize all the UI components
     * @param view the layout view of the current fragment
     */
    private void initializeUIComponents(View view) {
        fabAdd = view.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new FabButtonClickListener());
        fabAdd.hide();

        recyclerView = view.findViewById(R.id.recycler_view_active_sessions);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SessionAdapter(null, getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(fabAdd));

        swipeRefresh = view.findViewById(R.id.swipe_layout);
        swipeRefresh.setOnRefreshListener(new RefreshListener());
    }

    /**
     * The method that will fetch date from the server
     * @param forced if set to true, the data will be reloaded no matter what
     */
    private void fetchData(boolean forced) {
        if (sessionViewModel.getSessions().getValue() != null && !forced)
            return;

        // Observe for the list of session responses
        sessionViewModel.getSessions().observe(this, sessionResponses -> {
            adapter.setSessionResponses(sessionResponses);
            swipeRefresh.setRefreshing(false);
        });

        // Observe for the query text to be searched
        mainViewModel.getQueryText().observe(this, query -> {
            if (mainViewModel.getCurrentTab().equals(SESSION_TAB)
                    && query != null
                    && !TextUtils.isEmpty(query))
                sessionViewModel.fetchData(query);
        });

        sessionViewModel.fetchData();
    }

    /**
     * The interface class for the floating action button
     */
    private final class FabButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            startActivityForResult(new Intent(getActivity(), SessionDetailsActivity.class), SESSION_ADD);
        }
    }

    /**
     * The interface class for the swipe refresh layout
     */
    private final class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            mainViewModel.setIsRefreshing(true);
            fetchData(true);
        }
    }

    /**
     * The method when the app resume the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        mainViewModel.setCurrentTab(SESSION_TAB);
        fetchData(false);
        fabAdd.show();
    }

    /**
     * The method when the app pause the activity
     */
    @Override
    public void onPause() {
        super.onPause();
        fabAdd.hide();
    }

    /**
     * The method gets the result from the startActivityForResult
     * @param requestCode the unique code for the activity
     * @param resultCode the result of the activity
     * @param data the data sent back to this activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SESSION_ADD)
            if (resultCode == Activity.RESULT_OK) {
                SessionResponse sessionResponse = new Gson().fromJson(data.getStringExtra("sessionResponse"), SessionResponse.class);
                adapter.insertSession(sessionResponse);
                MyDialog.getInstance().createDialog(getActivity(), "Notification:", "Session successfully created.\n" + data.getStringExtra("sessionResponse")).show();
            }
    }

    /**
     * The method when the generate code was clicked
     * @param sessionResponse the session response associated
     */
    @Override
    public void onClickGenerateCode(SessionResponse sessionResponse) {
        if (sessionResponse.getCode() == null) {
            sessionViewModel.generateCode(sessionResponse, code -> {
                adapter.setSessionCode(code);
                MyDialog.getInstance().createDialog(getActivity(),"Code Generated:", code.getCode()).show();
            });
        }
        else
            MyDialog.getInstance().createDialog(getActivity(),"Code Generated:", sessionResponse.getCode().getCode()).show();
    }

    /**
     * The method when the view more was clicked
     * @param sessionResponse
     */
    @Override
    public void onClickViewMore(SessionResponse sessionResponse) {

    }
}
