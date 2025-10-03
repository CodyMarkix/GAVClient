package com.markix.gavclient.utils

import android.content.Context
import android.content.res.Configuration

class Utils {
    companion object {
        fun isNightMode(context: Context): Boolean {
            val nightModeFlags = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val isNightModeOn = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
            return isNightModeOn
        }
    }
}