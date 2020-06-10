package com.qqdota.evotomo.api;

import android.content.Context;
import com.qqdota.evotomo.utils.PrefManager;
import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "http://192.168.43.85/e-votomo-api/public/";
    public static Retrofit retrofit = null;

    public static Retrofit getApiClient(String url) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getTestApiClient(String url) {
        return new Retrofit.Builder()
                    .baseUrl(ApiClient.parseURL(url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    }

    public static String parseURL(String url) {
        String newUrl = "";

        if (!url.toLowerCase().contains("http://"))
            newUrl += "http://";

        newUrl += url;

        if (url.charAt(url.length() - 1) != '/')
            newUrl += "/";

        newUrl += "e-votomo-api/public/";

        return newUrl;
    }
}
