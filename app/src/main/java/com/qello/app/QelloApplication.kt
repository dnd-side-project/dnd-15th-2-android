package com.qello.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QelloApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
