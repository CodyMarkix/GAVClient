package com.markix.gavclient.logic.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import com.markix.gavclient.R
import com.markix.gavclient.logic.data.github.GithubRelease
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Request
import org.kohsuke.github.GitHub

class GAVClientViewModel(application: Application) : AndroidViewModel(application) {
    private val httpClient = OkHttpClient()

    suspend fun checkForUpdates(): Boolean {
        val pkgName = application.packageName
        val pkgInfo = application.packageManager.getPackageInfo(pkgName, 0)
        val pkgVersionName = pkgInfo.versionName
        Log.d("com.markix.gavclient", "Package version: ${pkgVersionName}")

        // Obtainium is a package manager for Android apps from GitHub/GitLab/Codeberg/whatever,
        // it's a better idea to let it do its thing
        for (app in application.packageManager.getInstalledPackages(0)) {
            Log.d("com.markix.gavclient", app.packageName)
            if (app.packageName == "dev.imranr.obtainium") {
                Log.d("com.markix.gavclient", "OBTANIUM DETECTED: Skipping auto-update check")
                return false
            }
        }

        val request = Request.Builder()
            .url("https://api.github.com/repos/CodyMarkix/GAVClient/releases/latest")
            .build()

        val response = withContext(Dispatchers.IO) {
            httpClient.newCall(request).execute().body!!.string()
        }

        Log.d("com.markix.gavclient", response)
        try {
            val release: GithubRelease = Json.decodeFromString<GithubRelease>(response)
            Log.d("com.markix.gavclient", "Github release: ${release.tag_name}")
            return (!release.tag_name.equals(pkgVersionName))

        } catch (e: IllegalArgumentException) {
            Log.e("com.markix.gavclient", e.toString())
            return false
        }
    }
}