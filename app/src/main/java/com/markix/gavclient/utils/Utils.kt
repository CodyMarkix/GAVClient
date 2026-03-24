package com.markix.gavclient.utils

import android.content.Context
import android.content.res.Configuration
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class Utils {
    companion object {
        fun isNightMode(context: Context): Boolean {
            val nightModeFlags = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val isNightModeOn = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
            return isNightModeOn
        }
    }
}

// Lines 17-18 from StackOverflow: https://stackoverflow.com/questions/64356174/insert-a-char-into-a-string-at-specific-position-x-in-kotlin#64356676
fun String.addCharAtIndex(char: Char, index: Int) =
    StringBuilder(this).apply { insert(index, char) }.toString()
