package com.robosoft.playverse.feature.presentation.view.gamedetails

import com.playverse.data.models.DataItem


interface RecentGamesStatsClickListener {
    fun getRecentStatsItemPosition(position: Int, dataItem: DataItem)
    fun getRecentGameUrl(position: Int, dataItem: DataItem)
    fun onViewStandingsClick(id: String , data: DataItem)
}