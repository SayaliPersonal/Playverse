package com.robosoft.playverse.feature.presentation.view.home

import com.robosoft.playverse.feature.presentation.view.gamedetails.GameClickListener

interface BuyInClickListener {

    fun buyInPopUp(
        gameId: Int,
        entryFee: Int,
        tournamentId: Int,
        listener: GameClickListener,
        url: String
    )
}