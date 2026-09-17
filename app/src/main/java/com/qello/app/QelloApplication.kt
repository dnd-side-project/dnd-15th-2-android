package com.qello.app

import android.app.Application
import com.mapbox.common.MapboxOptions
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class QelloApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        MapboxOptions.accessToken = BuildConfig.MAPBOX_ACCESS_TOKEN
    }
}
