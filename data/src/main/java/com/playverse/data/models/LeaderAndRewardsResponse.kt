package com.playverse.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LeaderAndRewardsResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: LeaderData,

	@field:SerializedName("success")
	val success: Boolean
) : Parcelable

@Parcelize
data class RewardsItem(

	@field:SerializedName("to_rank")
	val toRank: Int,

	@field:SerializedName("rewards")
	val rewards: Double,

	@field:SerializedName("from_rank")
	val fromRank: Int
) : Parcelable

@Parcelize
data class LeaderboardItem(

	@field:SerializedName("score")
	val score: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("user_name")
	val userName: String,

	@field:SerializedName("rank")
	val rank: Int,

	@field:SerializedName("profile_url")
	val profileUrl: String
) : Parcelable

@Parcelize
data class LeaderData(

	@field:SerializedName("leaderboard")
	val leaderboard: List<LeaderboardItem>,

	@field:SerializedName("rewards")
	val rewards: List<RewardsItem>
) : Parcelable
