package com.playverse.data.models

data class SessionRequestBody(
    var game_id: String,
    var user_id: String,
    var game_mode_type: String,
    var game_mode_id: String
)