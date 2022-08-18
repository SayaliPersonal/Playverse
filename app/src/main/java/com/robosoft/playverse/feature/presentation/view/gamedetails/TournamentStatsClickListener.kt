package com.robosoft.playverse.feature.presentation.view.gamedetails

import com.playverse.data.models.TournamentsItem


interface TournamentStatsClickListener {
    fun getStatsItemPosition(position: Int, tournamentsItem: TournamentsItem)
}