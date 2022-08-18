package com.robosoft.playverse.feature.presentation.view.gamedetails

interface GameClickListener {

    fun gameLoad(id: String, gameUrl: String, sessionId: String)
    fun gameDetails(id: String)
    fun statsDetails(id: String)
    fun gameLoadfromPopup(gameUrl: String,entryFee:Int, gameId: Int,tournamentId: Int,)
}