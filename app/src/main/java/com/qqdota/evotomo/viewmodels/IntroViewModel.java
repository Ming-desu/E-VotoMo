package com.qqdota.evotomo.viewmodels;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.qqdota.evotomo.api.ApiClient;
import com.qqdota.evotomo.api.TestApiInterface;
import com.qqdota.evotomo.fragments.IntroSetupServerFragment;
import com.qqdota.evotomo.models.Account;
import com.qqdota.evotomo.models.MyResponse;
import com.qqdota.evotomo.models.TestResponse;
import com.qqdota.evotomo.utils.MyDialog;
import com.qqdota.evotomo.utils.PrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroViewModel extends ViewModel {
    private static IntroViewModel instance;
    private Context context;
    private PrefManager prefManager;
    private TestApiInterface apiInterface;
    private String url;
    private TextInputLayout[] textLayouts;
    private IntroSetupServerFragment.TestConnectionListener listener;
    private Account account;

    private MutableLiveData<Boolean> goNext = new MutableLiveData<>();

    public IntroViewModel(Context context) {
        this.context = context;
        prefManager = new PrefManager(context);
    }

    public static IntroViewModel getInstance(Context context) {
        if (instance == null)
            instance = new IntroViewModel(context);
        return instance;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TextInputLayout[] getTextLayouts() {
        return textLayouts;
    }

    public void setTextLayouts(TextInputLayout[] textLayouts) {
        this.textLayouts = textLayouts;
    }

    public void setTestConnectionListener(IntroSetupServerFragment.TestConnectionListener listener) {
        this.listener = listener;
    }

    public LiveData<Boolean> getGoNext() {
        return goNext;
    }

    public void setGoNext(Boolean goNext) {
        this.goNext.setValue(goNext);
    }

    public void testConnection() {
        Dialog dialog = MyDialog.getInstance().createLoading(context, "Connecting to the server...", "This may take up a while.");
        dialog.show();

        apiInterface = ApiClient.getTestApiClient(url).create(TestApiInterface.class);
        Call<TestResponse> call = apiInterface.test();
        call.enqueue(new Callback<TestResponse>() {
            @Override
            public void onResponse(Call<TestResponse> call, Response<TestResponse> response) {
                if (response.isSuccessful()) {
                    listener.onSucess(response.body().isHasAccount());
                    setGoNext(true);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<TestResponse> call, Throwable t) {
                Log.e("Error", t.toString());
                listener.OnFailure();
                dialog.dismiss();
            }
        });
    }

    public void checkAccount() {
        Dialog dialog = MyDialog.getInstance().createLoading(context, "Checking account information...", "This may take up a while.");
        dialog.show();

        account = new Account(
                textLayouts[0].getEditText().getText().toString(),
                textLayouts[1].getEditText().getText().toString(),
                textLayouts[2].getEditText().getText().toString(),
                textLayouts[3].getEditText().getText().toString()
        );

        apiInterface = ApiClient.getTestApiClient(url).create(TestApiInterface.class);
        Call<MyResponse> call = apiInterface.checkAccount(account);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.isSuccessful()) {
                    prefManager.setBaseUrl(ApiClient.parseURL(url));
                    prefManager.setFirstTimeLaunch(false);
                    setGoNext(true);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.e("Error", t.toString());
                dialog.dismiss();
            }
        });
    }

    private void createAccount() {
        Dialog dialog = MyDialog.getInstance().createLoading(context, "Finalizing the setup of the application...", "This may take up a while.");
        dialog.show();

        apiInterface = ApiClient.getTestApiClient(url).create(TestApiInterface.class);
        Call<MyResponse> call = apiInterface.createAccount(account, true);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.isSuccessful()) {
                    setGoNext(true);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.e("Error", t.toString());
                dialog.dismiss();
            }
        });
    }

    public void validate(int page) {
        Log.i("Page", String.valueOf(page));
        setGoNext(false);
        if (page == 1)
            testConnection();
        if (page == 2)
            if (validateForm())
                checkAccount();
        if (page == 3)
            createAccount();
    }

    public boolean validateForm() {
        boolean isValid = true;

        String[] errorTexts = {
                "Firstname cannot be empty.",
                "Lastname cannot be empty.",
                "Username cannot be empty.",
                "Password cannot be empty."
        };

        for (int i = 0; i < textLayouts.length; i++) {
            if (TextUtils.isEmpty(textLayouts[i].getEditText().getText().toString())) {
                final int index = i;
                textLayouts[i].setError(errorTexts[i]);
                textLayouts[i].getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (count > 0)
                            textLayouts[index].setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                textLayouts[i].getEditText().requestFocusFromTouch();
                isValid = false;
                break;
            }
        }

        return isValid;
    }

    public interface ITestConnection {
        void onSucess(boolean hasAccount);
        void OnFailure();
    }
}
