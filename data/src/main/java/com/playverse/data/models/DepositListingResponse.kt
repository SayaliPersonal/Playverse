package com.playverse.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DepositListingResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: DepositListData,

	@field:SerializedName("success")
	val success: Boolean
) : Parcelable

@Parcelize
data class GamesItem(

	@field:SerializedName("rewards_won")
	val rewardsWon: Double,

	@field:SerializedName("banners")
	val banners: List<String> = ArrayList(),

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("developer")
	val developer: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("game_plays")
	val gamePlays: Int,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("users_played")
	val usersPlayed: Int
) : Parcelable

@Parcelize
data class DepositListData(

	@field:SerializedName("games")
	val games: List<GamesItem>,

	@field:SerializedName("total_items")
	val totalItems: Int,

	@field:SerializedName("current_page")
	val currentPage: Int
) : Parcelable
