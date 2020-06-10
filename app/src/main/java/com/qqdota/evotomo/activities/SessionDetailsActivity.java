package com.qqdota.evotomo.activities;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.qqdota.evotomo.R;
import com.qqdota.evotomo.api.IResponse;
import com.qqdota.evotomo.dialogs.DatePicker;
import com.qqdota.evotomo.dialogs.TimePicker;
import com.qqdota.evotomo.models.SessionResponse;
import com.qqdota.evotomo.viewmodels.*;

public class SessionDetailsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputLayout textLayoutName, textLayoutDateStart, textLayoutDateEnd, textLayoutTimeStart, textLayoutTimeEnd;
    private TextInputEditText editTextName, editTextDateStart, editTextDateEnd, editTextTimeStart, editTextTimeEnd;
    private Button buttonSave;

    private AuthViewModel authViewModel;
    private SessionViewModel sessionViewModel;
    private SessionResponse sessionResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_details);

        initializeUIComponents();
        initializeComponents();
    }

    /**
     * The method that will initialize all the view models and other classes
     * or components needed for this activity to run
     */
    private void initializeComponents() {
        authViewModel = new ViewModelProvider(this, SharedViewModelFactory.getInstance()).get(AuthViewModel.class);
        sessionViewModel = new ViewModelProvider(this, new SessionViewModelFactory(this)).get(SessionViewModel.class);
    }

    /**
     * The method that will initialize all the UI components
     */
    private void initializeUIComponents() {
        // Setup the toolbar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add the back button to the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textLayoutName = findViewById(R.id.text_input_name);
        textLayoutDateStart = findViewById(R.id.text_input_date_start);
        textLayoutDateEnd = findViewById(R.id.text_input_date_end);
        textLayoutTimeStart = findViewById(R.id.text_input_time_start);
        textLayoutTimeEnd = findViewById(R.id.text_input_time_end);

        editTextName = findViewById(R.id.edit_text_name);
        editTextDateStart = findViewById(R.id.edit_text_date_start);
        editTextDateEnd = findViewById(R.id.edit_text_date_end);
        editTextTimeStart = findViewById(R.id.edit_text_time_start);
        editTextTimeEnd = findViewById(R.id.edit_text_time_end);
        buttonSave = findViewById(R.id.button_save);

        editTextDateStart.setOnFocusChangeListener(new DateStartOnFocusListener());
        editTextDateEnd.setOnFocusChangeListener(new DateEndOnFocusListener());
        editTextTimeStart.setOnFocusChangeListener(new TimeStartOnFocusListener());
        editTextTimeEnd.setOnFocusChangeListener(new TimeEndOnFocusListener());
        buttonSave.setOnClickListener(new ButtonSaveOnClickListener());
    }

    /**
     * The method that will retrieve the save state of the UI data from the view model
     */
    private void retrieveSession() {
        if (sessionViewModel.getSessionResponse() != null) {
            SessionResponse sessionResponse = sessionViewModel.getSessionResponse();
            editTextName.setText(sessionResponse.getName());

            try {
                editTextDateStart.setText(sessionResponse.getVoteStart().split("-")[0]);
                editTextTimeStart.setText(sessionResponse.getVoteStart().split("-")[1]);

                editTextDateEnd.setText(sessionResponse.getVoteEnd().split("-")[0]);
                editTextTimeEnd.setText(sessionResponse.getVoteEnd().split("-")[1]);
            }
            catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The method that will insert new session
     */
    private void insertSession() {
        // Check if there is an error
        if (!validateForm())
            return;

        // Create the session response model
        sessionResponse = new SessionResponse(
                editTextName.getText().toString(),
                editTextDateStart.getText().toString() + " " + editTextTimeStart.getText().toString(),
                editTextDateEnd.getText().toString() + " " + editTextTimeEnd.getText().toString(),
                authViewModel.getAccount(),
                null
        );

        // Insert the session to the database
        sessionViewModel.insert(sessionResponse.getSessionForm(), new OnSuccessListener());
    }

    /**
     * The method that will validate the form
     * @return true if there are no validation error
     */
    private boolean validateForm() {
        boolean isValid = true;

        String[] errorTexts = {
                "Session name cannot be empty.",
                "Date start cannot be empty.",
                "Time start cannot be empty.",
                "Date end cannot be empty.",
                "Time end cannot be empty."
        };

        TextInputEditText[] editTexts = {
                editTextName,
                editTextDateStart,
                editTextTimeStart,
                editTextDateEnd,
                editTextTimeEnd
        };

        TextInputLayout[] textLayouts = {
                textLayoutName,
                textLayoutDateStart,
                textLayoutTimeStart,
                textLayoutDateEnd,
                textLayoutTimeEnd
        };

        for (int i = 0; i < editTexts.length; i++) {
            if (TextUtils.isEmpty(editTexts[i].getText())) {
                textLayouts[i].setError(errorTexts[i]);
                textLayouts[i].addOnEditTextAttachedListener(textInputLayout -> textInputLayout.setError(null));
                editTexts[i].requestFocus();
                isValid = false;
                break;
            }
        }

        return isValid;
    }

    /**
     * The method that will save the state of the UI to the view model
     */
    private void saveSession() {
        sessionViewModel.setSessionResponse(new SessionResponse(
                editTextName.getText().toString(),
                editTextDateStart.getText().toString() + "-" + editTextTimeStart.getText().toString(),
                editTextDateEnd.getText().toString() + "-" + editTextTimeEnd.getText().toString(),
                null,
                null
        ));
    }

    /**
     * The interface class for the on success
     */
    private final class OnSuccessListener implements SessionViewModel.ISessionResponse.OnSuccessListener {
        @Override
        public void onSuccess(SessionResponse sessionResponse) {
            Intent intent = new Intent();
            intent.putExtra("sessionResponse", new Gson().toJson(sessionResponse));
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    /**
     * The interface class for the date start
     */
    private final class DateStartOnFocusListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                new DatePicker(new DatePicker.IDatePicker() {
                    @Override
                    public void onDatePicked(String date) {
                        editTextDateStart.setText(date);
                        editTextDateStart.setError(null);
                        editTextDateStart.clearFocus();
                    }
                }).show(getSupportFragmentManager(), "datePicker");
        }
    }

    /**
     * The interface class for the date end
     */
    private final class DateEndOnFocusListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                new DatePicker(new DatePicker.IDatePicker() {
                    @Override
                    public void onDatePicked(String date) {
                        editTextDateEnd.setText(date);
                        editTextDateEnd.setError(null);
                        editTextDateEnd.clearFocus();
                    }
                }).show(getSupportFragmentManager(), "datePicker");
        }
    }

    /**
     * The interface class for the time start
     */
    private final class TimeStartOnFocusListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                new TimePicker(new TimePicker.ITimePicker() {
                    @Override
                    public void onTimePicked(String time) {
                        editTextTimeStart.setText(time);
                        editTextTimeStart.setError(null);
                        editTextTimeStart.clearFocus();
                    }
                }).show(getSupportFragmentManager(), "timePicker");
        }
    }

    /**
     * The interface class for the time end
     */
    private final class TimeEndOnFocusListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                new TimePicker(new TimePicker.ITimePicker() {
                    @Override
                    public void onTimePicked(String time) {
                        editTextTimeEnd.setText(time);
                        editTextTimeEnd.setError(null);
                        editTextTimeEnd.clearFocus();
                    }
                }).show(getSupportFragmentManager(), "timePicker");
        }
    }

    /**
     * The interface class for the button save
     */
    private final class ButtonSaveOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            insertSession();
        }
    }

    /**
     * The method executes when the user tries to go back or press back button
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * The method executes when the app creates its menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_save, menu);
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
            case R.id.menu_save:
                insertSession();
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * The method when the app resume the activity
     */
    @Override
    protected void onResume() {
        super.onResume();
        retrieveSession();
    }

    /**
     * The method when the app pause the activity
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveSession();
    }
}
