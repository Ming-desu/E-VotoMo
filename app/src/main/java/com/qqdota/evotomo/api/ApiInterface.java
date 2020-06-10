package com.qqdota.evotomo.api;

import com.qqdota.evotomo.models.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<MyResponse> login(@Field("username") String username, @Field("password") String password);

    @GET("auth/verify")
    Call<AuthResponse> verify(@Header("Authorization") String authorization);

    @GET("sessions")
    Call<List<SessionResponse>> getSessions(@Header("Authorization") String authorization);

    @GET("sessions")
    Call<List<SessionResponse>> getSessions(@Header("Authorization") String authorization, @Query("query") String query);

    @POST("sessions")
    Call<SessionResponse> insertSession(@Header("Authorization") String authorization, @Body SessionForm session);

    @FormUrlEncoded
    @POST("codes")
    Call<Code> generateCode(@Header("Authorization") String authorization, @Field("session_id") int sessionId);
}
