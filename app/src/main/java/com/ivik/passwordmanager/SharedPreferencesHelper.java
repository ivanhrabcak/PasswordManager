package com.ivik.passwordmanager;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesHelper {
    public static boolean isFirstTimeLaunch(Context context) {
        return getBoolean(context, "isFirstTimeLaunch");
    }

    public static void setFirstTimeLaunch(Context context, boolean value) {
        SharedPreferences.Editor editor = getEditor(context);

        editor.putBoolean("isFirstTimeLaunch", value);
        editor.apply();
    }

    public static void changePassword(Context context, String encryptedPassword) {
        SharedPreferences.Editor editor = getEditor(context);

        editor.putString("passwordHash", encryptedPassword);
        editor.apply();
    }

    private static boolean getBoolean(Context context, String key) {
        return context.getSharedPreferences("data", MODE_PRIVATE)
                .getBoolean(key, true);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return context.getSharedPreferences("data", MODE_PRIVATE).edit();
    }
}
