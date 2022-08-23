package com.playverse.data.models

data class VersionUpdate(
    val code: Int,
    val `data`: Version_Data,
    val success: Boolean
) {
    data class Version_Data(
        val androidAppLink: String,
        val androidAppVersion: String,
        val androidVersionCode: String
    )
}