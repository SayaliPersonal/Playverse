package com.playverse.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FreePlayListingResponse(

    @field:SerializedName("code")
    val code: Int = 0,

    @field:SerializedName("data")
    val data: FreePlayData = FreePlayData(),

    @field:SerializedName("success")
    val success: Boolean = false

) : Parcelable

@Parcelize
data class FreePlayData(

    @field:SerializedName("current_page")
    val currentPage: Int = 0,

    @field:SerializedName("total_items")
    val totalItems: Int = 0,

    @field:SerializedName("free_gameplay")
    val freeGameplay: ArrayList<FreeGameplay> = ArrayList()

) : Parcelable

@Parcelize
data class FreeGameplay(

    @field:SerializedName("description")
    val description: String = "",

    @field:SerializedName("game_icon")
    val gameIcon: String = "",

    @field:SerializedName("game_link")
    val gameLink: String = "",

    @field:SerializedName("gameplays")
    val gamePlays: Int = 0,

    @field:SerializedName("id")
    val id: Int = 0,

    @field:SerializedName("reward")
    val rewards: Int = 0,

    @field:SerializedName("score")
    val score: Int = 0,

    @field:SerializedName("user_count")
    val userCount: Int = 0,

    @field:SerializedName("video_url")
    val videoUrl: String = "",

    @field:SerializedName("game")
    val game: String = "",

    @field:SerializedName("game_id")
    val gameId: Int = 0,

    ) : Parcelable

