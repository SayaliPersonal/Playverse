package com.robosoft.playverse.feature.presentation.view.home

import com.google.gson.annotations.SerializedName

class FreePlayCell(
    var description: String ,
    var gameIcon: String,
    var gameLink: String,
    var gamePlays: Int ,
    var id: Int ,
    var rewards: Int,
    var score: Int,
    var userCount: Int ,
    var videoUrl: String ,
    var game: String ,
    var gameId: Int,

) {
}

class VideoUrlCell(
    var videoUrl: String
){

}