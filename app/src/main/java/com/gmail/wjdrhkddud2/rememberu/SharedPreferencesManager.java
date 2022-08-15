package com.gmail.wjdrhkddud2.rememberu;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;


public class SharedPreferencesManager {

    private static final String SHARED_PREFERENCES_NAME =
            "com.gmail.wjdrhkddud2.rememberu.sharedpreferences";

    private static final String USER_EMAIL = SHARED_PREFERENCES_NAME + "user.email";
    private static final String USER_DISPLAY_NAME = SHARED_PREFERENCES_NAME + "user.name";
    private static final String USER_EMAIL_PASSWORD = SHARED_PREFERENCES_NAME + "user.password";

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static boolean setUserEmail(Context context, String value) {

        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_EMAIL, value);
        return editor.commit();

    }

    public static boolean setUserName(Context context, String value) {
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_DISPLAY_NAME, value);
        return editor.commit();
    }

    public static boolean setUserEmailPassword(Context context, String value) {
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_EMAIL_PASSWORD, value);
        return editor.commit();
    }

    public static String getUserEmail(Context context) {
        return getPreferences(context).getString(USER_EMAIL, null);
    }

    public static String getUserName(Context context) {
        return getPreferences(context).getString(USER_DISPLAY_NAME, null);
    }

    public static String getUserEmailPassword(Context context) {
        return getPreferences(context).getString(USER_EMAIL_PASSWORD, null);
    }


}