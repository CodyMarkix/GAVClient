package com.markix.gavclient.logic.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel

data class IOCHomeData(
    var balls: Int = 0
)

class IOCHomeViewModel(application: Application) : AndroidViewModel(application) {

}