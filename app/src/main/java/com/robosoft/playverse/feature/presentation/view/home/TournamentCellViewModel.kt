package com.robosoft.playverse.feature.presentation.view.home

import androidx.lifecycle.ViewModel
import com.playverse.data.models.DataItem
import com.playverse.data.models.FreeGameplay
import com.playverse.data.models.TournamentDataList
import com.robosoft.playverse.feature.presentation.viewModel.FreePlayViewModel

class TournamentCellViewModel(val data: TournamentDataCell) : ViewModel() {
    fun TournamentDataList(): TournamentDataList {
        return TournamentDataList(
            buyIn = data.buyIn,
            status = data.status,
            takeRate = data.takeRate,
            frequency = data.frequency,
            fromDate = data.fromDate,
            toDate = data.toDate,
            game = data.game,
            gameId = data.gameId,
            gameLink = data.gameLink,
            gamePlays = data.gamePlays,
            gameIcon = data.gameIcon,
            id = data.id,
            rewardsWon = data.rewardsWon,
            userCount = data.userCount,
            videoUrl = data.videoUrl,
            category = data.category,
            rewards =data.rewards
        )
    }
}