package com.markix.gavclient.logic.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import com.markix.gavclient.R
import okhttp3.OkHttpClient
import org.kohsuke.github.GitHub

class GAVClientViewModel(application: Application) : AndroidViewModel(application) {
    private val github = GitHub.connectAnonymously()

    fun checkForUpdates(): Boolean {
        val pkgName = application.packageName
        val pkgInfo = application.packageManager.getPackageInfo(pkgName, 0)

        // Obtainium is a package manager for Android apps from GitHub/GitLab/Codeberg/whatever,
        // it's a better idea to let it do its thing
        if (application.packageManager.getInstallerPackageName(pkgName).equals("com.github.imranr.obtainium")) {
            return false
        }

        val release = github.getRepository("GAVClient").latestRelease ?: return false
        return (release.tagName == pkgInfo.versionName)
    }
}