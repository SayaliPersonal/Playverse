package com.playverse.data.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import com.google.gson.Gson
import java.net.UnknownHostException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response

/**
 * Created by Subham on 7/11/2022.
 */
@Suppress("DEPRECATION")
@SuppressLint("MissingPermission")
fun isInternetConnected(getApplicationContext: Context): Boolean {
    var status = false
    val cm =
        getApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (cm.activeNetwork != null && cm.getNetworkCapabilities(cm.activeNetwork) != null) {
            // connected to the internet
            status = true
        }
    } else {
        if (cm.activeNetworkInfo != null && cm.activeNetworkInfo?.isConnectedOrConnecting == true) {
            // connected to the internet
            status = true
        }
    }
    return status
}

@ExperimentalCoroutinesApi
fun <T> getDataFromNetwork(
    callNetwork: suspend () -> Response<T>,
    context: Context? = null
): Flow<Result<T>> {
    return flow {
        if (context?.let { isInternetConnected(it) } == false) {
            emit(
                Result.Failure(
                    ServerError(
                        "",
                        9001,
                        throwable = Throwable(UnknownHostException())
                    )
                )
            )
            return@flow
        }
        emit(Result.Loading())
        val apiResponse = callNetwork()
        val responseBody = apiResponse.body()



        if (responseBody != null && apiResponse.isSuccessful) {
            emit(
                Result.Success(responseBody)
            )
        } else {
            emit(
                Result.Failure(
                    ServerError(
                        apiResponse.message(),
                        apiResponse.code(),
                        apiResponse.errorBody(),
                        Throwable(message = "ErrorResponseCode=" + apiResponse.code() + apiResponse.message())
                    )
                )
            )
        }
    }.catch { exception ->
        emit(
            Result.Failure(ServerError(throwable = exception))
        )
    }
}
