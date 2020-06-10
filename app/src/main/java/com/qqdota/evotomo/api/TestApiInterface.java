package com.qqdota.evotomo.api;

import com.qqdota.evotomo.models.Account;
import com.qqdota.evotomo.models.MyResponse;
import com.qqdota.evotomo.models.TestResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface TestApiInterface {
    @GET("test/check")
    Call<TestResponse> test();

    @POST("test/check/account")
    Call<MyResponse> checkAccount(@Body Account account);

    @POST("test/check/account")
    Call<MyResponse> createAccount(@Body Account account, @Query("create") boolean create);
}
