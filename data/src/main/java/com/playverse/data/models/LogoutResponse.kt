package com.playverse.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LogoutResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: LogoutData,

    @field:SerializedName("success")
    val success: Boolean
) : Parcelable

@Parcelize
data class LogoutData(

    @field:SerializedName("resultInfo")
    val resultInfo: ResultInfo,

    @field:SerializedName("token")
    val token: Token
) : Parcelable

@Parcelize
data class Token(

    @field:SerializedName("accessToken")
    val accessToken: String
) : Parcelable

@Parcelize
data class ResultInfo(

    @field:SerializedName("resultStatus")
    val resultStatus: String,

    @field:SerializedName("resultCode")
    val resultCode: String,

    @field:SerializedName("resultCodeId")
    val resultCodeId: String,

    @field:SerializedName("resultMsg")
    val resultMsg: String
) : Parcelable

data class LogoutRequestBody(var accessToken: String)
