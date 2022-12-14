package com.robosoft.playverse


import androidx.multidex.MultiDexApplication
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import dagger.hilt.android.HiltAndroidApp
import java.io.File
import java.util.concurrent.ConcurrentHashMap

@HiltAndroidApp
class PlayVerseApp : MultiDexApplication() {

    companion object {
        var simpleCache: SimpleCache? = null
        var leastRecentlyUsedCacheEvictor: LeastRecentlyUsedCacheEvictor? = null
        var exoDatabaseProvider: ExoDatabaseProvider? = null
        var exoPlayerCacheSize: Long = 90 * 1024 * 1024
    }

    override fun onCreate() {
        super.onCreate()
        val INSTALL_APK_INFO: ConcurrentHashMap<String, File> = ConcurrentHashMap<String, File>()
        if (leastRecentlyUsedCacheEvictor == null) {
            leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize)
        }

        if (exoDatabaseProvider == null) {
            exoDatabaseProvider = ExoDatabaseProvider(this)
        }

        if (simpleCache == null) {
            simpleCache =
                exoDatabaseProvider?.let {
                    SimpleCache(cacheDir, leastRecentlyUsedCacheEvictor!!,
                        it
                    )
                }
        }
    }
}