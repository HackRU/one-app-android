package org.hackru.oneapp.hackru

import android.content.Context

object Utils {
        fun convertDpToPx(context: Context, dp: Float): Float {
            return dp * context.resources.displayMetrics.density
        }
}