package com.playverse.data.util


import okhttp3.ResponseBody

/**
 * Created by Subham on 7/11/2022.
 */
data class ServerError(
    val errorMsg: String ?=null,
    val errorCode: Int? = null,
    val errorResponse: ResponseBody? = null,
    val throwable: Throwable? = null
)
