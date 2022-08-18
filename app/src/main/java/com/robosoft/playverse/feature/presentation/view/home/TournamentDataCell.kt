package com.robosoft.playverse.feature.presentation.view.home

import com.google.gson.annotations.SerializedName

class TournamentDataCell(
    var buyIn: Int ,
    var status: String ,
    var takeRate: Int ,
    var frequency: String ,
    var fromDate: String ,
    var toDate: String ,
    var game: String ,
    var gameId: Int ,
    var gameLink: String ,
    var gamePlays: Int,
    var gameIcon: String ,
    var id: Int ,
    var rewardsWon: Int ,
    var userCount: Int ,
    var videoUrl: String ,
    var category: String,
    var rewards: Int
) {
}