package com.robosoft.playverse.feature.presentation.view.profile

import com.playverse.data.models.DataItem
import com.playverse.data.models.ProfileTournamentResponse


interface ProfileClickListener {
    fun onViewStandingsClick(id: String , data: DataItem)
    fun onGameLoad(url: String)
    fun onStatsClick(id: String , data: DataItem, gameId: String)
    fun getItemData(position: Int, profileTournamentResponse: ProfileTournamentResponse)
}