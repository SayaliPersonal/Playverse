package com.playverse.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Subham on 7/12/2022.
 */
@Parcelize
data class GameBannerAndInfoResponse(

    @field:SerializedName("code")
    val code: Int = 0,

    @field:SerializedName("data")
    val data: Data = Data(),

    @field:SerializedName("success")
    val success: Boolean = false
) : Parcelable

@Parcelize
data class Data(

    @field:SerializedName("game_url")
    val gameUrl: String = "",

    @field:SerializedName("icon_url")
    val iconUrl: String = "",

    @field:SerializedName("rewards_won")
    val rewardsWon: Double = 0.0,

    @field:SerializedName("name")
    val name: String? = "",

    @field:SerializedName("developer")
    val developer: String? = "",

    @field:SerializedName("id")
    val id: Int = 0,

    @field:SerializedName("game_plays")
    val gamePlays: Int = 0,

    @field:SerializedName("banners")
    val banners: List<String> = ArrayList(),

    @field:SerializedName("users_played")
    val usersPlayed: Int? = 0,

    @field:SerializedName("status")
    val status: String? = "",

    @field:SerializedName("tournaments")
    val tournaments: ArrayList<TournamentsItem> = ArrayList(),


    @field:SerializedName("free_game_plays")
    val freeGamePlays: List<FreeGamePlaysItem> = ArrayList()

) : Parcelable

@Parcelize
data class FreeGamePlaysItem(

    @field:SerializedName("reward")
    val reward: Double = 0.0,

    @field:SerializedName("score")
    val score: Int = 0,

    @field:SerializedName("description")
    val description: String = "",

    @field:SerializedName("id")
    val id: Int = 0
) : Parcelable


@Parcelize
data class TournamentsItem(

    @field:SerializedName("start_time")
    val startTime: String = "",

    @field:SerializedName("end_time")
    val endTime: String = "",

    @field:SerializedName("rank")
    val rank: Int = 0,

    @field:SerializedName("players")
    val players: Int = 0,

    @field:SerializedName("free_game_plays")
    val freeGamePlays: Int = 0,

    @field:SerializedName("total_rewards")
    val totalRewards: Double = 0.0,

    @field:SerializedName("id")
    val id: Int = 0,

    @field:SerializedName("entry_fee")
    val entryFee: Double = 0.0,

    @field:SerializedName("category")
    val category: Int = 0
) : Parcelable
