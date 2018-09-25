package org.hackru.oneapp.hackru

import android.content.Context
import android.preference.PreferenceManager

object Utils {
        fun convertDpToPx(context: Context, dp: Float): Float {
            return dp * context.resources.displayMetrics.density
        }

    // TODO: Make this a singleton injected with Dagger 2
    object SharedPreferences {
        private val KEY_AUTH_TOKEN = "authtoken"
        private val KEY_EMAIL = "email"
        private val KEY_CAN_SCAN = "can_scan"
        // TODO: Set up scanner for allowing waitlist
        private val KEY_ALLOW_WAITLIST = "allow_only_accepted"

        fun setAuthToken(context: Context, authToken: String?) {
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(KEY_AUTH_TOKEN, authToken)
                    .commit()
        }

        fun getAuthToken(context: Context): String? {
            return PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(KEY_AUTH_TOKEN, null)
        }

        fun setEmail(context: Context, email: String?) {
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(KEY_EMAIL, email)
                    .commit()
        }

        fun getEmail(context: Context): String? {
            return PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(KEY_EMAIL, null)
        }

        fun setCanScan(context: Context, canScan: Boolean) {
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putBoolean(KEY_CAN_SCAN, canScan)
                    .commit()
        }

        fun getCanScan(context: Context): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(KEY_CAN_SCAN, false)
        }

    }
}