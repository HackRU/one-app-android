package org.hackru.oneapp.hackru.api.Login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Sean on 3/21/2018.
 */

public class AuthToken {
    static final String PREF_AUTH_TOKEN= "authtoken";

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
}
