package com.playverse.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class TournamentListingResponse(

    @field:SerializedName("code")
    val code: Int = 0,

    @field:SerializedName("data")
    val data: TournamentData = TournamentData(),

    @field:SerializedName("success")
    val success: Boolean = false

) : Parcelable

@Parcelize
data class TournamentData(

    @field:SerializedName("current_page")
    val currentPage: Int = 0,

    @field:SerializedName("total_items")
    val totalItems: Int = 0,

    @field:SerializedName("tournaments")
    val tournaments: ArrayList<TournamentDataList> = ArrayList()

) : Parcelable

@Parcelize
data class TournamentDataList(

    @field:SerializedName("buy_in")
    val buyIn: Int? = 0,

    @field:SerializedName("status")
    val status: String? = "",

    @field:SerializedName("take_rate")
    val takeRate: Int? = 0,

    @field:SerializedName("frequency")
    val frequency: String? = "",

    @field:SerializedName("from_date")
    val fromDate: String? = "",

    @field:SerializedName("to_date")
    val toDate: String? = "",

    @field:SerializedName("game")
    val game: String? = "",

    @field:SerializedName("game_id")
    val gameId: Int? = 0,

    @field:SerializedName("game_link")
    val gameLink: String? = "",

    @field:SerializedName("gameplays")
    val gamePlays: Int? = 0,

    @field:SerializedName("game_icon")
    val gameIcon: String? = "",

    @field:SerializedName("id")
    val id: Int? = 0,

    @field:SerializedName("rewards_won")
    val rewardsWon: Int? = 0,

    @field:SerializedName("user_count")
    val userCount: Int? = 0,

    @field:SerializedName("video_url")
    val videoUrl: String? = "",

    @field:SerializedName("category")
    val category: String? = "",

    @field:SerializedName("rewards")
    val rewards: Int? = 0,

    ) : Parcelable

