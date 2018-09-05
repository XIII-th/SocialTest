package com.xiiilab.socialtest

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.squareup.picasso.Picasso
import com.xiiilab.socialtest.auth.AbstractAuthService
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by XIII-th on 05.09.2018
 */
object AvatarService {

    private val COMPRESSION_FORMAT = Bitmap.CompressFormat.PNG
    private lateinit var mCacheDir : File

    fun init(appContext: Context) {
        mCacheDir = appContext.cacheDir
    }

    fun getAvatarPath(authService: AbstractAuthService): Maybe<String> {
        return getCachedImagePath(authService.getServiceName()).
                switchIfEmpty(download(authService)).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
    }

    fun removeAvatar(serviceName: String) {
        Single.fromCallable { getCacheFile(serviceName).delete() }.
                // waiting for cancellation of downloading task
                delay(1, TimeUnit.SECONDS).
                subscribeOn(Schedulers.io()).
                subscribe{ result -> Log.d(serviceName, "Avatar file " +
                        if (result) "successfully deleted" else "not deleted")}
    }

    private fun getCachedImagePath(serviceName: String): Maybe<String> {
        return Maybe.fromCallable { getCacheFile(serviceName).takeIf(File::exists)?.let(File::getAbsolutePath) }
    }

    private fun download(authService: AbstractAuthService): Maybe<String> {
        return Maybe.wrap(authService.avatarUrl()).map { stringUrl->
            val bitmap = Picasso.get().load(stringUrl).get()
            val file = getCacheFile(authService.getServiceName())
            file.outputStream().use { bitmap.compress(COMPRESSION_FORMAT, 100, it) }
            file.takeIf(File::exists)?.let(File::getAbsolutePath)
        }
    }
    private fun getCacheFile(serviceName: String): File {
        val avatarDir = File(mCacheDir, "avatars")
        if (!avatarDir.exists() && !avatarDir.mkdirs())
            throw IOException("Unable to create avatar '$avatarDir' for service $serviceName")
        return File(avatarDir, "${serviceName}_avatar.$COMPRESSION_FORMAT")
    }
}