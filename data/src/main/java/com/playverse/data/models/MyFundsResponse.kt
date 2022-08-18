package com.playverse.data.models

import com.google.gson.annotations.SerializedName

data class MyFundsResponse(
    @field:SerializedName("depositCash") var depositCash: Int = 0,
    @field:SerializedName("winningCash") var winningCash: Int = 0,
    @field:SerializedName("totalCash") var totalCash: Int = 0,
    @field:SerializedName("user_id") var user_id: Int = 0,
) {
}