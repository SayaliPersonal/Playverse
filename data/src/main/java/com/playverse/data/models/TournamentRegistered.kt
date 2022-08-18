package com.playverse.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TournamentRegistered (

    @field:SerializedName("code")
    val code: Int = 0,

    @field:SerializedName("data")
    val data: Register = Register(),

    @field:SerializedName("success")
    val success: Boolean = false

    ) : Parcelable

@Parcelize
data class Register(
    @field:SerializedName("hasUserRegisteredTournament")
    val hasUserRegisteredTournament: Boolean = false

) : Parcelable

