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
import com.qqdota.evotomo.api.IResponse;
import com.qqdota.evotomo.models.Code;
import com.qqdota.evotomo.models.MyResponse;
import com.qqdota.evotomo.models.SessionResponse;
import com.qqdota.evotomo.models.SessionForm;
import com.qqdota.evotomo.utils.MyDialog;
import com.qqdota.evotomo.utils.PrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class SessionViewModel extends ViewModel {
    private PrefManager prefManager;
    private ApiInterface apiInterface;
    private Context context;
    private MutableLiveData<List<SessionResponse>> sessions = new MutableLiveData<>();
    private SessionResponse sessionResponse;

    /**
     * Class Constructor
     * @param context the application context
     */
    public SessionViewModel(Context context) {
        this.context = context;
        this.prefManager = new PrefManager(context);
        apiInterface = ApiClient.getApiClient(prefManager.getBaseUrl()).create(ApiInterface.class);
    }

    /**
     * The getter method of the session responses
     * @return LiveData of the session responses
     */
    public LiveData<List<SessionResponse>> getSessions() {
        return sessions;
    }

    /**
     * The setter method of the session responses
     * @param sessionResponses the session responses requested from the server
     */
    public void setSessions(List<SessionResponse> sessionResponses) {
        this.sessions.setValue(sessionResponses);
    }

    /**
     * The getter method of the session response,
     * gets the session response saved here
     * @return
     */
    public SessionResponse getSessionResponse() {
        return sessionResponse;
    }

    /**
     * The setter method of the session response,
     * saves the session response here
     * @param sessionResponse
     */
    public void setSessionResponse(SessionResponse sessionResponse) {
        this.sessionResponse = sessionResponse;
    }

    /**
     * The method that fetches data from the server
     * @param call the process to be enqueue to the Retrofit
     */
    private void fetch(Call<List<SessionResponse>> call) {
        call.enqueue(new Callback<List<SessionResponse>>() {
            @Override
            public void onResponse(Call<List<SessionResponse>> call, Response<List<SessionResponse>> response) {
                if (response.isSuccessful())
                    setSessions(response.body());
            }

            @Override
            public void onFailure(Call<List<SessionResponse>> call, Throwable t) {
                // TODO :: Handle connection timeout...
                Log.e("Error", t.toString());
            }
        });
    }

    /**
     * The method that will request all the sessions from the server
     */
    public void fetchData() {
        Call<List<SessionResponse>> call = apiInterface.getSessions(prefManager.getWebToken());
        fetch(call);
    }

    /**
     * The method that will request sessions according to the query string
     * @param query the string to be search
     */
    public void fetchData(String query) {
        Call<List<SessionResponse>> call = apiInterface.getSessions(prefManager.getWebToken(), query);
        fetch(call);
    }

    /**
     * The method that will add new session to the database
     * @param session the session model itself
     * @param listener the on success listener
     */
    public void insert(SessionForm session, ISessionResponse.OnSuccessListener listener) {
        Dialog dialog = MyDialog.getInstance().createLoading(context, "Please wait...", "This may take a while.");
        dialog.show();

        Call<SessionResponse> call = apiInterface.insertSession(prefManager.getWebToken(), session);
        call.enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(Call<SessionResponse> call, Response<SessionResponse> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                }
                else {
                    MyResponse myResponse = new Gson().fromJson(response.errorBody().charStream(), MyResponse.class);
                    MyDialog.getInstance().createDialog(context, "Oops. Something went wrong.", myResponse.getMessage()).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<SessionResponse> call, Throwable t) {
                // TODO :: Handle connection timeout...
                Log.e("Error", t.toString());
                dialog.dismiss();
            }
        });
    }

    /**
     * The method tha will request and generate code for the passed session response
     * @param sessionResponse the session response to insert the code
     * @param listener the on success listener
     */
    public void generateCode(SessionResponse sessionResponse, IGenerateCodeListener listener) {
        Dialog dialog = MyDialog.getInstance().createLoading(context, "Generating code.. Please wait...", "This may take a while.");
        dialog.show();

        Call<Code> call = apiInterface.generateCode(prefManager.getWebToken(), sessionResponse.getId());
        call.enqueue(new Callback<Code>() {
            @Override
            public void onResponse(Call<Code> call, Response<Code> response) {
                if (response.isSuccessful())
                    listener.onGenerateCode(response.body());
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Code> call, Throwable t) {
                Log.e("Error", t.toString());
                dialog.dismiss();
            }
        });
    }

    /**
     * The interface for generate code
     */
    public interface IGenerateCodeListener {
        void onGenerateCode(Code code);
    }

    /**
     * The interface for the session response
     */
    public interface ISessionResponse {
        interface OnSuccessListener {
            void onSuccess(SessionResponse sessionResponse);
        }

        interface OnErrorListener {
            void onError();
        }
    }
}
