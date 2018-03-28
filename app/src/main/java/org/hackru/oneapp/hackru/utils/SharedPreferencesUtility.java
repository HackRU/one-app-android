package org.hackru.oneapp.hackru.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Sean on 3/21/2018.
 */

public class SharedPreferencesUtility {
    static final String PREF_AUTH_TOKEN= "authtoken";
    static final String PREF_EMAIL= "email";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setAuthToken(Context ctx, String token) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_AUTH_TOKEN, token);
        editor.commit();
    }

    public static String getAuthToken(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_AUTH_TOKEN, "");
    }

    public static void setEmail(Context ctx, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_EMAIL, email);
        editor.commit();
    }

    public static String getEmail(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_EMAIL, "");
    }
}
