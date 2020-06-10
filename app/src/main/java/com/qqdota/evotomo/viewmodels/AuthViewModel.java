package com.qqdota.evotomo.viewmodels;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.gson.Gson;
import com.qqdota.evotomo.api.ApiClient;
import com.qqdota.evotomo.api.ApiInterface;
import com.qqdota.evotomo.api.IExecute;
import com.qqdota.evotomo.api.IResponse;
import com.qqdota.evotomo.models.Account;
import com.qqdota.evotomo.models.AuthResponse;
import com.qqdota.evotomo.models.MyResponse;
import com.qqdota.evotomo.utils.MyDialog;
import com.qqdota.evotomo.utils.PrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The view model responsible for the authentication
 * and security of the application itself.
 */
public class AuthViewModel extends ViewModel {
    private static AuthViewModel instance;
    private PrefManager prefManager;
    private Context context;
    private Dialog dialog;
    private ApiInterface apiInterface;

    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<Boolean> isVerified = new MutableLiveData<>();
    private Account account;
    private int accountType;
    private String page;

    /**
     * Singleton approach, in order to share this view model into different activities
     * This will return the same instance of AuthViewModel in every activity
     */
    public synchronized static AuthViewModel getInstance() {
        if (instance == null)
            instance = new AuthViewModel();
        return instance;
    }

    /**
     * The setter method for the context
     * @param context the application context
     */
    public void setContext(Context context) {
        this.context = context;
        this.prefManager = new PrefManager(context);
        apiInterface = ApiClient.getApiClient(prefManager.getBaseUrl()).create(ApiInterface.class);
    }

    /**
     *
     * @return
     */
    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> isVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean value) {
        isVerified.setValue(value);
    }

    public Account getAccount() {
        return account;
    }

    public int getAccountType() {
        return accountType;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    /**
     * The method that will verify if the token is valid or not
     */
    public void verifyAccess() {
        verifyAccess(null);
    }

    /**
     * The method that will verify if token is valid or not
     * @param listener the task to do before and after the execution
     */
    public void verifyAccess(IExecute listener) {
        verify(listener);
    }

    /**
     * The method that will verify if the token is valid or not
     * @param listener the task to do before and after the execution
     */
    private void verify(IExecute listener) {
        Log.i("Executed", "YES");
        // Do something before the executing the asynchronous call
        if (listener != null)
            listener.onPreExecute();

        Call<AuthResponse> call = apiInterface.verify("Bearer " + prefManager.getWebToken());
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    accountType = response.body().getType();
                    Log.i("TYPE", String.valueOf(response.body().getType()));
                    account = new Gson().fromJson(response.body().getResponse(), Account.class);
                    isVerified.setValue(true);
                }
                else {
                    AuthResponse authResponse = new Gson().fromJson(response.errorBody().charStream(), AuthResponse.class);
                    error.setValue(authResponse.getResponse());
                    isVerified.setValue(false);
                }

                // Do something after the asynchronous call
                if (listener != null)
                    listener.onPostExecute();
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                error.setValue(t.toString());

                // Do something after the asynchronous call
                if (listener != null)
                    listener.onPostExecute();
            }
        });
    }

    /**
     * The method that will try to authorize user via their login credentials
     * @param username the username of the user
     * @param password the password of the user
     */
    public void login(String username, String password, IResponse.OnSuccessListener listener) {
        dialog = MyDialog.getInstance().createLoading(context, "Please wait...", "This may take up a while.");
        dialog.show();

        Call<MyResponse> call = apiInterface.login(username, password);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.isSuccessful()) {
                    prefManager.setWebToken(response.body().getResponse());
                    isVerified.setValue(true);
                    listener.onSuccess();
                }
                else {
                    MyResponse myResponse = new Gson().fromJson(response.errorBody().charStream(), MyResponse.class);
                    error.setValue(myResponse.getMessage());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                // SocketTimeoutException
                error.setValue(t.toString());
                dialog.dismiss();
            }
        });
    }

    /**
     * The method to logout the current session
     */
    public void logout() {
        prefManager.clearWebToken();
        isVerified.setValue(false);
    }
}
