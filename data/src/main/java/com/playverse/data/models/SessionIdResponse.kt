package com.playverse.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SessionIdResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: SessionData,

    @field:SerializedName("success")
    val success: Boolean
) : Parcelable

@Parcelize
data class SessionData(

    @field:SerializedName("session_id")
    val sessionId: String
) : Parcelable
