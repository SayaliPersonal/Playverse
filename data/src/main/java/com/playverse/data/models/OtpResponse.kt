package com.playverse.data.models

import com.google.gson.annotations.SerializedName

/**Login*/
data class OtpResponse(
    @field:SerializedName("message") var message: String = "",
    @field:SerializedName("type") var type: String = "",
    @field:SerializedName("user_id") var user_id: String = "",
    @field:SerializedName("name") var name: String = "",
    @field:SerializedName("url") var url: String = "",
    @field:SerializedName("userAuthResponse") var UserAuthResponse: UserAuthResponse = UserAuthResponse()
)

data class UserAuthResponse(
    @field:SerializedName("access_token") val accessToken: String = "",
    @field:SerializedName("refresh_token") val refreshToken: String = ""
)

data class MainOtpResponse(
    @field:SerializedName("code") val code: Int = 0,
    @field:SerializedName("data") val data: OtpResponse = OtpResponse(),
    @field:SerializedName("success") val success: Boolean = false
)

data class ProfileResponse(
    @field:SerializedName("name") var name: String = "",
    @field:SerializedName("url") var url: String = "",
    @field:SerializedName("user_id") var user_id: String = "",

    ) {
}

data class ProfileMainResponse(
    @field:SerializedName("code") val code: Int = 0,
    @field:SerializedName("data") val data: ProfileResponse = ProfileResponse(),
    @field:SerializedName("success") val success: Boolean = false
) {
}