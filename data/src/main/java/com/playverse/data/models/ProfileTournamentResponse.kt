package com.playverse.data.models

import com.google.gson.annotations.SerializedName


data class ProfileTournamentResponse(
    @field:SerializedName("id") var id: Int,
    @field:SerializedName("game_id") var game_id: Int,
    @field:SerializedName("category") var category: Int,
    @field:SerializedName("status") var status: Int,
    @field:SerializedName("game_name") var game_name: String = "",
    @field:SerializedName("game_url") var game_url: String = "",
    @field:SerializedName("start_time") var start_time: String? = "",
    @field:SerializedName("end_time") var end_time: String = "",
    @field:SerializedName("user_rank") var user_rank: Int,
    @field:SerializedName("user_score") var user_score: Int,
    @field:SerializedName("current_high_score") var current_high_score: Int,
    @field:SerializedName("target_reward") var target_reward: Int,
    @field:SerializedName("target_score") var target_score: Int,

    ) {
    fun toDataItem(): DataItem {
        return DataItem(
            startTime = start_time ?: "",
            endTime = end_time,
            category = category,
            id = id,
            userRank = user_rank,
            currentHighScore = current_high_score,
            targetReward = target_reward.toDouble(),
            targetScore = target_score,
            userScore = user_score,
            gameUrl = game_url

            )
    }
}