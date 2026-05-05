package com.cleanerapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionManager {

    private static final String PREF_NAME = "CleanerAppSession";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_FCM_TOKEN = "fcm_token";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    @Inject
    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserSession(int userId, String name, String email, String role) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_ROLE, role);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }

    public String getUserRole() {
        return sharedPreferences.getString(KEY_USER_ROLE, "USER");
    }

    public boolean isCleaner() {
        return "CLEANER".equals(getUserRole());
    }

    public void saveFcmToken(String token) {
        editor.putString(KEY_FCM_TOKEN, token);
        editor.apply();
    }

    public String getFcmToken() {
        return sharedPreferences.getString(KEY_FCM_TOKEN, "");
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
