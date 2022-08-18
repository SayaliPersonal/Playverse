package com.playverse.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecentGameResponse(

	@field:SerializedName("code")
	val code: Int = 0,

	@field:SerializedName("data")
	val data: ArrayList<DataItem> = ArrayList(),

	@field:SerializedName("success")
	val success: Boolean = false
) : Parcelable

@Parcelize
data class DataItem(

	@field:SerializedName("start_time")
	val startTime: String = "",

	@field:SerializedName("current_high_score")
	val currentHighScore: Int = 0,

	@field:SerializedName("user_score")
	val userScore: Int = 0,

	@field:SerializedName("target_reward")
	val targetReward: Double? = 0.0,

	@field:SerializedName("game_url")
	val gameUrl: String? = "",

	@field:SerializedName("end_time")
	val endTime: String = "",

	@field:SerializedName("target_score")
	val targetScore: Int? = 0,

	@field:SerializedName("id")
	val id: Int = 0,

	@field:SerializedName("user_rank")
	val userRank: Int? = 0,

	@field:SerializedName("category")
	val category: Int = 0
) : Parcelable
