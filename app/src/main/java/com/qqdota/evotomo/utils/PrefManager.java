package com.qqdota.evotomo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private final String PREF_NAME = "android-evotomo";
    private final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private final String JWT_TOKEN = "WebToken";
    private final String BASE_URL = "BaseUrl";

    public PrefManager(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setWebToken(String token) {
        editor.putString(JWT_TOKEN, token);
        editor.commit();
    }

    public void clearWebToken() {
        editor.remove(JWT_TOKEN);
        editor.commit();
    }

    public String getWebToken() {
        return sharedPreferences.getString(JWT_TOKEN, null);
    }

    public void setBaseUrl(String url) {
        editor.putString(BASE_URL, url);
        editor.commit();
    }

    public String getBaseUrl() {
        return sharedPreferences.getString(BASE_URL, null);
    }
}
