package com.ivik.passwordmanager;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    public static boolean isFirstTimeLaunch(Context context) {
        return getBoolean(context, "isFirstTimeLaunch", true);
    }

    public static void setFirstTimeLaunch(Context context) {
        putBoolean(context, "isFirstTimeLaunch", false);
    }

    public static String getPasswordHash(Context context) {
        return getString(context, "passwordHash");
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static String getString(Context context, String key) {
        return getSharedPreferences(context).getString(key, null);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putString(key, value);
        editor.apply();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("data", Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
        return getSharedPreferences(context).edit();
    }
}
