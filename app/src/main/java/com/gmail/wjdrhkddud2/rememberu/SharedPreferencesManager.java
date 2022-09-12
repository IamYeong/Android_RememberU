package com.gmail.wjdrhkddud2.rememberu;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;


public class SharedPreferencesManager {

    private static final String SHARED_PREFERENCES_NAME =
            "com.gmail.wjdrhkddud2.rememberu.sharedpreferences";

    private static final String USER_UID = SHARED_PREFERENCES_NAME + "user.uid";
    private static final String USER_EMAIL = SHARED_PREFERENCES_NAME + "user.email";
    private static final String USER_DISPLAY_NAME = SHARED_PREFERENCES_NAME + "user.name";
    private static final String USER_EMAIL_PASSWORD = SHARED_PREFERENCES_NAME + "user.password";
    private static final String USER_DOWNLOAD_DATE = SHARED_PREFERENCES_NAME + "user.download.time";
    private static final String USER_UPLOAD_DATE = SHARED_PREFERENCES_NAME + "user.upload.time";

    private static final String PERSON_HASH = "com.gmail.wjdrhkddud2.rememberu.sharedpreferences.person.hash";
    private static final String MEMO_HASH = "com.gmail.wjdrhkddud2.rememberu.sharedpreferences.memo.hash";

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

    public static boolean setUID(Context context, String value) {
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_UID, value);
        return editor.commit();
    }

    public static boolean setPersonHash(Context context, String value) {
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PERSON_HASH, value);
        return editor.commit();
    }

    public static boolean setMemoHash(Context context, String value) {
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MEMO_HASH, value);
        return editor.commit();
    }

    public static boolean setDownloadDate(Context context, long value) {
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(USER_DOWNLOAD_DATE, value);
        return editor.commit();
    }

    public static boolean setUploadDate(Context context, long value) {
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(USER_UPLOAD_DATE, value);
        return editor.commit();
    }

    public static long getUploadDate(Context context) {
        return getPreferences(context).getLong(USER_UPLOAD_DATE, 0);
    }

    public static long getDownloadDate(Context context) {
        return getPreferences(context).getLong(USER_DOWNLOAD_DATE, 0);
    }

    public static String getMemoHash(Context context) {
        return getPreferences(context).getString(MEMO_HASH, null);
    }

    public static String getPersonHash(Context context) {
        return getPreferences(context).getString(PERSON_HASH, null);
    }

    public static String getUID(Context context) {
        return getPreferences(context).getString(USER_UID, null);
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
