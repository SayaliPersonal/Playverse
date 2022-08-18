package com.robosoft.playverse.feature.presentation.view.gamedetails

import com.playverse.data.models.TournamentsItem

interface TournamentGameClickListener {

    fun getGameData(position: Int, tournamentsItem: TournamentsItem)
}