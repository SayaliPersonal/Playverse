package com.robosoft.playverse.feature.presentation.view.home

import com.playverse.data.models.TournamentDataList

interface TournamentGamePlayClickListener {
    fun getTournamentPlayData(position: Int, data: TournamentDataList)
}