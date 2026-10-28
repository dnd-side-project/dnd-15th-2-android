package com.qello.app

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import com.mapbox.common.MapboxOptions
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class QelloApplication : Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        MapboxOptions.accessToken = BuildConfig.MAPBOX_ACCESS_TOKEN
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader =
        ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, COIL_MEMORY_CACHE_SIZE_PERCENT)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve(COIL_DISK_CACHE_DIR_NAME))
                    .maxSizeBytes(COIL_DISK_CACHE_MAX_SIZE_BYTES)
                    .build()
            }
            .build()

    private companion object {
        const val COIL_MEMORY_CACHE_SIZE_PERCENT = 0.15
        const val COIL_DISK_CACHE_DIR_NAME = "coil_image_cache"
        const val COIL_DISK_CACHE_MAX_SIZE_BYTES = 1024 * 1024 * 30L
    }
}
