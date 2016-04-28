package com.appdev.postify.datastorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.appdev.postify.BaseApplication;

/**
 * Created by daniel on 15.04.16.
 */
public class PreferencesManagement {

    public static final String PREFS_NAME = "com.appdev.postify";

    public static final String SSID_Key = "SSID";
    public static final String NetworkKey_Key = "password";

    public static void saveStringPreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringPreferences(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String objectId = sharedPreferences.getString(key, null);
        return objectId;
    }
    public static void deleteStringPreferences(String key){
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key).apply();
    }

    private static SharedPreferences getSharedPreferences() {
        return BaseApplication.getInstance().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}
