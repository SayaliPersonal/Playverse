package com.playverse.data.models

import com.google.gson.annotations.SerializedName

/**Login*/
data class LoginResponse(
    @field:SerializedName("type") var type: String = "",
    @field:SerializedName("request_id") var request_id: String = "",
    ) {
}