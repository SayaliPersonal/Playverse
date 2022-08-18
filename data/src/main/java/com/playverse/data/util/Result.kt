package com.playverse.data.util

/**
 * Created by Subham on 7/11/2022.
 */
sealed class Result<out T>(val status: Status) {

    enum class Status {
        SUCCESS,
        FAILURE,
        LOADING
    }

    data class Success<T>(val data: T? = null) : Result<T>(Status.SUCCESS)

    data class Failure(val errorResponse: ServerError) : Result<Nothing>(Status.FAILURE)

    class Loading : Result<Nothing>(Status.LOADING)
}