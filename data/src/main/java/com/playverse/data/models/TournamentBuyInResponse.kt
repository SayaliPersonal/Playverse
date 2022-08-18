package com.playverse.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TournamentBuyInResponse(

    @field:SerializedName("code")
    val code: Int = 0,

    @field:SerializedName("success")
    val success: Boolean = false

) : Parcelable


data class BuyinPopUpResponse(
    val game_id: Int,
    val entry_fee: Int,
    val tournament_id: Int,
    val user_id: Int
)
