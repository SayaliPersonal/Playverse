package com.playverse.data.models

import com.google.gson.annotations.SerializedName

sealed class AvatarResponse {
    data class MainResponse(
        @field:SerializedName("code") val code: Int = 0,
        @field:SerializedName("data") val data: AvatarUsernameResponse = AvatarUsernameResponse(),
        @field:SerializedName("success") val success: Boolean = false
    ): AvatarResponse()

    data class AvatarResponseBody(
        @SerializedName("image") val image: String?,
    ): AvatarResponse()

    data class AvatarUsernameResponse(
        @SerializedName("name") val name: String = "",
        @SerializedName("url") val url: String = "",
        @SerializedName("user_id") val user_id: String = "",
    ): AvatarResponse()
}